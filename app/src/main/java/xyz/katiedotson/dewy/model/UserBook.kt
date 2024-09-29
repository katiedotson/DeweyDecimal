package xyz.katiedotson.dewy.model

data class UserBook(
    val userId: String,
    val title: String,
    val authors: List<String>,
    val languages: List<String>,
    val publishers: List<String>,
    val subjects: List<String>,
)
