package com.example.bezo.view.dashboard.orders.full_trip_order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bezo.R
import com.example.bezo.databinding.CustomReserveFullTripBinding
import com.example.bezo.model.data.FullTripReserveData



class FullTripOrderAdapter  (private val onzClickListener: OnClickListener,private val ondClickListener: OnDeleteClickListener) :
    ListAdapter<FullTripReserveData, FullTripOrderAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(var binding: CustomReserveFullTripBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FullTripReserveData) {
            binding.data = data
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FullTripReserveData>() {
        override fun areItemsTheSame(oldItem: FullTripReserveData, newItem: FullTripReserveData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: FullTripReserveData, newItem: FullTripReserveData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(CustomReserveFullTripBinding.inflate(LayoutInflater.from(parent.context)))
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

    class OnClickListener(val clickListener: (data: FullTripReserveData) -> Unit) {
        fun onClick(data: FullTripReserveData) = clickListener(data)
    }

    class OnDeleteClickListener(val clickListener: (data: FullTripReserveData) -> Unit) {
        fun onClick(data: FullTripReserveData) = clickListener(data)
    }

}