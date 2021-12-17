package com.example.travelio.view.dashboard.hotel

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
import com.example.travelio.databinding.FragmentHotelBinding
import com.example.travelio.model.data.HotelFilterCollection
import com.example.travelio.view.util.HotelFilterPopUp
import com.example.travelio.view.util.PopUpMsg


class HotelFragment : Fragment() {
    private lateinit var binding:FragmentHotelBinding
    private lateinit var viewModel:HotelViewModel
    private var  loadMore : Boolean = true
    private var  swipeRefresh : Boolean = true
    private var isLoading = false
    private var isScrolling = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        //Initialization
        binding = FragmentHotelBinding.inflate(inflater,container,false)
        val application = requireNotNull(activity).application
        val collection = HotelFragmentArgs.fromBundle(requireArguments()).collection
        var filterData : HotelFilterCollection?= null
        val viewModelFactory = HotelViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(HotelViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = viewLifecycleOwner


        //Adapter
        binding.hotelsRecycler.adapter = HotelAdapter(HotelAdapter.OnClickListener{
            this.findNavController().navigate(HotelFragmentDirections.actionHotelFragmentToSingleHotelFragment(it))
        })

        //AppBar_onClick
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.filter ->{
                    HotelFilterPopUp.handleHotelFilter(this.requireContext(),collection,filterData){ data ->
                        viewModel.saveFilterData(data)
                        viewModel.resetData()
                        viewModel.callRequest(data.hotels_list_id,data.hotels_list_city_id,data.meal_id,data.stars,data.perRoom,data.minPrice,data.maxPrice)
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
                        if (filterData != null){
                            viewModel.callRequest(filterData!!.hotels_list_id, filterData!!.hotels_list_city_id, filterData!!.meal_id, filterData!!.stars, filterData!!.perRoom, filterData!!.minPrice, filterData!!.maxPrice)
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

        return  binding.root
    }
    private fun swipeRefreshToggle(){
        binding.swipeLoad.isRefreshing = swipeRefresh
    }
}