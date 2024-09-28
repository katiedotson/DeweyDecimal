package xyz.katiedotson.dewy.service.language

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    fun providesDatabase(
        @ApplicationContext context: Context
    ): DewyDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = DewyDatabase::class.java,
            name = "dewy-database"
        ).createFromAsset(databaseFilePath = "dewy-database.db").build()
    }

    @Provides
    fun providesLanguageDao(database: DewyDatabase): LanguageDao {
        return database.languageDao()
    }
}
