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
import com.example.bezo.view.util.PopUpMsg


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
        viewModel.loading.observe(this.viewLifecycleOwner,{
            if(it == true){
                PopUpMsg.showDialogue(this.requireContext())
            }else{
                PopUpMsg.hideDialogue()
            }
        })

        viewModel.error.observe(this.viewLifecycleOwner,{
            if(it != null){
                PopUpMsg.alertMsg(this.requireView(),it)
            }
        })


        viewModel.noAuth.observe(this.viewLifecycleOwner,{
            if(it == true){
                PopUpMsg.showLoginAgainDialogue(this)
            }
        })


        binding.recyclerViewHotel.adapter = SingleTripHotelAdapter(SingleTripHotelAdapter.OnClickListener{

        })
        return binding.root
    }
}