package com.example.bezo.view.single.hotel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bezo.databinding.CustomHotelImageBinding
import com.example.bezo.model.data.Images

class SingleHotelAdapter (private val onzClickListener: OnClickListener) :
    ListAdapter<Images, SingleHotelAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: CustomHotelImageBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: Images) {
            binding.data = imgUrl
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Images>() {
        override fun areItemsTheSame(oldItem: Images, newItem: Images): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Images, newItem: Images): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(CustomHotelImageBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imgUrl = getItem(position)
        holder.itemView.setOnClickListener {
            onzClickListener.onClick(imgUrl)
        }
        holder.bind(imgUrl)
    }

    class OnClickListener(val clickListener: (imgUrl: Images) -> Unit) {
        fun onClick(imgUrl: Images) = clickListener(imgUrl)
    }



}