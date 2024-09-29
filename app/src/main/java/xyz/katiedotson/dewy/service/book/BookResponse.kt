package xyz.katiedotson.dewy.service.book

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BookResponse(
    val numFound: Int,
    val docs: List<Document>
)

@JsonClass(generateAdapter = true)
data class Document(
    @Json(name = "author_name") val authorName: List<String>,
    val isbn: List<String>,
    val key: String,
    val language: List<String>,
    @Json(name = "publish_year") val publishYear: List<Int>,
    val publisher: List<String>,
    val title: String,
    val subject: List<String>,
    @Json(name = "ddc_sort") val ddcSort: String,
)
