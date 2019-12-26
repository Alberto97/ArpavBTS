package org.alberto97.arpavbts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.databinding.ListItemBtsBinding
import org.alberto97.arpavbts.models.BTSDetailsAdapterItem


class BTSAdapter(private val list: List<BTSDetailsAdapterItem>) :
    RecyclerView.Adapter<BTSAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_bts, parent, false
            )
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(list[position])

    inner class ViewHolder(private val binding: ListItemBtsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: BTSDetailsAdapterItem) = with(binding) {
            bts = item
        }
    }
}