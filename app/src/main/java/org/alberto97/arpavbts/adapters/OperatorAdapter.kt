package org.alberto97.arpavbts.adapters

import android.content.res.ColorStateList
import android.view.ViewGroup
import androidx.core.widget.TextViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.alberto97.arpavbts.databinding.ListItemOperatorBinding
import org.alberto97.arpavbts.models.OperatorModel
import org.alberto97.arpavbts.tools.Extensions.getInflater


class OperatorAdapter(val onClick: (OperatorModel) -> Unit) :
    ListAdapter<OperatorModel, OperatorAdapter.ViewHolder>(OperatorDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ListItemOperatorBinding.inflate(parent.getInflater())
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int)  = holder.bind(getItem(position))

    inner class ViewHolder(private val binding: ListItemOperatorBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: OperatorModel) {
            binding.root.setOnClickListener { onClick(item) }
            TextViewCompat.setCompoundDrawableTintList(binding.label, ColorStateList.valueOf(item.color))
            binding.label.text = item.name
            binding.towers.text = item.towers.toString()
        }
    }
}

class OperatorDiff : DiffUtil.ItemCallback<OperatorModel>() {
    override fun areItemsTheSame(
        oldItem: OperatorModel,
        newItem: OperatorModel
    ): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(
        oldItem: OperatorModel,
        newItem: OperatorModel
    ): Boolean {
        return oldItem.name == newItem.name
    }

}