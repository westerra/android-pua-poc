package com.westerra.release.btp.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.westerra.release.btp.model.BtpProductItem
import com.westerra.release.databinding.ItemBtpAccountBinding

class BtpAccountCardListAdapter(
    private val cardInterface: BtpAccountCardInterface,
) : ListAdapter<BtpProductItem, BtpAccountCardViewHolder>(
    BtpAccountCardDiffCallback(),
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BtpAccountCardViewHolder {
        return BtpAccountCardViewHolder(
            binding = ItemBtpAccountBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            cardInterface = cardInterface,
        )
    }

    override fun onBindViewHolder(holder: BtpAccountCardViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

private class BtpAccountCardDiffCallback : DiffUtil.ItemCallback<BtpProductItem>() {
    override fun areItemsTheSame(oldItem: BtpProductItem, newItem: BtpProductItem): Boolean {
        return oldItem.areItemsSame(other = newItem)
    }

    override fun areContentsTheSame(oldItem: BtpProductItem, newItem: BtpProductItem): Boolean {
        return oldItem.areContentsSame(other = newItem)
    }
}
