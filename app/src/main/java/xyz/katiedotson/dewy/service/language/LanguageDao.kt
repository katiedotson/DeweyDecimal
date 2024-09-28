package xyz.katiedotson.dewy.service.language

import androidx.room.Dao
import androidx.room.Query

@Dao
interface LanguageDao {
    @Query("SELECT * FROM language")
    fun getAll(): List<DatabaseLanguageModel>
}
