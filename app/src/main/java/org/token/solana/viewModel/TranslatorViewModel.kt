package org.token.solana.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import org.token.solana.database.MyDatabase
import org.token.solana.model.SubtitleFile
import org.token.solana.database.SubtitleFileDatabase
import org.token.solana.repository.SubtitleFileRepository

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

