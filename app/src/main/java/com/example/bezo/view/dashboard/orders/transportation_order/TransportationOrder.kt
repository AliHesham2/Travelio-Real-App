package com.example.bezo.view.dashboard.orders.transportation_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.bezo.R
import com.example.bezo.databinding.FragmentTransportationOrderBinding
import com.example.bezo.view.util.PopUpMsg


class TransportationOrder : Fragment() {
    private lateinit var binding: FragmentTransportationOrderBinding
    private lateinit var viewModel: TransportationOrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //initialization
        binding = FragmentTransportationOrderBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val viewModelFactory = TransportationOrderViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(TransportationOrderViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.data = viewModel

        //adapter
        binding.transportsRecycler.adapter = TransportsOrderAdapter(TransportsOrderAdapter.OnClickListener{
           requireActivity().findNavController(R.id.nav_fragment).navigate(TransportationOrderFragmentDirections.actionTransportationOrderFragmentDataToSingleTransportsOrder(it))
        },TransportsOrderAdapter.OnDeleteClickListener{
            PopUpMsg.deleteAlertDialogue(this.requireContext(),this.resources){ done ->
                if(done){
                    viewModel.sendDeleteRequest(it)
                }
            }
        })



        return binding.root
    }
    object Instance{val instance : TransportationOrder by lazy { TransportationOrder() } }
}