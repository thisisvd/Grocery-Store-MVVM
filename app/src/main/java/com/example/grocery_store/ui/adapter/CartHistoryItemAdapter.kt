package com.example.grocery_store.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery_store.core.Utils
import com.example.grocery_store.databinding.LayoutCartHistoryItemBinding
import com.example.grocery_store.domain.GroceryCartItem

class CartHistoryItemAdapter : RecyclerView.Adapter<CartHistoryItemAdapter.CartItemViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<GroceryCartItem>() {
        override fun areItemsTheSame(
            oldItem: GroceryCartItem, newItem: GroceryCartItem
        ): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: GroceryCartItem, newItem: GroceryCartItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
        val binding =
            LayoutCartHistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {

        val item = differ.currentList[position]

        holder.binding.apply {
            itemImage.setImageBitmap(Utils.byteArrayToBitmap(item.itemImage))
            itemName.text = item.itemName
            itemDescription.text = item.itemDescription
            itemPrice.text = buildString {
                append("₹ ")
                append((item.itemPrice * item.itemCount))
            }
            itemCount.text = item.itemCount.toString()
            itemPriceCount.text = buildString {
                append(item.itemCount)
                append(" x ")
                append(item.itemPrice)
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    inner class CartItemViewHolder(val binding: LayoutCartHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}