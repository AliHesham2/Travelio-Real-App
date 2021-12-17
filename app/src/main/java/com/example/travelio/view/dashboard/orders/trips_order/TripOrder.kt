package com.example.travelio.view.dashboard.orders.trips_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.travelio.R
import com.example.travelio.databinding.FragmentTripOrderBinding
import com.example.travelio.view.util.PopUpMsg


class TripOrder : Fragment() {
    private lateinit var binding: FragmentTripOrderBinding
    private lateinit var viewModel: TripOrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //initialization
        binding = FragmentTripOrderBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val viewModelFactory = TripOrderViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(TripOrderViewModel::class.java)
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
    object Instance{val instance : TripOrder by lazy { TripOrder() } }
}
