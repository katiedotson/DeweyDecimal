package xyz.katiedotson.deweydecimal.book

import javax.inject.Inject
import kotlin.Result

class BookRepository @Inject constructor(private val bookApiService: BookApiService) {
    suspend fun getByIsbn(isbn: String): Result<BookModel> = kotlin.runCatching {
        val response = bookApiService.getByIsbn(isbn)!!
        val firstMatch = response.docs.first()
        BookModel(
            title = firstMatch.title,
            authors = firstMatch.authorName.map { AuthorModel(it) },
            languages = firstMatch.language.map { LanguageModel(it) },
            publishers = firstMatch.publisher.map { PublisherModel(it) },
            isbns = firstMatch.isbn.map { IsbnModel(it) },
            openLibraryKey = firstMatch.key,
            publishedYears = firstMatch.publishYear,
            subjects = firstMatch.subject.map { SubjectModel(it) },
            ddcSort = firstMatch.ddcSort
        )
    }
}
