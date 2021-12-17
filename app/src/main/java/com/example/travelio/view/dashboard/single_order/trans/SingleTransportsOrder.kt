package com.example.travelio.view.dashboard.single_order.trans

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.example.travelio.R
import com.example.travelio.databinding.FragmentSingleTransportsOrderBinding
import com.example.travelio.view.single.transportation.SingleTransportAdapter
import com.example.travelio.view.util.FullScreenImage


class SingleTransportsOrder : Fragment() {
    private lateinit var binding: FragmentSingleTransportsOrderBinding
    private lateinit var viewModel: SingleTransportsOrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Initialization
        binding = FragmentSingleTransportsOrderBinding.inflate(inflater,container,false)
        val application = requireNotNull(activity).application
        val transportData = SingleTransportsOrderArgs.fromBundle(requireArguments()).transportsData
        val viewModelFactory = SingleTransportsOrderViewModelFactory(application,transportData)
        viewModel = ViewModelProvider(this,viewModelFactory).get(SingleTransportsOrderViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this

        binding.recyclerViewImg.adapter = SingleTransportAdapter(SingleTransportAdapter.OnClickListener{
            val imageUrl = this.resources.getString(R.string.PHOTO_LINK) + it.imageUrl
            val fullScreenIntent = Intent(this.context, FullScreenImage::class.java)
            fullScreenIntent.data = imageUrl.toUri()
            this.context!!.startActivity(fullScreenIntent)
        })

        return  binding.root
    }

}