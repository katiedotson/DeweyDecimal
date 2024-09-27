package xyz.katiedotson.dewy.book

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
import javax.inject.Inject
import kotlin.coroutines.resume

class BookRepository @Inject constructor(
    private val bookApiService: BookApiService,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
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

    suspend fun saveBookResult(
        bookModel: BookModel
    ): Result<BookModel> = withContext(dispatcher) {
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
    ): Result<BookModel> = withContext(dispatcher) {
        return@withContext suspendCancellableCoroutine { continuation ->
            runCatching {
                Firebase.firestore.collection("identified_books")
                    .document(key)
                    .get()
                    .addOnSuccessListener { doc ->

                        val moshi: Moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
                        val bookAdapter: JsonAdapter<BookModel> = moshi.adapter<BookModel>()

                        continuation.resume(
                            Result.success(
                                bookAdapter.fromJson(
                                    JSONObject(doc.data!!).toString()
                                )!!
                            )
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
}
