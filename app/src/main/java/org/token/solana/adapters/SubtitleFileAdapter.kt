package org.token.solana.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.token.solana.databinding.ItemSubtitleFileBinding
import org.token.solana.model.SubtitleFile

class SubtitleFileAdapter(
    private val subtitleFiles: List<SubtitleFile>,
    private val clickListener: OnSubtitleFileClickListener
) : RecyclerView.Adapter<SubtitleFileAdapter.SubtitleFileViewHolder>() {

    interface OnSubtitleFileClickListener {
        fun onSubtitleFileClick(subtitleFile: SubtitleFile)
    }

    // ViewHolder class برای نگهداری نمای آیتم‌ها با ViewBinding
    class SubtitleFileViewHolder(val binding: ItemSubtitleFileBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtitleFileViewHolder {
        // ایجاد ViewBinding برای هر آیتم
        val binding = ItemSubtitleFileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubtitleFileViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubtitleFileViewHolder, position: Int) {
        val subtitleFile = subtitleFiles[position]

        // استفاده از ViewBinding برای دسترسی به ویوها
        holder.binding.apply {
            textViewFileName.text = subtitleFile.fileName
            textViewFileInfo.text = "Type: ${subtitleFile.fileType}, Size: ${subtitleFile.fileSize} bytes"
            textViewAddedDate.text = "Added: ${subtitleFile.addedDate}"
            // تنظیم listener برای کلیک روی آیتم
            root.setOnClickListener {
                clickListener.onSubtitleFileClick(subtitleFile)
            }
        }
    }

    override fun getItemCount(): Int {
        return subtitleFiles.size
    }
}