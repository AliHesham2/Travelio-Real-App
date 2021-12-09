package com.example.bezo.view.dashboard.trips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bezo.databinding.CustomTripsCardViewBinding
import com.example.bezo.model.data.Trip


class TripAdapter (private val onzClickListener: OnClickListener) :
    ListAdapter<Trip, TripAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: CustomTripsCardViewBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tripInfo: Trip) {
            binding.data = tripInfo
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Trip>() {
        override fun areItemsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Trip, newItem: Trip): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(CustomTripsCardViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tripInfo = getItem(position)
        holder.itemView.setOnClickListener {
            onzClickListener.onClick(tripInfo)
        }
        holder.bind(tripInfo)
    }

    class OnClickListener(val clickListener: (tripInfo: Trip) -> Unit) {
        fun onClick(tripInfo: Trip) = clickListener(tripInfo)
    }



}