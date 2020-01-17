package org.alberto97.arpavbts.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.alberto97.arpavbts.databinding.ListItemBtsBinding
import org.alberto97.arpavbts.models.BTSDetailsAdapterItem
import org.alberto97.arpavbts.tools.Extensions.getInflater

class BTSAdapter : ListAdapter<BTSDetailsAdapterItem, BTSAdapter.ViewHolder>(BTSDetailsDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ListItemBtsBinding.inflate(parent.getInflater()))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ListItemBtsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BTSDetailsAdapterItem) = with(binding) {
            bts = item
        }
    }
}

private class BTSDetailsDiff : DiffUtil.ItemCallback<BTSDetailsAdapterItem>() {
    override fun areItemsTheSame(
        oldItem: BTSDetailsAdapterItem,
        newItem: BTSDetailsAdapterItem
    ): Boolean {
        return oldItem.text == newItem.text;
    }

    override fun areContentsTheSame(
        oldItem: BTSDetailsAdapterItem,
        newItem: BTSDetailsAdapterItem
    ): Boolean {
        return oldItem.text == newItem.text;
    }
}