package com.example.travelio.view.dashboard.orders.hotel_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.travelio.databinding.FragmentHotelOrderBinding
import com.example.travelio.view.util.PopUpMsg


class HotelOrder : Fragment() {
    private lateinit var binding: FragmentHotelOrderBinding
    private lateinit var viewModel: HotelOrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //initialization
        binding = FragmentHotelOrderBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val viewModelFactory = HotelOrderViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(HotelOrderViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.data = viewModel

        //adapter
        binding.hotelsRecycler.adapter = HotelOrderAdapter(HotelOrderAdapter.OnClickListener{
        this.findNavController().navigate(HotelOrderDirections.actionHotelOrderDataToSingleHotelOrder2(it))
        },HotelOrderAdapter.OnDeleteClickListener{
            PopUpMsg.deleteAlertDialogue(this.requireContext(),this.resources){ done ->
                if(done){
                    viewModel.sendDeleteRequest(it)
                }
            }
        })

        //observers
        viewModel.noAuth.observe(this.viewLifecycleOwner,{
            if(it == true){
                PopUpMsg.showLoginAgainDialogue(this)
            }
        })

        return binding.root
    }
    object Instance{val instance : HotelOrder by lazy { HotelOrder() } }
}