package com.example.bezo.view.dashboard.single_order.hotel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.R
import com.example.bezo.databinding.FragmentSingleHotelOrderBinding
import com.example.bezo.view.single.hotel.SingleHotelAdapter
import com.example.bezo.view.util.FullScreenImage


class SingleHotelOrder : Fragment() {
    private lateinit var binding: FragmentSingleHotelOrderBinding
    private lateinit var viewModel: SingleHotelOrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Initialization
        binding = FragmentSingleHotelOrderBinding.inflate(inflater,container,false)
        val application = requireNotNull(activity).application
        val hotelData = SingleHotelOrderArgs.fromBundle(requireArguments()).hotelData
        val viewModelFactory = SingleHotelOrderViewModelFactory(application,hotelData)
        viewModel = ViewModelProvider(this,viewModelFactory).get(SingleHotelOrderViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this

        binding.recyclerViewImg.adapter = SingleHotelAdapter(SingleHotelAdapter.OnClickListener{
            val imageUrl = this.resources.getString(R.string.PHOTO_LINK) + it.imageUrl
            val fullScreenIntent = Intent(this.context, FullScreenImage::class.java)
            fullScreenIntent.data = imageUrl.toUri()
            this.startActivity(fullScreenIntent)
        })

        return  binding.root
    }

}