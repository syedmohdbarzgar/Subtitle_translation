package org.token.solana.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.token.solana.model.Subtitle
import org.token.solana.model.SubtitleFile

@Database(entities = [SubtitleFile::class], version = 1, exportSchema = false)
abstract class SubtitleFileDatabase : RoomDatabase() {

    abstract fun subtitleFileDao(): SubtitleFileDao

    companion object {
        @Volatile
        private var INSTANCE: SubtitleFileDatabase? = null

        fun getDatabase(context: Context): SubtitleFileDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SubtitleFileDatabase::class.java,
                    "main.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
