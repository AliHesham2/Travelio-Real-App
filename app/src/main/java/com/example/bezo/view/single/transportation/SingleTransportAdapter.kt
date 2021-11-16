package com.example.bezo.view.single.transportation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bezo.databinding.CustomTransportImageBinding
import com.example.bezo.model.data.TransportationImages


class SingleTransportAdapter (private val onzClickListener: OnClickListener) :
    ListAdapter<TransportationImages, SingleTransportAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: CustomTransportImageBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: TransportationImages) {
            binding.data = imgUrl
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TransportationImages>() {
        override fun areItemsTheSame(oldItem: TransportationImages, newItem: TransportationImages): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TransportationImages, newItem: TransportationImages): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(CustomTransportImageBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imgUrl = getItem(position)
        holder.itemView.setOnClickListener {
            onzClickListener.onClick(imgUrl)
        }
        holder.bind(imgUrl)
    }

    class OnClickListener(val clickListener: (imgUrl: TransportationImages) -> Unit) {
        fun onClick(imgUrl: TransportationImages) = clickListener(imgUrl)
    }



}