package xyz.katiedotson.dewy.app.bookinput

import xyz.katiedotson.dewy.model.key
import xyz.katiedotson.dewy.service.book.BookRepository
import xyz.katiedotson.dewy.service.language.LanguageRepository
import javax.inject.Inject

class GetBookInputModelUseCase @Inject constructor(
    private val bookRepository: BookRepository,
    private val languageRepository: LanguageRepository,
) {
    suspend operator fun invoke(key: String): Result<BookInputModel> {
        return bookRepository
            .getByKey(key)
            .mapCatching { result ->
                val languagesAbbr = result.languages.map { it.abbreviation }
                val displayLanguages = languageRepository.getByKeys(languagesAbbr)
                BookInputModel(
                    key = result.key,
                    title = result.title,
                    authors = result.authors.map { it.fullName },
                    languages = displayLanguages,
                    publishers = result.publishers.map { it.publisherName },
                    subjects = result.subjects.map { it.subjectName }
                )
            }
    }
}

data class BookInputModel(
    val key: String,
    val title: String,
    val authors: List<String>,
    val languages: List<String>,
    val publishers: List<String>,
    val subjects: List<String>,
)
