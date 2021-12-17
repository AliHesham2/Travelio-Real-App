package com.example.travelio.view.single.trips

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.travelio.R
import com.example.travelio.databinding.FragmentSingleTripsBinding
import com.example.travelio.view.util.FullScreenImage
import com.example.travelio.view.util.PopUpMsg


class SingleTripsFragment : Fragment() {
    private lateinit var binding: FragmentSingleTripsBinding
    private lateinit var viewModel: SingleTripsViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Initialization
        binding = FragmentSingleTripsBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val receivedData = SingleTripsFragmentArgs.fromBundle(requireArguments()).tripData
        val viewModelFactory = SingleTripsViewModelFactory(receivedData,application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(SingleTripsViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this

        binding.recyclerViewImg.adapter = SingleTripAdapter(SingleTripAdapter.OnClickListener{
            val imageUrl = this.resources.getString(R.string.PHOTO_LINK) + it.imageUrl
            val fullScreenIntent = Intent(this.context, FullScreenImage::class.java)
            fullScreenIntent.data = imageUrl.toUri()
            this.context!!.startActivity(fullScreenIntent)
        })
        //observers
        viewModel.noImage.observe(this.viewLifecycleOwner,{
            if(it == true){
                binding.recyclerViewImg.visibility = View.GONE
                binding.textView3.visibility = View.VISIBLE
            }else{
                binding.textView3.visibility = View.GONE
                binding.recyclerViewImg.visibility = View.VISIBLE
            }
        })
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