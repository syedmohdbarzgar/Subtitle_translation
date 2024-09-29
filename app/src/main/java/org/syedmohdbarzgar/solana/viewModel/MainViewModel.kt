package org.syedmohdbarzgar.solana.viewModel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.syedmohdbarzgar.solana.database.MyDatabase
import org.syedmohdbarzgar.solana.model.SubtitleFile
import org.syedmohdbarzgar.solana.repository.SubtitleFileRepository
import java.io.File

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SubtitleFileRepository

    init {
        val database = MyDatabase.getInstance(application)
        val subtitleFileDao = database.subtitleFileDao
        repository = SubtitleFileRepository(subtitleFileDao)
    }

    // LiveData برای زیرنویس‌ها
    private val _subtitleFilesLiveData = MutableLiveData<MutableList<SubtitleFile>>()
    val subtitleFilesLiveData: LiveData<MutableList<SubtitleFile>> get() = _subtitleFilesLiveData

    // دریافت همه فایل‌های زیرنویس به صورت Flowable
    @SuppressLint("CheckResult")
    fun getAllSubtitleFiles() {
        repository.getAllSubtitleFiles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ subtitleFiles ->
                _subtitleFilesLiveData.postValue(subtitleFiles)
            }, { error ->
                // مدیریت خطا
            })
    }

    // حذف فایل زیرنویس
    @SuppressLint("CheckResult")
    fun deleteSubtitleFile(subtitleFile: SubtitleFile) {
        repository.delete(subtitleFile)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ /* موفقیت */

                val file: File = File(subtitleFile.filePath)
                file.delete()

            }, { error -> /* مدیریت خطا */
                println(error)
            })
    }
}