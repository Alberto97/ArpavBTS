package org.alberto97.arpavbts.adapters

import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.alberto97.arpavbts.databinding.ListItemGestoreBinding
import org.alberto97.arpavbts.models.GestoreAdapterItem
import org.alberto97.arpavbts.tools.Extensions.getInflater


class GestoreAdapter(val listener: (GestoreAdapterItem) -> Unit) :
    ListAdapter<GestoreAdapterItem, GestoreAdapter.ViewHolder>(GestoreDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemGestoreBinding.inflate(parent.getInflater())
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  = holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ListItemGestoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GestoreAdapterItem) {
            binding.root.setOnClickListener { listener(item) }
            TextViewCompat.setCompoundDrawableTintList(binding.label, ColorStateList.valueOf(item.color))
            binding.label.text = item.name
        }
    }
}

class GestoreDiff : DiffUtil.ItemCallback<GestoreAdapterItem>() {
    override fun areItemsTheSame(
        oldItem: GestoreAdapterItem,
        newItem: GestoreAdapterItem
    ): Boolean {
        return oldItem.name == newItem.name && oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: GestoreAdapterItem,
        newItem: GestoreAdapterItem
    ): Boolean {
        return oldItem.name == newItem.name && oldItem.id == newItem.id
    }

}