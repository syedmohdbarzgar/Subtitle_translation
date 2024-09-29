package org.token.solana.views.activities

import SubtitleAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import io.reactivex.rxjava3.disposables.CompositeDisposable
import org.token.solana.R
import org.token.solana.databinding.ActivitySubtitleEditingBinding
import org.token.solana.model.Subtitle
import org.token.solana.model.SubtitleFile
import org.token.solana.utils.SRTParser
import org.token.solana.views.dialogs.SubtitleEditingDialog

class SubtitleEditingActivity : AppCompatActivity(), (Subtitle) -> Unit,
    SubtitleEditingDialog.OnSubtitleEditedListener {
    private val TAG = "SubtitleEditingActivity"
    private lateinit var activitySubtitleEditingBinding: ActivitySubtitleEditingBinding
    private lateinit var subtitleFile: SubtitleFile
    private lateinit var subtitleAdapter: SubtitleAdapter
    private lateinit var srtParser: SRTParser
    private lateinit var subtitles: MutableList<Subtitle>
    private val compositeDisposable = CompositeDisposable() // Initialize CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activitySubtitleEditingBinding = ActivitySubtitleEditingBinding.inflate(layoutInflater)
        setContentView(activitySubtitleEditingBinding.root)

        ViewCompat.setOnApplyWindowInsetsListener(activitySubtitleEditingBinding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setup()
    }

    private fun setup() {
        setupIntentReceiver()
        setupToolbar()
        setupAdapter()
    }

    private fun setupIntentReceiver() {
        val subtitleFileString = intent.getStringExtra("subtitleFile")
        subtitleFile = GsonBuilder().create().fromJson(subtitleFileString, SubtitleFile::class.java)
    }

    @SuppressLint("CheckResult")
    private fun setupAdapter() {
        srtParser = SRTParser()
        val filePath = subtitleFile.filePath

        val disposable = srtParser.parseSRT(this, filePath)
            .subscribe({ subtitles ->
                this.subtitles = subtitles
                subtitleAdapter = SubtitleAdapter(subtitles, this)
                activitySubtitleEditingBinding.listSubtitle.layoutManager =
                    LinearLayoutManager(this)
                activitySubtitleEditingBinding.listSubtitle.adapter = subtitleAdapter
            }, { error ->
                error.printStackTrace()
            })

        compositeDisposable.add(disposable) // Add to CompositeDisposable
    }

    private fun setupToolbar() {
        setSupportActionBar(activitySubtitleEditingBinding.subtitleEditingToolbar)
        activitySubtitleEditingBinding.subtitleEditingToolbar.isTitleCentered = true
        activitySubtitleEditingBinding.subtitleEditingToolbar.title =
            getString(R.string.language_selection)
        activitySubtitleEditingBinding.subtitleEditingToolbar.setNavigationIcon(R.drawable.left_arrow)
        activitySubtitleEditingBinding.subtitleEditingToolbar.setNavigationOnClickListener {
            finish()
        }
    }

    override fun invoke(p1: Subtitle) {
        val dialog = SubtitleEditingDialog(p1)
        dialog.setOnSubtitleEditedListener(this)
        dialog.show(supportFragmentManager, "SubtitleEditingDialog")
    }

    override fun onSubtitleEdited(subtitle: Subtitle) {
        subtitleAdapter.updateSubtitle(subtitle)
    }

    @SuppressLint("CheckResult")
    private fun saveSubtitles(fileName: String) {

        val updatedSubtitles = subtitleAdapter.getSubtitles() // دریافت لیست به‌روز شده زیرنویس‌ها
        srtParser.convertSubtitlesToSRTRx(updatedSubtitles)
            .flatMap { srtContent ->

                srtParser.saveSubtitlesToFileRx(this, srtContent, fileName)
            }
            .subscribe({ file ->
                // زمانی که فایل ذخیره شد
                finish()
            }, { error ->
                error.printStackTrace()
                Log.e(TAG, "saveSubtitles: ", error)
                Toast.makeText(this, getString(R.string.there_was_an_error_saving_the_file), Toast.LENGTH_SHORT).show()
            })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.subtitle_editing_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveSubtitles(subtitleFile.name) // Use new method
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear() // Clear all subscriptions
    }
}
