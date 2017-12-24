package models

data class ErrorMsg(val msg: String,
                    val errorId: Int,
                    val docsLink: String? = null)

const val INVALID_POST_STRUCT = 5640
const val ERROR_DELETING_POST = 8974
const val INVALID_POSTID = 4186
const val INVALID_LOCATIONID = 1782
const val DUPLICATE_RECORDS_FOUND = 8451
const val FILE_SIZE_TOO_BIG = 654

class DuplicateFound(msg: String): Exception(msg)
class InvalidPostObject(msg: String) : Exception(msg)
class InvalidFileExtension(msg:String) : Exception(msg)
class FileSizeTooBig(msg:String) :Exception(msg)