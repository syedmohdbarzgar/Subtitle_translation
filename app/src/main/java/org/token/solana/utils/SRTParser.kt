package org.token.solana.utils

import android.content.Context
import android.net.Uri
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import org.token.solana.model.Subtitle
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class SRTParser {
    fun parseSRT(context: Context, filePath: String): Single<MutableList<Subtitle>> {
        return Single.create<MutableList<Subtitle>> { emitter ->
            try {
                // استفاده از مسیر فایل به جای Uri
                val file = File(filePath)
                val reader = BufferedReader(InputStreamReader(file.inputStream()))
                val subtitles = mutableListOf<Subtitle>()
                val lines = reader.readLines()

                var index = 0
                var startTime = ""
                var endTime = ""
                var text = StringBuilder()

                for (line in lines) {
                    if (line.isBlank()) {
                        // ایجاد شیء Subtitle و اضافه کردن آن به لیست
                        subtitles.add(Subtitle(index, startTime, endTime, text.toString().trim()))
                        text = StringBuilder()
                    } else if (line.matches(Regex("\\d+"))) {
                        // شماره زیرنویس
                        index = line.toInt()
                    } else if (line.contains("-->")) {
                        // زمان شروع و پایان
                        val times = line.split(" --> ")
                        startTime = times[0].trim()
                        endTime = times[1].trim()
                    } else {
                        // متن زیرنویس
                        text.append(line).append("\n")
                    }
                }

                emitter.onSuccess(subtitles)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
            .subscribeOn(Schedulers.io())  // انجام عملیات در نخ ورودی/خروجی
            .observeOn(AndroidSchedulers.mainThread())  // مشاهده نتیجه در نخ اصلی (UI)
    }

    // متدی که فایل SRT را با استفاده از RxJava پردازش می‌کند
    fun parseSRT(context: Context, uri: Uri): Single<List<Subtitle>> {
        return Single.create<List<Subtitle>> { emitter ->
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val reader = BufferedReader(InputStreamReader(inputStream))
                val subtitles = mutableListOf<Subtitle>()
                val lines = reader.readLines()

                var index = 0
                var startTime = ""
                var endTime = ""
                var text = StringBuilder()

                for (line in lines) {
                    if (line.isBlank()) {
                        // ایجاد شیء Subtitle و اضافه کردن آن به لیست
                        subtitles.add(Subtitle(index, startTime, endTime, text.toString().trim()))
                        text = StringBuilder()
                    } else if (line.matches(Regex("\\d+"))) {
                        // شماره زیرنویس
                        index = line.toInt()
                    } else if (line.contains("-->")) {
                        // زمان شروع و پایان
                        val times = line.split(" --> ")
                        startTime = times[0].trim()
                        endTime = times[1].trim()
                    } else {
                        // متن زیرنویس
                        text.append(line).append("\n")
                    }
                }

                emitter.onSuccess(subtitles)
            } catch (e: Exception) {
                emitter.onError(e)
            }
        }
            .subscribeOn(Schedulers.io())  // انجام عملیات در نخ ورودی/خروجی
            .observeOn(AndroidSchedulers.mainThread())  // مشاهده نتیجه در نخ اصلی (UI)
    }

    fun convertSubtitlesToSRTRx(subtitles: List<Subtitle>): Single<String> {
        return Single.fromCallable {
            val stringBuilder = StringBuilder()

            subtitles.forEach { subtitle ->
                stringBuilder.append("${subtitle.index}\n")
                stringBuilder.append("${subtitle.startTime} --> ${subtitle.endTime}\n")
                stringBuilder.append("${subtitle.text}\n\n")
            }

            stringBuilder.toString() // بازگرداندن رشته SRT
        }.subscribeOn(Schedulers.computation()) // عملیات در رشته محاسباتی انجام می‌شود
    }


    fun saveSubtitlesToFileRx(
        context: Context,
        srtContent: String,
        fileName: String
    ): Single<File> {
        return Single.fromCallable {
            //
            val path = File(context.filesDir, "Subtitles")
            // محل ذخیره فایل (مثلاً داخل دایرکتوری فایل‌های داخلی)
            println(fileName)
            val file = File(path, fileName)

            // اگر فایل وجود دارد، آن را حذف کن
            if (file.exists()) {
                file.delete() // فایل را حذف می‌کند
            }

            // اگر دایرکتوری وجود ندارد، آن را ایجاد کنید
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }

            // باز کردن فایل با حالت رونویسی (append=false)
            val outputStream =
                FileOutputStream(file, true) // پارامتر false باعث می‌شود که فایل رونویسی شود

            // نوشتن محتوای SRT به داخل فایل
            OutputStreamWriter(outputStream).use { writer ->
                writer.write(srtContent)
            }

            outputStream.close()

            // بازگرداندن فایل ذخیره شده
            file
        }.subscribeOn(Schedulers.io()) // عملیات در رشته پس‌زمینه انجام می‌شود
    }


}
