package database

import io.ktor.request.MultiPartData
import io.ktor.request.PartData
import io.ktor.request.forEachPart
import io.ktor.util.ValuesMap
import models.*
import java.io.File
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.time.LocalDateTime

class Validator {
    fun validatePost(unValidatedPost: HashMap<String,String>, user: User): Post =
            Post(
                    Utils.sha256(user.facebookId + unValidatedPost["description"] + LocalDateTime.now()),
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

    private fun validateDietary(dietaryString: String?): Dietary? =
            if (dietaryString.isNullOrBlank()) null
                    else dietaryString?.let { Dietary.valueOf(it) }

    private fun validateTimestamp(dateTimeInString: String?): Timestamp =
            Timestamp(SimpleDateFormat("dd-MM-yyyy hh:mm:ss")
                    .parse(dateTimeInString).time)

    suspend fun validateMultiPartPost(user:User,mpartData: MultiPartData):HashMap<String,String>{
        //Validate normal fields
        val postHm = HashMap<String,String>()
        mpartData.forEachPart {
            if(it is PartData.FormItem) {
                when(it.partName){
                    "locationId" -> postHm.put(it.partName.toString(),it.value)
                    "expiryTime" -> postHm.put(it.partName.toString(),it.value)
                    "dietary" -> postHm.put(it.partName.toString(),it.value)
                    "description" -> postHm.put(it.partName.toString(),it.value)
                    "foodAvailability" -> postHm.put(it.partName.toString(),it.value)
                }
            }else if (it is PartData.FileItem){
                val ext = File(it.originalFileName).extension
                val fileSizeInMb = File(it.originalFileName).length() / 1024 / 1024
                if(ext != "jpg")
                    throw InvalidFileExtension("Invalid File extension. File with extension of $ext was found")
                if(fileSizeInMb > MAX_SIZE)
                    throw FileSizeTooBig("File size is $fileSizeInMb. Max file size is $FILE_SIZE_TOO_BIG")

                val fileName = "${System.currentTimeMillis()}.$ext"
                val dir = File(IMAGES_DIR + user.userId)
                val imageDir = File(dir,
                        fileName)
                if(!dir.exists())
                    try {
                        dir.mkdir()
                    }catch (e:SecurityException){
                        e.printStackTrace()
                    }
                    //create directory if it doesn't exist
                    /*if(!dir.exists()){
                        println("Creating dir for user ${user.userId}")
                        try {
                            dir.mkdir()
                            println("Dir created")
                        }catch (e:SecurityException){
                            println("You do not have permission to create a directory in this directory")
                        }
                    }*/
                it.streamProvider().use { its -> imageDir.outputStream().buffered().use {
                    its.copyTo(it)
                }}
                if(!postHm.containsKey("images"))
                    postHm.put("images", fileName)
                else
                    postHm.put("images",postHm["images"] +",$fileName" )
            }
        }
        return postHm
    }
}



fun String?.toImageList(): List<String>? {
    return when (this.isNullOrBlank()) {
        true -> null
        else -> ArrayList<String>(this
                ?.split(","))
                .map { c: String -> c.trim() }
                .toList()
    }
}