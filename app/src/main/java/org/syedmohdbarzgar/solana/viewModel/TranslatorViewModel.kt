package org.syedmohdbarzgar.solana.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import org.syedmohdbarzgar.solana.database.MyDatabase
import org.syedmohdbarzgar.solana.model.SubtitleFile
import org.syedmohdbarzgar.solana.repository.SubtitleFileRepository

class TranslatorViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: SubtitleFileRepository

    init {
        val database = MyDatabase.getInstance(application)
        val subtitleFileDao = database.subtitleFileDao
        repository = SubtitleFileRepository(subtitleFileDao)
    }

    // LiveData برای زیرنویس‌ها
    private val _subtitleFilesLiveData = MutableLiveData<List<SubtitleFile>>()
    val subtitleFilesLiveData: LiveData<List<SubtitleFile>> get() = _subtitleFilesLiveData

    // افزودن فایل زیرنویس
    fun insertSubtitleFile(subtitleFile: SubtitleFile) {
        repository.insert(subtitleFile)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ /* موفقیت */ }, { error -> /* مدیریت خطا */
            println(error)
            })
    }

    // دریافت همه فایل‌های زیرنویس به صورت Flowable
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

    // سایر عملیات‌ها مثل update و delete مشابه خواهند بود
}

