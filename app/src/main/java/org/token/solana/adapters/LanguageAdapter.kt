package org.token.solana.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.token.solana.R

class LanguageAdapter(
    private val languages: List<String>,
    private val onLanguageClick: (String) -> Unit
) : RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder>() {

    class LanguageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val languageName: TextView = itemView.findViewById(R.id.languageName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.language_item, parent, false)
        return LanguageViewHolder(view)
    }

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]
        holder.languageName.text = language

        holder.itemView.setOnClickListener {
            onLanguageClick(language)
        }
    }

    override fun getItemCount(): Int {
        return languages.size
    }
}
