package com.joshuadickey.findit

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.joshuadickey.findit.databinding.ItemSavedBinding

class ItemAdapter(private var items: List<SavedItem>, private val onDelete: (SavedItem) -> Unit) : RecyclerView.Adapter<ItemAdapter.Holder>() {
    class Holder(val binding: ItemSavedBinding) : RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(ItemSavedBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    override fun getItemCount() = items.size
    override fun onBindViewHolder(holder: Holder, position: Int) = with(holder.binding) {
        val item = items[position]; itemName.text = item.name; itemLocation.text = "📍 ${item.location}"; itemNote.text = item.note.ifBlank { "No extra details" }
        if (item.photoPath != null) { itemPhoto.setImageBitmap(BitmapFactory.decodeFile(item.photoPath)); itemPhoto.visibility = View.VISIBLE } else itemPhoto.visibility = View.INVISIBLE
        deleteButton.setOnClickListener { onDelete(item) }
    }
    fun update(newItems: List<SavedItem>) { items = newItems; notifyDataSetChanged() }
}
