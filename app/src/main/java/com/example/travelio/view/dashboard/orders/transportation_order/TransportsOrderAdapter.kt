package com.example.travelio.view.dashboard.orders.transportation_order

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travelio.R
import com.example.travelio.databinding.CustomReserveTransportBinding
import com.example.travelio.model.data.TransportReserveData


class TransportsOrderAdapter (private val onzClickListener: OnClickListener,private val ondClickListener:OnDeleteClickListener) :
    ListAdapter<TransportReserveData, TransportsOrderAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder( var binding: CustomReserveTransportBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TransportReserveData) {
            binding.data = data
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TransportReserveData>() {
        override fun areItemsTheSame(oldItem: TransportReserveData, newItem: TransportReserveData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TransportReserveData, newItem: TransportReserveData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(CustomReserveTransportBinding.inflate(LayoutInflater.from(parent.context)))
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

    class OnClickListener(val clickListener: (data: TransportReserveData) -> Unit) {
        fun onClick(data: TransportReserveData) = clickListener(data)
    }

    class OnDeleteClickListener(val clickListener: (data: TransportReserveData) -> Unit) {
        fun onClick(data: TransportReserveData) = clickListener(data)
    }

}