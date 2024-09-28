package xyz.katiedotson.dewy.model

data class BookModel(
    val title: String,
    val authors: List<AuthorModel>,
    val languages: List<LanguageModel>,
    val publishers: List<PublisherModel>,
    val publishedYears: List<Int>,
    val subjects: List<SubjectModel>,
    val isbns: List<IsbnModel>,
    val openLibraryKey: String,
    val ddcSort: String,
)

val BookModel.key: String
    get() = this.openLibraryKey.replace(oldValue = "/works", newValue = "")

data class AuthorModel(
    val fullName: String,
)

data class LanguageModel(
    val abbreviation: String,
)

data class PublisherModel(
    val publisherName: String,
)

data class IsbnModel(
    val isbn: String
)

data class SubjectModel(
    val subjectName: String,
)
