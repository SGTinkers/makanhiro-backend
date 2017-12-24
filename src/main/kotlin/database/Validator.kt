package database

import io.ktor.request.MultiPartData
import io.ktor.request.PartData
import io.ktor.request.forEachPart
import models.*
import java.io.File
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class Validator {
    /**
     * This ensures that every post from users are legitimate so that it reduces the work the SQL database
     * does.
     * @param unValidatedPost
     * @param user
     * @return
     */
    fun validatePost(unValidatedPost: HashMap<String,String>, user: User): Post =
            Post(
                    if (!unValidatedPost.containsKey("postId"))
                        Utils.sha256(user.facebookId + unValidatedPost["description"] + LocalDateTime.now())
                    else unValidatedPost["postId"].toString(),
                    validateLocation(unValidatedPost["locationId"]),
                    validateTimestamp(unValidatedPost["expiryTime"]),
                    unValidatedPost["images"].toImageList(),
                    validateDietary(unValidatedPost["dietary"].toString()),
                    unValidatedPost["description"].toString(),
                    FoodAvailability.valueOf(unValidatedPost["foodAvailability"].toString()),
                    Utils.timeStampNow(),
                    Utils.timeStampNow(),
                    user.userId
            )

    /**
     * Validates location provided by User
     * @param unValidatedPost
     * @return Location
     */
    private fun validateLocation(unValidatedPost: String?): Location {
        val locationInt = try {
            unValidatedPost?.toInt()
        } catch (e: NumberFormatException) {
            e.printStackTrace()
            null
        }
        return when (locationInt) {
            null -> throw InvalidPostObject("LocationId cannot be null")
            else -> {
                val location = LocationSource().getLocationSourceById(locationInt)
                when (location) {
                    null -> throw InvalidPostObject("Invalid location")
                    else -> location
                }
            }
        }

    }

    /**
     * Validates Dietary Enums
     * @param dietaryString
     * @return Dietary?
     */
    private fun validateDietary(dietaryString: String?): Dietary? =
            if (dietaryString.isNullOrBlank()) null
                    else dietaryString?.let { Dietary.valueOf(it) }

    /**
     * Validates TimeStamp with Format of dd-MM-yyyy hh:mm:ss
     * @param dateTimeInString
     * @return TimeStamp
     */
    private fun validateTimestamp(dateTimeInString: String?): Timestamp =
            Timestamp(SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
                    .parse(dateTimeInString).time)

    /**
     * Validates MultiPartPost which may contain images
     * Images are limited to 3mb and only takes in
     * jpg || jpeg || png
     * @param user
     * @param mPartData
     * @return HashMap<String,String>
     */
    suspend fun validateMultiPartPost(user:User,mPartData: MultiPartData):HashMap<String,String>{
        val postHm = HashMap<String,String>()
        mPartData.forEachPart {
            if(it is PartData.FormItem) {
                when(it.partName){
                    "locationId" -> postHm.put(it.partName.toString(),it.value)
                    "expiryTime" -> postHm.put(it.partName.toString(),it.value)
                    "dietary" -> postHm.put(it.partName.toString(),it.value)
                    "description" -> postHm.put(it.partName.toString(),it.value)
                    "foodAvailability" -> postHm.put(it.partName.toString(),it.value)
                    "postId" -> postHm.put(it.partName.toString(),it.value)
                }
            }else if (it is PartData.FileItem){
                val ext = File(it.originalFileName).extension
                val fileSizeInMb = File(it.originalFileName).length() / 1024 / 1024
                var totalSize:Long = 0

                if(!ext.isValidFileExt())
                    throw InvalidFileExtension("Invalid file ext of $ext was found!")

                if(fileSizeInMb > MAX_SIZE)
                    throw FileSizeTooBig("File size is $fileSizeInMb. Max file size is $FILE_SIZE_TOO_BIG")

                val fileName = "${System.currentTimeMillis()}.$ext"
                val dir = File(IMAGES_DIR  + "\\public\\"+ user.userId)
                val imageDir = File(dir, fileName)

                if(!dir.exists())
                    try {
                        dir.mkdir()
                    }catch (e:SecurityException){
                        e.printStackTrace()
                    }

                it.streamProvider().use { its -> imageDir.outputStream().buffered().use {
                    its.copyTo(it)
                }}

                if(totalSize > 9)
                    throw FileSizeTooBig("You have exceeded file limit of 9mb! Your file size $totalSize")

                totalSize += if(!postHm.containsKey("images")){
                    postHm.put("images", fileName)
                    fileSizeInMb
                } else {
                    postHm.put("images", postHm["images"] + ",$fileName")
                    fileSizeInMb
                }
            }
        }
        return postHm
    }
}