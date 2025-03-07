package com.example.grocery_store.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.grocery_store.core.Utils
import com.example.grocery_store.databinding.LayoutGroceryItemBinding
import com.example.grocery_store.domain.GroceryItem

class GroceryItemAdapter : RecyclerView.Adapter<GroceryItemAdapter.GroceryItemViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<GroceryItem>() {
        override fun areItemsTheSame(
            oldItem: GroceryItem, newItem: GroceryItem
        ): Boolean {
            return oldItem.itemId == newItem.itemId
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: GroceryItem, newItem: GroceryItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroceryItemViewHolder {
        val binding =
            LayoutGroceryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroceryItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroceryItemViewHolder, position: Int) {

        val item = differ.currentList[position]

        holder.binding.apply {
            itemImage.setImageBitmap(Utils.byteArrayToBitmap(item.itemImage))
            itemName.text = item.itemName
            itemDescription.text = item.itemDescription
            itemCount
            itemPrice.text = buildString {
                append("₹ ")
                append(item.itemPrice)
            }
            root.setOnClickListener {
                onItemClickListener?.let {
                    it(item)
                }
            }
        }
    }

    override fun getItemCount() = differ.currentList.size

    inner class GroceryItemViewHolder(val binding: LayoutGroceryItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((GroceryItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (GroceryItem) -> Unit) {
        onItemClickListener = listener
    }
}