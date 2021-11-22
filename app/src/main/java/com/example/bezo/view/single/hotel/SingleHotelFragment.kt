package com.example.bezo.view.single.hotel

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.databinding.FragmentSingleHotelBinding
import com.example.bezo.view.util.FullScreenImage
import com.example.bezo.view.util.PopUpMsg


class SingleHotelFragment : Fragment() {
    private lateinit var binding: FragmentSingleHotelBinding
    private lateinit var viewModel: SingleHotelViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Initialization
        binding = FragmentSingleHotelBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val receivedData = SingleHotelFragmentArgs.fromBundle(requireArguments()).hotelData
        val viewModelFactory = SingleHotelViewModelFactory(receivedData,application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(SingleHotelViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this

        binding.recyclerViewImg.adapter = SingleHotelAdapter(SingleHotelAdapter.OnClickListener{
            val fullScreenIntent = Intent(this.context, FullScreenImage::class.java)
            fullScreenIntent.data = it.imageUrl.toUri()
            this.startActivity(fullScreenIntent)
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



        return binding.root
    }

}