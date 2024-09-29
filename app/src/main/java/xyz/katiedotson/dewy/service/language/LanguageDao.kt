package xyz.katiedotson.dewy.service.language

import androidx.room.Dao
import androidx.room.Query

@Dao
interface LanguageDao {
    @Query("SELECT * FROM language")
    fun getAll(): List<DatabaseLanguageModel>

    @Query("SELECT * FROM language WHERE `key` IN (:keys)")
    fun getByKeys(keys: List<String>): List<DatabaseLanguageModel>
}
