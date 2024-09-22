package xyz.katiedotson.deweydecimal.book

import kotlinx.serialization.Serializable

@Serializable
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

@Serializable
data class AuthorModel(
    val fullName: String,
)

@Serializable
data class LanguageModel(
    val abbreviation: String,
)

@Serializable
data class PublisherModel(
    val publisherName: String,
)

@Serializable
data class IsbnModel(
    val isbn: String
)

@Serializable
data class SubjectModel(
    val subjectName: String,
)
