package org.alberto97.arpavbts.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import org.alberto97.arpavbts.R
import org.alberto97.arpavbts.databinding.ListItemGestoreBinding
import org.alberto97.arpavbts.models.GestoreAdapterItem


class GestoreAdapter(private val list: ArrayList<GestoreAdapterItem>, val listener: (GestoreAdapterItem) -> Unit) :
    RecyclerView.Adapter<GestoreAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_gestore, parent, false
            )
        )

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  = holder.bind(list[position])

    inner class ViewHolder(private val binding: ListItemGestoreBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GestoreAdapterItem) = with(binding) {
            binding.gestore = item
            setClickListener { listener(item) }
        }
    }
}