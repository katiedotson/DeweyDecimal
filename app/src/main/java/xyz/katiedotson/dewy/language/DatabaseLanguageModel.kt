package xyz.katiedotson.dewy.language

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "language")
data class DatabaseLanguageModel(
    @PrimaryKey val key: String,
    val displayName: String
)
