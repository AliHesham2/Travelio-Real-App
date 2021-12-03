package com.example.bezo.view.dashboard.orders.trips_order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bezo.R
import com.example.bezo.databinding.CustomReserveTripBinding
import com.example.bezo.model.data.TripReserveData


class TripsOrderAdapter (private val onzClickListener: OnClickListener,private val ondClickListener:OnDeleteClickListener) :
    ListAdapter<TripReserveData, TripsOrderAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(var binding: CustomReserveTripBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TripReserveData) {
            binding.data = data
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TripReserveData>() {
        override fun areItemsTheSame(oldItem: TripReserveData, newItem: TripReserveData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TripReserveData, newItem: TripReserveData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(CustomReserveTripBinding.inflate(LayoutInflater.from(parent.context)))
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

    class OnClickListener(val clickListener: (data: TripReserveData) -> Unit) {
        fun onClick(data: TripReserveData) = clickListener(data)
    }

    class OnDeleteClickListener(val clickListener: (data: TripReserveData) -> Unit) {
        fun onClick(data: TripReserveData) = clickListener(data)
    }

}