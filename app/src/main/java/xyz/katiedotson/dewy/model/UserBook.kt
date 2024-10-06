package xyz.katiedotson.dewy.model

data class UserBook(
    val key: String,
    val userId: String,
    val title: String,
    val authors: List<String>,
    val languages: List<String>,
    val publisher: String,
    val subjects: List<String>,
)
