package models

data class ErrorMsg(val msg: String,
                    val errorId: Int,
                    val docsLink: String? = null)

const val INVALID_POST_STRUCT = 5640
const val ERROR_DELETING_POST = 8974
const val INVALID_POSTID = 4186
const val INVALID_LOCATIONID = 1782
const val DUPLICATE_RECORDS_FOUND = 8451

class DuplicateFound(msg: String): Exception(msg)
class InvalidPostObject(msg: String) : Exception(msg)