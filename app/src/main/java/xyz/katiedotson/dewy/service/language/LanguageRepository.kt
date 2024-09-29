package xyz.katiedotson.dewy.service.language

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import xyz.katiedotson.dewy.IoDispatcher
import javax.inject.Inject

class LanguageRepository @Inject constructor(
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
    private val languageDao: LanguageDao
) {
    suspend fun getByKeys(languagesAbbr: List<String>): List<String> = withContext(dispatcher) {
        return@withContext languageDao
            .getByKeys(languagesAbbr)
            .map { it.displayName }
    }
}
