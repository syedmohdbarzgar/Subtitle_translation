package org.token.solana.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import org.token.solana.model.SubtitleFile

@Dao
interface SubtitleFileDao {

    @Insert
    fun insert(subtitleFile: SubtitleFile): Completable

    @Update
    fun update(subtitleFile: SubtitleFile): Completable

    @Delete
    fun delete(subtitleFile: SubtitleFile): Completable

    @Query("SELECT * FROM subtitle_files WHERE id = :id")
    fun getSubtitleFileById(id: Int): Maybe<SubtitleFile>

    @Query("SELECT * FROM subtitle_files")
    fun getAllSubtitleFiles(): Flowable<MutableList<SubtitleFile>>

    @Query("DELETE FROM subtitle_files")
    fun deleteAllSubtitleFiles(): Completable
}


