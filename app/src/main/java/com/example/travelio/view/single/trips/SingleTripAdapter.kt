package com.example.travelio.view.single.trips

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travelio.databinding.CustomTripImageBinding
import com.example.travelio.model.data.TripImages


class SingleTripAdapter (private val onzClickListener: OnClickListener) :
    ListAdapter<TripImages, SingleTripAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: CustomTripImageBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: TripImages) {
            binding.data = imgUrl
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<TripImages>() {
        override fun areItemsTheSame(oldItem: TripImages, newItem: TripImages): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: TripImages, newItem: TripImages): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(CustomTripImageBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imgUrl = getItem(position)
        holder.itemView.setOnClickListener {
            onzClickListener.onClick(imgUrl)
        }
        holder.bind(imgUrl)
    }

    class OnClickListener(val clickListener: (imgUrl: TripImages) -> Unit) {
        fun onClick(imgUrl: TripImages) = clickListener(imgUrl)
    }



}