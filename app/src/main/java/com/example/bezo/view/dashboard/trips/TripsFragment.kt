package com.example.bezo.view.dashboard.trips

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
import com.example.bezo.databinding.FragmentTripsBinding
import com.example.bezo.model.data.TripFilterCollection
import com.example.bezo.view.util.PopUpMsg
import com.example.bezo.view.util.TripFilterPopUp


class TripsFragment : Fragment() {
    private lateinit var binding : FragmentTripsBinding
    private lateinit var viewModel: TripsViewModel
    private var  loadMore : Boolean = true
    private var  swipeRefresh : Boolean = true
    private var isLoading = false
    private var isScrolling = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripsBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val collection = TripsFragmentArgs.fromBundle(requireArguments()).collection
        var filterData : TripFilterCollection? = null
        val viewModelFactory = TripsViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(TripsViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //Adapter
        binding.tripsRecycler.adapter = TripAdapter(TripAdapter.OnClickListener{
            this.findNavController().navigate(TripsFragmentDirections.actionTripsFragmentToSingleTripsFragment(it))
        })

        //AppBar_onClick
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.filter ->{
                    TripFilterPopUp.handleTripFilter(this.requireContext(),collection,filterData){ data ->
                        viewModel.saveFilterData(data)
                        viewModel.resetData()
                        viewModel.callRequest(data.city_id,data.location_id,data.minPrice,data.maxPrice,data.fromDate,data.toDate)
                    }
                    true
                }
                else -> false
            }

        }

        binding.topAppBar.setNavigationOnClickListener { this.requireActivity().onBackPressed() }

        //observer
        viewModel.error.observe(this.viewLifecycleOwner,{
            if(it != null){
                PopUpMsg.alertMsg(this.requireView(),it)
            }
        })
        viewModel.filterData.observe(this.viewLifecycleOwner,{
            if(it != null){
                filterData = it
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
        viewModel.swipeLoading.observe(this.viewLifecycleOwner,{
            if (it != null){
                swipeRefresh = it
                swipeRefreshToggle()
            }
        })

        //onSwipeAction
        binding.swipeLoad.setOnRefreshListener {
            viewModel.swipeLoadHandle()
            binding.swipeLoad.isRefreshing = swipeRefresh
        }

        //Pagination
        binding.tripsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
                        if(filterData != null){
                            viewModel.callRequest(filterData!!.city_id, filterData!!.location_id, filterData!!.minPrice, filterData!!.maxPrice, filterData!!.fromDate, filterData!!.toDate)
                        }else{
                            viewModel.callRequest()
                        }
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

        return binding.root
    }
    private fun swipeRefreshToggle(){
        binding.swipeLoad.isRefreshing = swipeRefresh
    }

}