package xyz.katiedotson.dewy.model

data class BookSearchResult(
    val title: String,
    val authors: List<AuthorResult>,
    val languages: List<LanguageResult>,
    val publishers: List<PublisherResult>,
    val publishedYears: List<Int>,
    val subjects: List<SubjectResult>,
    val isbns: List<IsbnResult>,
    val openLibraryKey: String,
    val ddcSort: String,
)

// primary key
// Firebase won't allow "/" in identifiers
val BookSearchResult.key: String
    get() = this.openLibraryKey.replace(oldValue = "/works", newValue = "")

data class AuthorResult(
    val fullName: String,
)

data class LanguageResult(
    val abbreviation: String,
)

data class PublisherResult(
    val publisherName: String,
)

data class IsbnResult(
    val isbn: String
)

data class SubjectResult(
    val subjectName: String,
)
