package com.example.bezo.view.dashboard.hotel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bezo.databinding.CustomHotelCardViewBinding
import com.example.bezo.model.data.Hotel


class HotelAdapter (private val onzClickListener: OnClickListener) :
    ListAdapter<Hotel, HotelAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: CustomHotelCardViewBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(hotelInfo: Hotel) {
            binding.data = hotelInfo
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Hotel>() {
        override fun areItemsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Hotel, newItem: Hotel): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(CustomHotelCardViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hotelInfo = getItem(position)
        holder.itemView.setOnClickListener {
            onzClickListener.onClick(hotelInfo)
        }
        holder.bind(hotelInfo)
    }

    class OnClickListener(val clickListener: (hotelInfo: Hotel) -> Unit) {
        fun onClick(hotelInfo: Hotel) = clickListener(hotelInfo)
    }



}