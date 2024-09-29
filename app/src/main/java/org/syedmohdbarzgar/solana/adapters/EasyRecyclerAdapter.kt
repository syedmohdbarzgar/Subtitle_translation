package org.syedmohdbarzgar.tracking.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.syedmohdbarzgar.solana.R

abstract class EasyRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    fun setFinalize(view: View, end: Boolean) {
        val context: Context = view.context
        val marginBottom = if (end) {
            context.resources.getDimension(R.dimen.margin_bottom).toInt()
        } else {
            context.resources.getDimension(R.dimen.margin_small).toInt()
        }

        val params = view.layoutParams as? ViewGroup.MarginLayoutParams
        if (params != null) {
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, marginBottom)
            view.layoutParams = params
            view.requestLayout() // درخواست مجدد بارگذاری
        }
    }


    val colors: Array<Int> = arrayOf(
        0xFFEF9A9A.toInt(), 0xFFF48FB1.toInt(), 0xFFCE93D8.toInt(), 0xFFB39DDB.toInt(),
        0xFF9FA8DA.toInt(), 0xFF90CAF9.toInt(), 0xFF81D4FA.toInt(), 0xFF80DEEA.toInt(),
        0xFF80CBC4.toInt(), 0xFFA5D6A7.toInt(), 0xFFC5E1A5.toInt(), 0xFFE6EE9C.toInt(),
        0xFFFFF59D.toInt(), 0xFFFFE082.toInt(), 0xFFFFCC80.toInt(), 0xFFFFAB91.toInt()
    )

    abstract inner class AdHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
