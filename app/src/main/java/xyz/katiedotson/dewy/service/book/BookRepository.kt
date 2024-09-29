package xyz.katiedotson.dewy.service.book

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import xyz.katiedotson.dewy.IoDispatcher
import xyz.katiedotson.dewy.model.AuthorResult
import xyz.katiedotson.dewy.model.BookSearchResult
import xyz.katiedotson.dewy.model.IsbnResult
import xyz.katiedotson.dewy.model.LanguageResult
import xyz.katiedotson.dewy.model.PublisherResult
import xyz.katiedotson.dewy.model.SubjectResult
import xyz.katiedotson.dewy.model.UserBook
import xyz.katiedotson.dewy.model.key
import javax.inject.Inject
import kotlin.coroutines.resume

class BookRepository @Inject constructor(
    private val bookApiService: BookApiService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    suspend fun getByIsbn(isbn: String): Result<BookSearchResult> = kotlin.runCatching {
        val response = bookApiService.getByIsbn(isbn)!!
        val firstMatch = response.docs.first()
        BookSearchResult(
            title = firstMatch.title,
            authors = firstMatch.authorName.map { AuthorResult(it) },
            languages = firstMatch.language.map {
                LanguageResult(it)
            },
            publishers = firstMatch.publisher.map { PublisherResult(it) },
            isbns = firstMatch.isbn.map { IsbnResult(it) },
            openLibraryKey = firstMatch.key,
            publishedYears = firstMatch.publishYear,
            subjects = firstMatch.subject.map { SubjectResult(it) },
            ddcSort = firstMatch.ddcSort
        )
    }

    suspend fun saveBookResult(
        bookModel: BookSearchResult
    ): Result<BookSearchResult> = withContext(dispatcher) {
        return@withContext suspendCancellableCoroutine { continuation ->
            runCatching {
                Firebase.firestore.collection("identified_books")
                    .document(bookModel.key)
                    .set(bookModel)
                    .addOnSuccessListener {
                        continuation.resume(
                            Result.success(bookModel)
                        )
                    }.addOnFailureListener { e ->
                        continuation.resume(
                            Result.failure(e)
                        )
                    }.addOnCanceledListener {
                        continuation.cancel(cause = null)
                    }
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun getByKey(
        key: String
    ): Result<BookSearchResult> = withContext(dispatcher) {
        return@withContext suspendCancellableCoroutine { continuation ->
            runCatching {
                Firebase.firestore.collection("identified_books")
                    .document(key)
                    .get()
                    .addOnSuccessListener { doc ->
                        val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                        val bookAdapter: JsonAdapter<BookSearchResult> = moshi.adapter<BookSearchResult>()
                        val model = bookAdapter.fromJson(
                            JSONObject(doc.data!!).toString()
                        )!!
                        continuation.resume(
                            Result.success(model)
                        )
                    }
                    .addOnFailureListener { e ->
                        continuation.resume(
                            Result.failure(e)
                        )
                    }
                    .addOnCanceledListener {
                        continuation.cancel(cause = null)
                    }
            }
        }
    }

    suspend fun saveBookForUser(userBook: UserBook): Result<String> = withContext(dispatcher) {
        return@withContext suspendCancellableCoroutine { continuation ->
            runCatching {
                Firebase.firestore.collection("user_books")
                    .add(userBook)
                    .addOnSuccessListener { result ->
                        continuation.resume(Result.success(result.id))
                    }
                    .addOnFailureListener { e ->
                        continuation.resume(Result.failure(e))
                    }
                    .addOnCanceledListener {
                        continuation.cancel(cause = null)
                    }
            }
        }
    }
}
