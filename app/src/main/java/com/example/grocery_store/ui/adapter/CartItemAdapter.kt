package com.example.grocery_store.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery_store.core.Utils
import com.example.grocery_store.databinding.LayoutCartItemBinding
import com.example.grocery_store.domain.GroceryCartItem

class CartItemAdapter : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

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
            LayoutCartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
            addItem.setOnClickListener {
                onItemClickListener?.let {
                    it(item, CartItemType.ADD_ITEM_IN_CART)
                }
            }
            reduceItem.setOnClickListener {
                onItemClickListener?.let {
                    it(item, CartItemType.REDUCE_ITEM_IN_CART)
                }
            }
            cancelItem.setOnClickListener {
                onItemClickListener?.let {
                    it(item, CartItemType.CANCEL_ITEM)
                }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    inner class CartItemViewHolder(val binding: LayoutCartItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((GroceryCartItem, CartItemType) -> Unit)? = null

    fun setOnItemClickListener(listener: (GroceryCartItem, CartItemType) -> Unit) {
        onItemClickListener = listener
    }
}

enum class CartItemType {
    ADD_ITEM_IN_CART, REDUCE_ITEM_IN_CART, CANCEL_ITEM
}