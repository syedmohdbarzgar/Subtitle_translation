package org.syedmohdbarzgar.solana.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Keep
@Entity(tableName = "subtitle_files")
data class SubtitleFile(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,             // شناسه منحصر به فرد
    @Expose
    @SerializedName("name")
    val name: String,
    @Expose
    @SerializedName("file_name")
    val fileName: String,         // نام فایل
    @Expose
    @SerializedName("file_type")
    val fileType: String,         // نوع فایل (.srt, .vtt)
    @Expose
    @SerializedName("file_size")
    val fileSize: Long,           // اندازه فایل به بایت
    @Expose
    @SerializedName("file_path")
    val filePath: String,         // مسیر ذخیره سازی
    @Expose
    @SerializedName("added_date")
    val addedDate: String         // تاریخ اضافه شدن فایل
)

