package com.westerra.release.btp.components

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.westerra.release.R
import com.westerra.release.WesterraApplication
import com.westerra.release.btp.model.BtpProductItem
import com.westerra.release.databinding.ItemBtpAccountBinding
import java.text.NumberFormat

class BtpAccountCardViewHolder(
    private val binding: ItemBtpAccountBinding,
    private val cardInterface: BtpAccountCardInterface,
) : RecyclerView.ViewHolder(binding.root) {
    companion object {
        private val TAG = BtpAccountCardViewHolder::class.java.simpleName
    }

    fun bind(product: BtpProductItem) {
        with(binding) {
            executePendingBindings()
            Log.d(TAG, "binding to: $product")
            render(product = product)
        }
    }

    private fun render(product: BtpProductItem) {
        binding.nameText.text = product.displayName
        binding.accountNumberText.text = product.BBAN
        WesterraApplication.getInstance().getActivity() ?. let {
            binding.balanceTitleText.text = it.getString(R.string.available_balance)
            val formatted = NumberFormat.getCurrencyInstance().format(
                product.getAvailableBalanceAsFloat(),
            )
            binding.amountText.text = formatted
        }
        binding.layoutContents.setOnClickListener {
            cardInterface.onCardClicked(product = product)
        }
    }
}
