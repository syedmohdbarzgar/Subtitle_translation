package org.token.solana.views.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.token.solana.databinding.DialogSubtitleEditingBinding
import org.token.solana.model.Subtitle

class SubtitleEditingDialog(private val subtitle: Subtitle) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogSubtitleEditingBinding
    private var listener: OnSubtitleEditedListener? = null

    interface OnSubtitleEditedListener {
        fun onSubtitleEdited(subtitle: Subtitle)
    }
    fun setOnSubtitleEditedListener(listener: OnSubtitleEditedListener){
        this.listener = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogSubtitleEditingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // نمایش اطلاعات زیرنویس در فیلدهای متنی
        binding.editTextStartTime.setText(subtitle.startTime)
        binding.editTextEndTime.setText(subtitle.endTime)
        binding.editTextSubtitleText.setText(subtitle.text)

        // تنظیم کلیک دکمه تایید
        binding.buttonSave.setOnClickListener {
            subtitle.apply {
                startTime = binding.editTextStartTime.text.toString()
                endTime = binding.editTextEndTime.text.toString()
                text = binding.editTextSubtitleText.text.toString()
            }
            listener?.onSubtitleEdited(subtitle)
            dismiss()
        }
    }


}
