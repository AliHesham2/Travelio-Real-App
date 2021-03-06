package com.example.travelio.view.dashboard.fulltrip

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
import com.example.travelio.R
import com.example.travelio.databinding.FragmentFullTripBinding
import com.example.travelio.model.data.FullTripFilterCollection
import com.example.travelio.view.util.FullTripFilterPopUp
import com.example.travelio.view.util.PopUpMsg


class FullTripFragment : Fragment() {
    private lateinit var binding : FragmentFullTripBinding
    private lateinit var viewModel: FullTripViewModel
    private var  loadMore : Boolean = true
    private var isScrolling = false
    private var  swipeRefresh : Boolean = true
    private var isLoading = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //initialization
        binding = FragmentFullTripBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val collection = FullTripFragmentArgs.fromBundle(requireArguments()).collection
        var filterData : FullTripFilterCollection?= null
        val viewModelFactory = FullTripViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(FullTripViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //Adapter
        binding.fullTripsRecycler.adapter = FullTripAdapter(FullTripAdapter.OnClickListener{
            this.findNavController().navigate(FullTripFragmentDirections.actionFullTripFragmentToSingleFullTripFragment(it))
        })

        //AppBar_onClick
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.filter ->{
                    FullTripFilterPopUp.handleFullTripFilter(this.requireContext(),collection,filterData){ data ->
                        viewModel.saveFilterData(data)
                        viewModel.resetData()
                        viewModel.callRequest(data.hotels_list_id,data.hotels_list_city_id,data.minPrice,data.maxPrice,data.fromDate,data.toDate)
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

        viewModel.noAuth.observe(this.viewLifecycleOwner,{
            if(it == true){
                PopUpMsg.showLoginAgainDialogue(this)
            }
        })

        viewModel.loadMore.observe(this.viewLifecycleOwner,{
            if(it != null){
                loadMore = it
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
        binding.fullTripsRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener(){
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
                    val shouldPaginate =   isAtLastItem && isNotAtBeginning && isTotalMoreThanVisible && isScrolling && !isLoading
                    if(shouldPaginate) {
                        if(filterData != null){
                            viewModel.callRequest(filterData!!.hotels_list_id, filterData!!.hotels_list_city_id, filterData!!.minPrice, filterData!!.maxPrice, filterData!!.fromDate, filterData!!.toDate)
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