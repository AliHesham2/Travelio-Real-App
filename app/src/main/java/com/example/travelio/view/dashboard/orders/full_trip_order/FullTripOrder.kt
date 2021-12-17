package com.example.travelio.view.dashboard.orders.full_trip_order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.travelio.R
import com.example.travelio.databinding.FragmentFullTripOrderBinding
import com.example.travelio.view.util.PopUpMsg


class FullTripOrder : Fragment() {
    private lateinit var binding: FragmentFullTripOrderBinding
    private lateinit var viewModel: FullTripOrderViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //initialization
        binding = FragmentFullTripOrderBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val viewModelFactory = FullTripOrderViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(FullTripOrderViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.data = viewModel

        //adapter
        binding.fullTripRecycler.adapter = FullTripOrderAdapter(FullTripOrderAdapter.OnClickListener{
            requireActivity().findNavController(R.id.nav_fragment).navigate(FullTripOrderFragmentDirections.actionFullTripOrderFragmentDataToSingleFullTripOrder(it))
        },FullTripOrderAdapter.OnDeleteClickListener{
            PopUpMsg.deleteAlertDialogue(this.requireContext(),this.resources){ done ->
                if(done){
                    viewModel.sendDeleteRequest(it)
                }
            }
        })



        return binding.root
    }
    object Instance{val instance : FullTripOrder by lazy { FullTripOrder() } }
}