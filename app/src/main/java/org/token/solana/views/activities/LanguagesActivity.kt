package org.token.solana.views.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import org.token.solana.R
import org.token.solana.adapters.LanguageAdapter
import org.token.solana.databinding.ActivityLanguagesBinding

class LanguagesActivity : AppCompatActivity() {
    private lateinit var activityLanguagesBinding: ActivityLanguagesBinding

    private val languagesMap = mapOf(
        "Afrikaans" to "af",
        "العربية" to "ar",
        "Беларуская" to "be",
        "Български" to "bg",
        "বাংলা" to "bn",
        "Català" to "ca",
        "Čeština" to "cs",
        "Cymraeg" to "cy",
        "Dansk" to "da",
        "Deutsch" to "de",
        "Ελληνικά" to "el",
        "English" to "en",
        "Esperanto" to "eo",
        "Español" to "es",
        "Eesti" to "et",
        "فارسی" to "fa",
        "Suomi" to "fi",
        "Français" to "fr",
        "Gaeilge" to "ga",
        "Galego" to "gl",
        "ગુજરાતી" to "gu",
        "עברית" to "he",
        "हिन्दी" to "hi",
        "Hrvatski" to "hr",
        "Kreyòl Ayisyen" to "ht",
        "Magyar" to "hu",
        "Bahasa Indonesia" to "id",
        "Íslenska" to "is",
        "Italiano" to "it",
        "日本語" to "ja",
        "ქართული" to "ka",
        "ಕನ್ನಡ" to "kn",
        "한국어" to "ko",
        "Lietuvių" to "lt",
        "Latviešu" to "lv",
        "Македонски" to "mk",
        "मराठी" to "mr",
        "Bahasa Melayu" to "ms",
        "Malti" to "mt",
        "Nederlands" to "nl",
        "Norsk" to "no",
        "Polski" to "pl",
        "Português" to "pt",
        "Română" to "ro",
        "Русский" to "ru",
        "Slovenčina" to "sk",
        "Slovenščina" to "sl",
        "Shqip" to "sq",
        "Svenska" to "sv",
        "Kiswahili" to "sw",
        "தமிழ்" to "ta",
        "తెలుగు" to "te",
        "ไทย" to "th",
        "Tagalog" to "tl",
        "Türkçe" to "tr",
        "Українська" to "uk",
        "اردو" to "ur",
        "Tiếng Việt" to "vi",
        "中文" to "zh"
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        activityLanguagesBinding = ActivityLanguagesBinding.inflate(layoutInflater)
        setContentView(activityLanguagesBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(activityLanguagesBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()

        val languagesList = languagesMap.keys.toList()

        // تنظیم RecyclerView
        activityLanguagesBinding.recyclerViewLanguages.layoutManager = LinearLayoutManager(this)
        activityLanguagesBinding.recyclerViewLanguages.adapter = LanguageAdapter(languagesList) { selectedLanguageName ->
            // همزمان نام و کد زبان را دریافت کنید
            val selectedLanguage = languagesMap.entries.firstOrNull { it.key == selectedLanguageName }
            val selectedLanguageCode = selectedLanguage?.value ?: "en" // کد زبان
            val selectedLanguageText = selectedLanguage?.key ?: "English" // نام زبان
            Toast.makeText(this@LanguagesActivity, selectedLanguageText, Toast.LENGTH_SHORT).show()
//            val dataManager =DataManager(this)
//            dataManager.putString("LanguageCode", selectedLanguageCode)
//            dataManager.putString("Language", selectedLanguageText)
            // Prepare the result to send back to the calling activity
            val resultIntent = Intent().apply {
                putExtra("LanguageCode", selectedLanguageCode)
                putExtra("Language", selectedLanguageText)
            }

// Set the result with OK and pass back the Intent
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(activityLanguagesBinding.languageToolbar)
        activityLanguagesBinding.languageToolbar.isTitleCentered = true
        activityLanguagesBinding.languageToolbar.title = getString(R.string.language_selection)
        activityLanguagesBinding.languageToolbar.setNavigationIcon(R.drawable.left_arrow)
        activityLanguagesBinding.languageToolbar.setNavigationOnClickListener {
            finish()
        }
    }
}