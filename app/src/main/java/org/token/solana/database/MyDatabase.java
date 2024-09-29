package org.token.solana.database;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import org.token.solana.model.SubtitleFile;

@Database(entities = {SubtitleFile.class}, version = 1, exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase myDatabase;

    // تعریف مهاجرت از نسخه 1 به 2
    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // دستور SQL برای اضافه کردن ستون جدید به جدول SubtitleFile
            database.execSQL("ALTER TABLE SubtitleFile ADD COLUMN name TEXT");
        }
    };

    public static MyDatabase getInstance(Application application) {
        if (myDatabase == null) {
            myDatabase = Room.databaseBuilder(application, MyDatabase.class, "main.db")
//                    .addMigrations(MIGRATION_2_3) // اضافه کردن مهاجرت
                    .build();
        }
        return myDatabase;
    }

    public abstract SubtitleFileDao getSubtitleFileDao();
}
