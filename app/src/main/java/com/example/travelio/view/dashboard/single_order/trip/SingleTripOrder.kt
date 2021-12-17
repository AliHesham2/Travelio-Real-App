package com.example.travelio.view.dashboard.single_order.trip

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.travelio.R
import com.example.travelio.databinding.FragmentSingleTripOrderBinding
import com.example.travelio.view.single.trips.SingleTripAdapter
import com.example.travelio.view.util.FullScreenImage


class SingleTripOrder : Fragment() {
    private lateinit var binding: FragmentSingleTripOrderBinding
    private lateinit var viewModel: SingleTripOrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Initialization
        binding = FragmentSingleTripOrderBinding.inflate(inflater,container,false)
        val application = requireNotNull(activity).application
        val tripData = SingleTripOrderArgs.fromBundle(requireArguments()).tripData
        val viewModelFactory = SingleTripOrderViewModelFactory(application,tripData)
        viewModel = ViewModelProvider(this,viewModelFactory).get(SingleTripOrderViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this

        binding.recyclerViewImg.adapter = SingleTripAdapter(SingleTripAdapter.OnClickListener{
            val imageUrl = this.resources.getString(R.string.PHOTO_LINK) + it.imageUrl
            val fullScreenIntent = Intent(this.context, FullScreenImage::class.java)
            fullScreenIntent.data = imageUrl.toUri()
            this.context!!.startActivity(fullScreenIntent)
        })

        return  binding.root
    }

}