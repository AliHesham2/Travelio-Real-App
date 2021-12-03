package com.example.bezo.view.single.transportation

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.R
import com.example.bezo.databinding.FragmentSingleTransportationBinding
import com.example.bezo.view.util.FullScreenImage
import com.example.bezo.view.util.PopUpMsg


class SingleTransportationFragment : Fragment() {
    private lateinit var binding: FragmentSingleTransportationBinding
    private lateinit var viewModel: SingleTransportationViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Initialization
        binding = FragmentSingleTransportationBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val receivedData = SingleTransportationFragmentArgs.fromBundle(requireArguments()).transportData
        val viewModelFactory = SingleTransportationViewModelFactory(receivedData,application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(SingleTransportationViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this

        binding.recyclerViewImg.adapter = SingleTransportAdapter(SingleTransportAdapter.OnClickListener{
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