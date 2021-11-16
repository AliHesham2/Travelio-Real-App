package com.example.bezo.view.dashboard.fulltrip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bezo.databinding.CustomFullTripCardViewBinding
import com.example.bezo.databinding.CustomTripsCardViewBinding
import com.example.bezo.model.data.FullTrip


class FullTripAdapter  (private val onzClickListener: OnClickListener) :
    ListAdapter<FullTrip, FullTripAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: CustomFullTripCardViewBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(tripInfo: FullTrip) {
            binding.data = tripInfo
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FullTrip>() {
        override fun areItemsTheSame(oldItem: FullTrip, newItem: FullTrip): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: FullTrip, newItem: FullTrip): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(CustomFullTripCardViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tripInfo = getItem(position)
        holder.itemView.setOnClickListener {
            onzClickListener.onClick(tripInfo)
        }
        holder.bind(tripInfo)
    }

    class OnClickListener(val clickListener: (tripInfo: FullTrip) -> Unit) {
        fun onClick(tripInfo: FullTrip) = clickListener(tripInfo)
    }



}