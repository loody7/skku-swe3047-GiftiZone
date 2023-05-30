package edu.skku.cs.giftizone

import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class TagModal(
    private val context: Context,
    private val onConfirm: (String) -> Unit
) {
    private val inflater = LayoutInflater.from(context)
    private val dialogLayout = inflater.inflate(R.layout.dialog_layout, null)
    private val confirmTagBtn = dialogLayout.findViewById<Button>(R.id.confirmTagButton)
    private val cancelTagBtn = dialogLayout.findViewById<Button>(R.id.cancelTagButton)
    private val editTextTag = dialogLayout.findViewById<EditText>(R.id.editTextTag)

    private val tagDialog = AlertDialog.Builder(context).create()

    init {
        tagDialog.setView(dialogLayout)
        confirmTagBtn.setOnClickListener {
            val tag = editTextTag.text.toString()
            if (tag.isEmpty()) {
                Toast.makeText(context, "태그를 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            onConfirm(tag)
            tagDialog.dismiss()
        }
        cancelTagBtn.setOnClickListener {
            tagDialog.cancel()
        }
    }

    fun show() {
        tagDialog.show()
    }
}