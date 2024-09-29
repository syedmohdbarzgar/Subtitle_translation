package org.token.solana

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.token.solana.utils.DataManager

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val dataManager = DataManager(this)

        // بازیابی حالت شب ذخیره‌شده
        val savedNightMode = dataManager.getInteger("night_mode")
        if (savedNightMode == 0) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        else AppCompatDelegate.setDefaultNightMode(savedNightMode)

        // تنظیم زبان
        val savedLanguageCode = dataManager.getString("LanguageCode") ?: "en"
        setAppLocale(savedLanguageCode)
    }

    private fun setAppLocale(languageCode: String) {
        val locales = androidx.core.os.LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }
}