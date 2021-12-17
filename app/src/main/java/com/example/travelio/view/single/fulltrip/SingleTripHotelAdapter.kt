package com.example.travelio.view.single.fulltrip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travelio.databinding.CustomHotelFullCardViewBinding
import com.example.travelio.model.data.FullTripHotelData


class SingleTripHotelAdapter(private val onzClickListener: OnClickListener) :
    ListAdapter<FullTripHotelData, SingleTripHotelAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: CustomHotelFullCardViewBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(fullTripHotelData: FullTripHotelData) {
            binding.data = fullTripHotelData
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FullTripHotelData>() {
        override fun areItemsTheSame(oldItem: FullTripHotelData, newItem: FullTripHotelData): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: FullTripHotelData, newItem: FullTripHotelData): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(CustomHotelFullCardViewBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fullTripHotelData = getItem(position)
        holder.itemView.setOnClickListener {
            onzClickListener.onClick(fullTripHotelData)
        }
        holder.bind(fullTripHotelData)
    }

    class OnClickListener(val clickListener: (fullTripHotelData: FullTripHotelData) -> Unit) {
        fun onClick(fullTripHotelData: FullTripHotelData) = clickListener(fullTripHotelData)
    }



}