package com.example.bezo.view.dashboard.orders.trips_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.bezo.R
import com.example.bezo.databinding.FragmentTripExpiredOrderBinding
import com.example.bezo.view.util.PopUpMsg


class TripExpiredOrder : Fragment() {
    private lateinit var binding: FragmentTripExpiredOrderBinding
    private lateinit var viewModel: TripExpiredOrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //initialization
        binding = FragmentTripExpiredOrderBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val viewModelFactory = TripExpiredOrderViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(TripExpiredOrderViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.data = viewModel

        //adapter
        binding.tripsRecycler.adapter = TripsOrderAdapter(TripsOrderAdapter.OnClickListener{
        requireActivity().findNavController(R.id.nav_fragment).navigate(TripOrderFragmentDirections.actionTripOrderFragmentDataToSingleTripOrder(it))
        },TripsOrderAdapter.OnDeleteClickListener{
            PopUpMsg.deleteAlertDialogue(this.requireContext(),this.resources){ done ->
                if(done){
                    viewModel.sendDeleteRequest(it)
                }
            }
        })

        return binding.root
    }
    object Instance{val instance : TripExpiredOrder by lazy { TripExpiredOrder() } }
}