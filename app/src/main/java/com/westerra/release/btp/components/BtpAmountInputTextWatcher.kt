package com.westerra.release.btp.components

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.westerra.release.extensions.getAmount
import java.lang.ref.WeakReference
import java.text.NumberFormat

interface BtpAmountInputInterface {
    fun doValidation()
}

class BtpAmountInputTextWatcher(
    editText: EditText?,
    private val amountInputInterface: BtpAmountInputInterface,
) : TextWatcher {
    private val editTextWeakReference: WeakReference<EditText> = WeakReference<EditText>(editText)

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        amountInputInterface.doValidation()
    }
    override fun afterTextChanged(editable: Editable) {
        val editText = editTextWeakReference.get() ?: return
        editText.removeTextChangedListener(this)
        val formatted = NumberFormat.getCurrencyInstance().format(editText.getAmount())
        editText.setText(formatted)
        editText.setSelection(formatted.length)
        editText.addTextChangedListener(this)
    }
}
