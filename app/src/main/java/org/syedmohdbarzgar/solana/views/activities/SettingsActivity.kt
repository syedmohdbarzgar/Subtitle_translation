package org.syedmohdbarzgar.solana.views.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.syedmohdbarzgar.solana.R
import org.syedmohdbarzgar.solana.databinding.ActivitySettingsBinding
import org.syedmohdbarzgar.solana.utils.DataManager

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var dataManager: DataManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // تنظیم ViewBinding
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupToolbar()

        dataManager = DataManager(this)

        // تنظیم کلیک روی language_spinner
        binding.languageSpinner.setOnClickListener {
            selectLanguage()
        }

        // تنظیم حالت شب بر اساس ذخیره‌سازی
        setupDarkMode()

        // تنظیم زبان بر اساس ذخیره‌سازی
        setupLanguage()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.settingsToolbar)
        binding.settingsToolbar.isTitleCentered = true
        binding.settingsToolbar.title = getString(R.string.settings)
        binding.settingsToolbar.setNavigationIcon(R.drawable.left_arrow)
        binding.settingsToolbar.setNavigationOnClickListener {
            finish()
        }
    }
    // استفاده از ActivityResultLauncher برای دریافت نتیجه
    private val selectLanguageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // دریافت داده بازگشتی (زبان انتخاب شده)
                val selectedLanguageCode = result.data?.getStringExtra("LanguageCode") ?: "en"
                val selectedLanguageText = result.data?.getStringExtra("Language") ?: "English"

                // ذخیره زبان انتخاب شده با DataManager
                dataManager.putString("LanguageCode", selectedLanguageCode)
                dataManager.putString("Language", selectedLanguageText)

                // تنظیم زبان با AppCompatDelegate
                setAppLocale(selectedLanguageCode)

                // به‌روزرسانی UI برای نمایش زبان انتخاب شده
                binding.languageSpinner.text = selectedLanguageText
            }
        }

    private fun selectLanguage() {
        // ایجاد Intent برای انتقال به LanguagesActivity
        val intent = Intent(this, LanguagesActivity::class.java)
        selectLanguageLauncher.launch(intent) // شروع اکتیویتی و انتظار برای نتیجه
    }

    private fun setupLanguage() {
        val savedLanguageCode = dataManager.getString("LanguageCode") ?: "en"
        val savedLanguageText = dataManager.getString("Language") ?: "English"

        // تنظیم زبان با AppCompatDelegate
        setAppLocale(savedLanguageCode)

        // به‌روزرسانی UI برای نمایش زبان انتخاب شده
        binding.languageSpinner.text = savedLanguageText
    }

    private fun setupDarkMode() {
        val savedMode = dataManager.getInteger("night_mode")
        when (savedMode) {
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> binding.darkModeRadioGroup.check(R.id.system_mode)
            AppCompatDelegate.MODE_NIGHT_YES -> binding.darkModeRadioGroup.check(R.id.dark_mode)
            AppCompatDelegate.MODE_NIGHT_NO -> binding.darkModeRadioGroup.check(R.id.light_mode)
        }

        // ذخیره حالت شب پس از انتخاب کاربر
        binding.darkModeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.system_mode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    dataManager.putInteger("night_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
                R.id.dark_mode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    dataManager.putInteger("night_mode", AppCompatDelegate.MODE_NIGHT_YES)
                }
                R.id.light_mode -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    dataManager.putInteger("night_mode", AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    private fun setAppLocale(languageCode: String) {
        val locales = androidx.core.os.LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(locales)
    }
}