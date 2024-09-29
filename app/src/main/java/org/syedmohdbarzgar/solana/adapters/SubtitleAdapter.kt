import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.syedmohdbarzgar.solana.databinding.ItemSubtitleBinding
import org.syedmohdbarzgar.solana.model.Subtitle

class SubtitleAdapter(
    private val subtitles: MutableList<Subtitle>,
    private val onEditClick: (Subtitle) -> Unit
) : RecyclerView.Adapter<SubtitleAdapter.SubtitleViewHolder>() {

    inner class SubtitleViewHolder(val binding: ItemSubtitleBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubtitleViewHolder {
        val binding = ItemSubtitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubtitleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SubtitleViewHolder, position: Int) {
        val subtitle = subtitles[position]
        with(holder.binding) {
            tvIndex.text = subtitle.index.toString()
            tvTime.text = "${subtitle.startTime} - ${subtitle.endTime}"
            tvText.text = subtitle.text

            // تنظیم listener برای دکمه ویرایش
            btnEdit.setOnClickListener {
                onEditClick(subtitle)
            }
        }
    }

    fun updateSubtitle(updatedSubtitle: Subtitle) {
        val index = subtitles.indexOfFirst { it.index == updatedSubtitle.index }
        if (index != -1) {
            subtitles[index] = updatedSubtitle
            notifyItemChanged(index)
        }
    }

    // بازگرداندن کپی از لیست زیرنویس‌ها
    fun getSubtitles(): List<Subtitle> {
        return subtitles.toList()
    }

    override fun getItemCount(): Int {
        return subtitles.size
    }
}
