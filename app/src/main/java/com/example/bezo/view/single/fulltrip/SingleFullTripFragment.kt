package com.example.bezo.view.single.fulltrip

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.databinding.FragmentSingleFullTripBinding
import com.example.bezo.view.util.FullScreenImage


class SingleFullTripFragment : Fragment() {
    private lateinit var binding: FragmentSingleFullTripBinding
    private lateinit var viewModel: SingleFullTripViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Initialization
        binding = FragmentSingleFullTripBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val receivedData = SingleFullTripFragmentArgs.fromBundle(requireArguments()).fullTripData
        val viewModelFactory = SingleFullTripViewModelFactory(receivedData,application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(SingleFullTripViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this

        //Adapter
        binding.recyclerViewImg.adapter = SingleFullTripAdapter(SingleFullTripAdapter.OnClickListener{
            val fullScreenIntent = Intent(this.context, FullScreenImage::class.java)
            fullScreenIntent.data = it.imageUrl.toUri()
            this.context!!.startActivity(fullScreenIntent)
        })

        //observers
        viewModel.noImage.observe(this.viewLifecycleOwner,{
            if(it == true){
                binding.recyclerViewImg.visibility = GONE
                binding.textView3.visibility = VISIBLE
            }else{
                binding.textView3.visibility = GONE
                binding.recyclerViewImg.visibility = VISIBLE
            }
        })

        viewModel.noHotel.observe(this.viewLifecycleOwner,{
            if(it == true){
                binding.recyclerViewHotel.visibility = GONE
                binding.textView4.visibility = VISIBLE
            }else{
                binding.textView4.visibility = GONE
                binding.recyclerViewHotel.visibility = VISIBLE
            }
        })

        binding.recyclerViewHotel.adapter = SingleTripHotelAdapter(SingleTripHotelAdapter.OnClickListener{

        })
        return binding.root
    }
}