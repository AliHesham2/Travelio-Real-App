package com.example.travelio.view.single.fulltrip

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travelio.databinding.CustomFullTripImageBinding
import com.example.travelio.model.data.FullTripImages


class SingleFullTripAdapter (private val onzClickListener: OnClickListener) :
    ListAdapter<FullTripImages, SingleFullTripAdapter.ViewHolder>(DiffCallback) {

    class ViewHolder(private var binding: CustomFullTripImageBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imgUrl: FullTripImages) {
            binding.data = imgUrl
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<FullTripImages>() {
        override fun areItemsTheSame(oldItem: FullTripImages, newItem: FullTripImages): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: FullTripImages, newItem: FullTripImages): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ViewHolder {
        return ViewHolder(CustomFullTripImageBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imgUrl = getItem(position)
        holder.itemView.setOnClickListener {
            onzClickListener.onClick(imgUrl)
        }
        holder.bind(imgUrl)
    }

    class OnClickListener(val clickListener: (imgUrl: FullTripImages) -> Unit) {
        fun onClick(imgUrl: FullTripImages) = clickListener(imgUrl)
    }



}