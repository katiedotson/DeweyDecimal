package xyz.katiedotson.dewy.service.book

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface BookApiService {
    @GET("search.json")
    suspend fun getByIsbn(@Query("isbn") isbn: String): BookResult?
}

@Module
@InstallIn(SingletonComponent::class)
object BookApiServiceModule {

    @Provides
    fun provideBookApiService(): BookApiService {
        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
        return Retrofit
            .Builder()
            .baseUrl("https://openlibrary.org/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(BookApiService::class.java)
    }
}
