package com.example.bezo.view.dashboard.hotel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bezo.R
import com.example.bezo.databinding.FragmentHotelBinding
import com.example.bezo.view.util.HotelFilterPopUp
import com.example.bezo.view.util.PopUpMsg


class HotelFragment : Fragment() {
    private lateinit var binding:FragmentHotelBinding
    private lateinit var viewModel:HotelViewModel
    private var  loadMore : Boolean = true
    private var isLoading = false
    private var isScrolling = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        //Initialization
        binding = FragmentHotelBinding.inflate(inflater,container,false)
        val application = requireNotNull(activity).application
        val collection = HotelFragmentArgs.fromBundle(requireArguments()).collection
        val viewModelFactory = HotelViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(HotelViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this

        //Adapter
        binding.hotelsRecycler.adapter = HotelAdapter(HotelAdapter.OnClickListener{
            this.findNavController().navigate(HotelFragmentDirections.actionHotelFragmentToSingleHotelFragment(it))
        })

        //AppBar_onClick
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.filter ->{
                    HotelFilterPopUp.handleHotelFilter(this.requireContext(),collection){ data ->

                    }
                    true
                }
                else -> false
            }

        }
        binding.topAppBar.setNavigationOnClickListener { this.requireActivity().onBackPressed()}

        //observer
        viewModel.error.observe(this.viewLifecycleOwner,{
            if(it != null){
                PopUpMsg.alertMsg(this.requireView(),it)
            }
        })


        viewModel.loadMore.observe(this.viewLifecycleOwner,{
            if(it != null){
                loadMore = it
            }
        })

        viewModel.noAuth.observe(this.viewLifecycleOwner,{
            if(it == true){
               PopUpMsg.showLoginAgainDialogue(this)
            }
        })



        //Pagination
        binding.hotelsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(loadMore){
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
                    val isNotAtBeginning = firstVisibleItemPosition >= 0
                    val isTotalMoreThanVisible = totalItemCount >= 5
                    val shouldPaginate =   isAtLastItem && isNotAtBeginning &&
                            isTotalMoreThanVisible && isScrolling && !isLoading
                    if(shouldPaginate) {
                        viewModel.callRequest()
                        isScrolling = false
                    }
                }
            }
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }
        })

        return  binding.root
    }
}