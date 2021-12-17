package com.example.travelio.view.dashboard.orders.hotel_order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travelio.R
import com.example.travelio.databinding.CustomReserveHotelBinding
import com.example.travelio.model.data.HotelReserveData


class HotelOrderAdapter (private val onzClickListener: OnClickListener,private val ondClickListener:OnDeleteClickListener) :
    ListAdapter<HotelReserveData, HotelOrderAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder( var binding: CustomReserveHotelBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: HotelReserveData) {
            binding.data = data
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<HotelReserveData>() {
        override fun areItemsTheSame(oldItem: HotelReserveData, newItem: HotelReserveData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: HotelReserveData, newItem: HotelReserveData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(CustomReserveHotelBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if(data.pivot.status != holder.itemView.resources.getString(R.string.PENDING)){
            holder.binding.deleteOrder.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            onzClickListener.onClick(data)
        }
        holder.binding.deleteOrder.setOnClickListener {
            ondClickListener.onClick(data)
        }
        holder.bind(data)
    }

    class OnClickListener(val clickListener: (data: HotelReserveData) -> Unit) {
        fun onClick(data: HotelReserveData) = clickListener(data)
    }

    class OnDeleteClickListener(val clickListener: (data: HotelReserveData) -> Unit) {
        fun onClick(data: HotelReserveData) = clickListener(data)
    }

}