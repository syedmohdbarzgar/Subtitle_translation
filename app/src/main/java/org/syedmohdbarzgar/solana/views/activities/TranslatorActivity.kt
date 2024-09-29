package org.syedmohdbarzgar.solana.views.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.syedmohdbarzgar.solana.Helper.TranslationHelper
import org.syedmohdbarzgar.solana.R
import org.syedmohdbarzgar.solana.databinding.ActivityTranslatorBinding
import org.syedmohdbarzgar.solana.model.Subtitle
import org.syedmohdbarzgar.solana.model.SubtitleFile
import org.syedmohdbarzgar.solana.utils.SRTParser
import org.syedmohdbarzgar.solana.viewModel.TranslatorViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID

class TranslatorActivity : AppCompatActivity() {
    private val TAG = "TranslatorActivity"
    private lateinit var binding: ActivityTranslatorBinding
    private lateinit var filePath: Uri
    private var sourceLanguageCode: String = ""
    private var targetLanguageCode: String = ""
    private val compositeDisposable = CompositeDisposable()
    private lateinit var parser: SRTParser
    private lateinit var translationHelper: TranslationHelper
    private lateinit var viewModel:TranslatorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ViewBinding setup
        binding = ActivityTranslatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this).get(TranslatorViewModel::class.java)
        setup()

    }

    private fun setup() {
        setupEdge()
        setupToolbar()
        setupSelectionFile()
        setupLanguageSelection()
        setupFileTranslation()
    }

    private fun setupFileTranslation() {
        binding.fileTranslation.root.visibility = View.INVISIBLE

        // تنظیم کلیک دکمه شروع ترجمه
        binding.fileTranslation.btnStartTranslation.setOnClickListener {
            binding.fileTranslation.btnStartTranslation.isEnabled = false
            binding.selectFile.btnSelectFile.isEnabled = false
            binding.languageSelection.spinnerSourceLanguage.isEnabled = false
            binding.languageSelection.spinnerTargetLanguage.isEnabled = false
            binding.languageSelection.btnTranslate.isEnabled = false
            binding.fileTranslation.translationTitle.text = "Reading the file..."
            binding.fileTranslation.progressTranslation.visibility = View.VISIBLE
            binding.fileTranslation.tvWarningMessage.visibility = View.VISIBLE
            binding.fileTranslation.tvProgressPercentage.visibility = View.VISIBLE
            startFileTranslation()
        }
    }

    private fun startFileTranslation() {
        parser = SRTParser()

        // پردازش فایل SRT با استفاده از SRTParser و RxAndroid
        val disposable = parser.parseSRT(this, filePath)
            .subscribe(
                { subtitles ->
                    // موفقیت: لیست زیرنویس‌ها را دریافت کرده‌اید
//                    subtitles.forEach { subtitle ->
//                        println("Index: ${subtitle.index}")
//                        println("Start: ${subtitle.startTime}, End: ${subtitle.endTime}")
//                        println("Text: ${subtitle.text}\n")
//                    }
                    binding.fileTranslation.translationTitle.text = "Translating..."
                    binding.fileTranslation.progressTranslation.max = subtitles.size
                    subtitleTranslation(subtitles, 0)
                },
                { error ->
                    binding.fileTranslation.tvWarningMessage.text =
                        "Error parsing SRT file: ${error.message}"
                }
            )

        // اضافه کردن disposable به compositeDisposable برای مدیریت حافظه
        compositeDisposable.add(disposable)
    }

    private fun subtitleTranslation(subtitles: List<Subtitle>, i: Int) {
// ایجاد یک نمونه از org.token.solana.Helper.TranslationHelper و تنظیم چرخه عمر
        translationHelper = TranslationHelper(this, this)

        // تنظیم زبان مبدا و مقصد (به عنوان مثال، انگلیسی به فارسی)
        translationHelper.createTranslator(sourceLanguageCode, targetLanguageCode)

        // دانلود مدل‌های زبان در صورت نیاز
        translationHelper.downloadModelIfNeeded(
            onSuccess = {
//                Toast.makeText(this, "مدل زبان با موفقیت دانلود شد", Toast.LENGTH_SHORT).show()

                // ترجمه متن پس از دانلود مدل
                translate(subtitles, i)
            },
            onFailure = { exception ->
                binding.fileTranslation.tvWarningMessage.text =
                    "Error in downloading the model: ${exception.message}"
            }
        )
    }

    @SuppressLint("SetTextI18n", "CheckResult")
    private fun translate(subtitles: List<Subtitle>, i: Int) {
        // اگر در حال انجام عملیات هستیم
        val p = ((i.toDouble() / (subtitles.size - 1).toDouble()) * 100).toInt()

        // تنظیم مقدار max و progress در ProgressBar
        binding.fileTranslation.progressTranslation.max = subtitles.size-1
        binding.fileTranslation.progressTranslation.progress = i

        // نمایش درصد در حال انجام
        binding.fileTranslation.tvProgressPercentage.text =
            getString(R.string.progress) + "  $p%"

        // ترجمه متن
        translationHelper.translateText(
            subtitles[i].text,
            onSuccess = { translatedText ->
                subtitles[i].text = translatedText
//                Toast.makeText(this, "Translation:$translatedText", Toast.LENGTH_LONG).show()

                if (i + 1 < subtitles.size)
                    translate(subtitles, i + 1)
                else {
                    // فراخوانی متد تبدیل با استفاده از RxAndroid
                    parser.convertSubtitlesToSRTRx(subtitles)
                        .observeOn(AndroidSchedulers.mainThread()) // مشاهده نتیجه در رشته اصلی (UI)
                        .subscribe(
                            { srtContent ->
                                // موفقیت: محتوای SRT را دریافت می‌کنید
                                Toast.makeText(
                                    this,
                                    "Conversion to SRT was successful",
                                    Toast.LENGTH_LONG
                                ).show()

                                // اکنون می‌توانید SRT را ذخیره یا نمایش دهید
                                parser.saveSubtitlesToFileRx(
                                    this,
                                    srtContent,
                                    UUID.randomUUID().toString()+".srt"
                                )
                                    .observeOn(AndroidSchedulers.mainThread()) // مشاهده نتیجه در رشته اصلی (UI)
                                    .subscribe(
                                        { file ->
                                            val subtitleFile = SubtitleFile(
                                                name = file.name,
                                                fileName = getFileNameFromUri(this@TranslatorActivity, filePath).toString(),
                                                fileType = file.extension, // یا هر نوع فایل دیگری که می‌خواهید
                                                fileSize = file.length(), // اندازه فایل فرضی
                                                filePath = file.path, // مسیر فرضی
                                                addedDate = SimpleDateFormat().format(Date()) // تاریخ فرضی
                                            )
                                            // فراخوانی متد insert
                                            viewModel.insertSubtitleFile(subtitleFile)

                                            binding.fileTranslation.btnStartTranslation.isEnabled =
                                                true
                                            binding.selectFile.btnSelectFile.isEnabled = true
                                            binding.languageSelection.spinnerSourceLanguage.isEnabled =
                                                true
                                            binding.languageSelection.spinnerTargetLanguage.isEnabled =
                                                true
                                            binding.languageSelection.btnTranslate.isEnabled = true
                                        }, { error ->
                                            binding.fileTranslation.tvWarningMessage.text =
                                                "Error in storage: ${error.message}"
                                        }
                                    )

                            },
                            { error ->
                                binding.fileTranslation.tvWarningMessage.text =
                                    "Error converting: ${error.message}"
                            }
                        )
                }
            },
            onFailure = { exception ->
                translate(subtitles, i)
                binding.fileTranslation.tvWarningMessage.text =
                    "Error in translation:${exception.message}"
            }
        )
    }

    fun getFileNameFromUri(context: Context, uri: Uri): String? {
        var fileName: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        fileName = it.getString(nameIndex)
                    }
                }
            }
        }
        if (fileName == null) {
            fileName = uri.path?.let { path ->
                val cut = path.lastIndexOf('/')
                if (cut != -1) path.substring(cut + 1) else path
            }
        }
        return fileName
    }


    private fun setupLanguageSelection() {
        binding.languageSelection.root.visibility = View.INVISIBLE
        // راه‌اندازی ActivityResultLauncher برای زبان مبدا
        val sourceLanguageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.let { data ->
                        val languageCode = data.getStringExtra("LanguageCode")
                        val languageName = data.getStringExtra("Language")
                        sourceLanguageCode = languageCode.toString()
                        binding.languageSelection.spinnerSourceLanguage.text =
                            languageName // نمایش نام زبان مبدا
                    }
                }
            }

        // راه‌اندازی ActivityResultLauncher برای زبان مقصد
        val targetLanguageResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.let { data ->
                        val languageCode = data.getStringExtra("LanguageCode")
                        val languageName = data.getStringExtra("Language")
                        targetLanguageCode = languageCode.toString()
                        binding.languageSelection.spinnerTargetLanguage.text =
                            languageName // نمایش نام زبان مقصد
                    }
                }
            }

        // تنظیم کلیک زبان مبدا
        binding.languageSelection.spinnerSourceLanguage.setOnClickListener {
            val intent = Intent(this, LanguagesActivity::class.java)
            sourceLanguageResultLauncher.launch(intent)
        }

        // تنظیم کلیک زبان مقصد
        binding.languageSelection.spinnerTargetLanguage.setOnClickListener {
            val intent = Intent(this, LanguagesActivity::class.java)
            targetLanguageResultLauncher.launch(intent)
        }

        binding.languageSelection.btnTranslate.setOnClickListener {
            if (sourceLanguageCode.isNotEmpty() && targetLanguageCode.isNotEmpty()) {
                binding.fileTranslation.root.visibility = View.VISIBLE

            } else {
                Toast.makeText(
                    this,
                    "Select the source and destination language.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun setupSelectionFile() {
        // عملکرد دکمه انتخاب فایل
        binding.selectFile.btnSelectFile.setOnClickListener {
            requestStoragePermissionAndOpenFileSelector()
        }

    }

    private fun requestStoragePermissionAndOpenFileSelector() {

        Dexter.withContext(this)
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    // اگر مجوز صادر شد، دیالوگ انتخاب فایل باز شود
                    openFileSelector()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    // نمایش پیغام به کاربر که مجوز نیاز است
                    Toast.makeText(
                        this@TranslatorActivity,
                        "Storage permission is required to select files.",
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest,
                    token: PermissionToken
                ) {
                    // اگر کاربر مجوز را رد کرد و نیاز به نمایش توضیحات بیشتر بود
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun openFileSelector() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*" // برای انتخاب هر نوع فایلی (محدودیت در مرحله بعد اعمال می‌شود)
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        filePickerLauncher.launch(Intent.createChooser(intent, "Select Subtitle File"))

    }

    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    val fileName = getFileName(uri)
                    if (fileName != null && (fileName.endsWith(".srt") || fileName.endsWith(".txt"))) {
                        binding.selectFile.tvSelectedFile.text = fileName
                        binding.languageSelection.root.visibility = View.VISIBLE

                        filePath = uri
                    } else {
                        Toast.makeText(
                            this,
                            "Only .srt or .txt files are supported",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }

    // دریافت نام فایل از Uri
    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                fileName =
                    cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            }
        }
        return fileName
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.translatorToolbar)
        binding.translatorToolbar.isTitleCentered = true
        binding.translatorToolbar.title = getString(R.string.new_project)
        binding.translatorToolbar.setNavigationIcon(R.drawable.left_arrow)
        binding.translatorToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun setupEdge() {
        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // پاکسازی disposable ها
        compositeDisposable.clear()
    }
}
