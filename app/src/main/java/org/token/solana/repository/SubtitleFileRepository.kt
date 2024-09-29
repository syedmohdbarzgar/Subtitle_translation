package org.token.solana.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import org.token.solana.model.SubtitleFile
import org.token.solana.database.SubtitleFileDao

class SubtitleFileRepository(private val subtitleFileDao: SubtitleFileDao) {

    fun insert(subtitleFile: SubtitleFile): Completable {
        return subtitleFileDao.insert(subtitleFile)
    }

    fun update(subtitleFile: SubtitleFile): Completable {
        return subtitleFileDao.update(subtitleFile)
    }

    fun delete(subtitleFile: SubtitleFile): Completable {
        return subtitleFileDao.delete(subtitleFile)
    }

    fun getSubtitleFileById(id: Int): Maybe<SubtitleFile> {
        return subtitleFileDao.getSubtitleFileById(id)
    }

    fun getAllSubtitleFiles(): Flowable<MutableList<SubtitleFile>> {
        return subtitleFileDao.getAllSubtitleFiles()
    }

    fun deleteAllSubtitleFiles(): Completable {
        return subtitleFileDao.deleteAllSubtitleFiles()
    }
}

