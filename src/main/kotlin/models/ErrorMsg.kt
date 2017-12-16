package models

data class ErrorMsg(val msg: String,
                    val errorId: Int,
                    val docsLink: String? = null)