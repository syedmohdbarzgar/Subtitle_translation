package org.token.solana.Helper

import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.common.model.RemoteModelManager
import com.google.mlkit.nl.translate.TranslateRemoteModel
import android.content.Context
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.nl.translate.Translation

class TranslationHelper(private val context: Context, lifecycleOwner: LifecycleOwner) : LifecycleObserver {

    private var translator: Translator? = null

    init {
        // افزودن observer برای مدیریت چرخه عمر
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun createTranslator(sourceLang: String, targetLang: String) {
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.fromLanguageTag(sourceLang)!!)
            .setTargetLanguage(TranslateLanguage.fromLanguageTag(targetLang)!!)
            .build()

        translator = Translation.getClient(options)
    }

    fun downloadModelIfNeeded(onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val conditions = DownloadConditions.Builder()
//            .requireWifi() // استفاده از وای‌فای برای دانلود
            .build()

        translator?.downloadModelIfNeeded(conditions)
            ?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener { exception -> onFailure(exception) }
    }

    fun translateText(text: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        translator?.translate(text)
            ?.addOnSuccessListener { translatedText -> onSuccess(translatedText) }
            ?.addOnFailureListener { exception -> onFailure(exception) }
    }

    fun deleteModel(language: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val modelManager = RemoteModelManager.getInstance()
        val model = TranslateRemoteModel.Builder(TranslateLanguage.fromLanguageTag(language)!!).build()

        modelManager.deleteDownloadedModel(model)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    fun closeTranslator() {
        translator?.close()
        translator = null
    }
}
