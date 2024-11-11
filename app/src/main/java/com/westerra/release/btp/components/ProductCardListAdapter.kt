package com.westerra.release.btp.components

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.westerra.release.btp.model.WesterraProduct
import com.westerra.release.databinding.CardWesterraProductBinding

class ProductCardListAdapter(private val cardInterface: ProductCardInterface) :
    ListAdapter<WesterraProduct, ProductCardViewHolder>(ProductCardDiffCallback()) {

    var isAccountEligible: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCardViewHolder {
        val binding = CardWesterraProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false,
        )
        return ProductCardViewHolder(
            binding = binding,
            cardInterface = cardInterface,
        )
    }

    override fun onBindViewHolder(holder: ProductCardViewHolder, position: Int) {
        holder.bind(getItem(position), isAccountEligible)
    }
}

private class ProductCardDiffCallback : DiffUtil.ItemCallback<WesterraProduct>() {

    override fun areItemsTheSame(oldItem: WesterraProduct, newItem: WesterraProduct): Boolean {
        return oldItem.areItemsSame(other = newItem)
    }

    override fun areContentsTheSame(oldItem: WesterraProduct, newItem: WesterraProduct): Boolean {
        return oldItem.areContentsSame(other = newItem)
    }
}
