package xyz.katiedotson.dewy.language

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DatabaseLanguageModel::class], version = 1)
abstract class DewyDatabase : RoomDatabase() {
    abstract fun languageDao(): LanguageDao
}
