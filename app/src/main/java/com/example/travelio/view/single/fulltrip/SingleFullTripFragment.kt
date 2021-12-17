package com.example.travelio.view.single.fulltrip

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.travelio.R
import com.example.travelio.databinding.FragmentSingleFullTripBinding
import com.example.travelio.view.util.FullScreenImage
import com.example.travelio.view.util.PopUpMsg


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
            val imageUrl = this.resources.getString(R.string.PHOTO_LINK) + it.imageUrl
            val fullScreenIntent = Intent(this.context, FullScreenImage::class.java)
            fullScreenIntent.data = imageUrl.toUri()
            this.context!!.startActivity(fullScreenIntent)
        })
        binding.recyclerViewHotel.adapter = SingleTripHotelAdapter(SingleTripHotelAdapter.OnClickListener{

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