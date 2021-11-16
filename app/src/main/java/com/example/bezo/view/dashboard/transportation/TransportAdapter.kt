package com.example.bezo.view.dashboard.transportation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bezo.databinding.CustomHotelCardViewBinding
import com.example.bezo.databinding.CustomTransportsCardViewBinding
import com.example.bezo.model.data.Transportation


class TransportAdapter (private val onzClickListener: OnClickListener) :
    ListAdapter<Transportation, TransportAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: CustomTransportsCardViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(transportInfo: Transportation) {
            binding.data = transportInfo
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Transportation>() {
        override fun areItemsTheSame(oldItem: Transportation, newItem: Transportation): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Transportation, newItem: Transportation): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(CustomTransportsCardViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transportInfo = getItem(position)
        holder.itemView.setOnClickListener {
            onzClickListener.onClick(transportInfo)
        }
        holder.bind(transportInfo)
    }

    class OnClickListener(val clickListener: (transportInfo: Transportation) -> Unit) {
        fun onClick(transportInfo: Transportation) = clickListener(transportInfo)
    }

}