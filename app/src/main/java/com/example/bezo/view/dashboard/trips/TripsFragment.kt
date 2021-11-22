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
import com.example.bezo.view.util.PopUpMsg


class TripsFragment : Fragment() {
    private lateinit var binding : FragmentTripsBinding
    private lateinit var viewModel: TripsViewModel
    private var  loadMore : Boolean = true
    private var isLoading = false
    private var isScrolling = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTripsBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val viewModelFactory = TripsViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(TripsViewModel::class.java)
        binding.data = viewModel
        binding.lifecycleOwner = this

        //Adapter
        binding.tripsRecycler.adapter = TripAdapter(TripAdapter.OnClickListener{
            this.findNavController().navigate(TripsFragmentDirections.actionTripsFragmentToSingleTripsFragment(it))
        })

        //AppBar_onClick
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.filter ->{
                    true
                }
                else -> false
            }

        }

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

        return binding.root
    }


}