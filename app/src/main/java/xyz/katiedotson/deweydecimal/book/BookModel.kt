package xyz.katiedotson.deweydecimal.book

data class BookModel(
    val title: String,
    val authors: List<AuthorModel>,
    val languages: List<LanguageModel>,
    val publishers: List<PublisherModel>,
    val isbns: List<IsbnModel>,
    val openLibraryKey: String,
    val publishedYears: List<Int>,
    val subjects: List<SubjectModel>,
    val ddcSort: String,
)

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
