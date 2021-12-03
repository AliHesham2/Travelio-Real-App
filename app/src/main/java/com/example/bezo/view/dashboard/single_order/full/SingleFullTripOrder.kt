package com.example.bezo.view.dashboard.single_order.full

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.R
import com.example.bezo.databinding.FragmentSingleFullTripOrderBinding
import com.example.bezo.view.single.fulltrip.SingleFullTripAdapter
import com.example.bezo.view.single.fulltrip.SingleTripHotelAdapter
import com.example.bezo.view.util.FullScreenImage


class SingleFullTripOrder : Fragment() {
    private lateinit var binding: FragmentSingleFullTripOrderBinding
    private lateinit var viewModel: SingleFullTripOrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Initialization
        binding = FragmentSingleFullTripOrderBinding.inflate(inflater,container,false)
        val application = requireNotNull(activity).application
        val fullData = SingleFullTripOrderArgs.fromBundle(requireArguments()).fullTripData
        val viewModelFactory = SingleFullTripOrderViewModelFactory(application,fullData)
        viewModel = ViewModelProvider(this,viewModelFactory).get(SingleFullTripOrderViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this

        //Adapter
        binding.recyclerViewImg.adapter = SingleFullTripAdapter(SingleFullTripAdapter.OnClickListener{
            val imageUrl = this.resources.getString(R.string.PHOTO_LINK) + it.imageUrl
            val fullScreenIntent = Intent(this.context, FullScreenImage::class.java)
            fullScreenIntent.data = imageUrl.toUri()
            this.context!!.startActivity(fullScreenIntent)
        })

        binding.recyclerViewHotel.adapter = SingleTripHotelAdapter(SingleTripHotelAdapter.OnClickListener{

        })

        return  binding.root
    }

}