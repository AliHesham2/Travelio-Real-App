package com.example.travelio.view.dashboard.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travelio.R
import com.example.travelio.databinding.FragmentDashBoardBinding
import com.example.travelio.model.data.UserData
import com.example.travelio.model.preference.Local
import com.example.travelio.model.preference.Token
import com.example.travelio.view.dashboard.fulltrip.FullTripAdapter
import com.example.travelio.view.dashboard.hotel.HotelAdapter
import com.example.travelio.view.dashboard.orders.OrderActivity
import com.example.travelio.view.dashboard.transportation.TransportAdapter
import com.example.travelio.view.dashboard.trips.TripAdapter
import com.example.travelio.view.splash.MainActivity
import com.example.travelio.view.util.ChangeLanguagePopUp
import com.example.travelio.view.util.PopUpMsg


class DashBoard : Fragment() {
    private lateinit var binding: FragmentDashBoardBinding
    private lateinit var viewModel: DashBoardViewModel
    private var  hotelLoadMore : Boolean = true
    private var  transportLoadMore : Boolean = true
    private var  tripLoadMore : Boolean = true
    private var  packageLoadMore : Boolean = true
    private var isScrolling = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        //initialization
        binding = FragmentDashBoardBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        var userData = this.requireActivity().intent.extras?.getParcelable<UserData>(this.resources.getString(R.string.USER))
        val viewModelFactory = DashBoardViewModelFactory(application,userData)
        viewModel = ViewModelProvider(this,viewModelFactory).get(DashBoardViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.data = viewModel


        val navController = this.requireActivity().findNavController(R.id.nav_fragment_app)
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<UserData>(this.resources.getString(R.string.USER))?.observe(
            viewLifecycleOwner) { result ->
            if(result != null){
                userData = result
            }
        }
        //adapters
        binding.forHotels.adapter = HotelAdapter(HotelAdapter.OnClickListener{
            this.findNavController().navigate(DashBoardDirections.actionDashBoardToSingleHotelFragment(it))
        })

        binding.forPackages.adapter = FullTripAdapter(FullTripAdapter.OnClickListener{
            this.findNavController().navigate(DashBoardDirections.actionDashBoardToSingleFullTripFragment(it))
        })

        binding.forTransports.adapter = TransportAdapter(TransportAdapter.OnClickListener{
            this.findNavController().navigate(DashBoardDirections.actionDashBoardToSingleTransportationFragment(it))
        })

        binding.forTrips.adapter = TripAdapter(TripAdapter.OnClickListener{
            this.findNavController().navigate(DashBoardDirections.actionDashBoardToSingleTripsFragment(it))
        })

        //observer
        viewModel.error.observe(this.viewLifecycleOwner,{
            if(it != null){
                PopUpMsg.alertMsg(this.requireView(),it)
            }
        })

        viewModel.noAuth.observe(this.viewLifecycleOwner,{
            if(it == true){
                PopUpMsg.showLoginAgainDialogue(this)
            }
        })

        viewModel.data.observe(this.viewLifecycleOwner,{
            if(it != null){
                userData = it.data.user
            }
        })

        viewModel.fullTripLoadMore.observe(this.viewLifecycleOwner,{
            if(it != null){
                packageLoadMore = it
            }
        })
        viewModel.tripLoadMore.observe(this.viewLifecycleOwner,{
            if(it != null){
                tripLoadMore = it
            }
        })
        viewModel.hotelLoadMore.observe(this.viewLifecycleOwner,{
            if(it != null){
                hotelLoadMore = it
            }
        })
        viewModel.transportLoadMore.observe(this.viewLifecycleOwner,{
            if(it != null){
                transportLoadMore = it
            }
        })
        //Drawer Setting
        binding.topAppBar.setNavigationOnClickListener {
            binding.drawerLayout.open()
        }

        binding.navigationView.setNavigationItemSelectedListener {
            binding.drawerLayout.close()
            when(it.itemId){
                R.id.Trans ->{
                   if(viewModel.getCollections() != null){
                       this.findNavController().navigate(DashBoardDirections.actionDashBoardToTransportationFragment(viewModel.getCollections()!!))
                   }
                    true
                }
                R.id.hotel ->{
                    if(viewModel.getCollections() != null) {
                        this.findNavController()
                            .navigate(DashBoardDirections.actionDashBoardToHotelFragment(viewModel.getCollections()!!))
                    }
                    true
                }
                R.id.trip ->{
                    if(viewModel.getCollections() != null) {
                        this.findNavController()
                            .navigate(DashBoardDirections.actionDashBoardToTripsFragment(viewModel.getCollections()!!))
                    }
                    true
                }
                R.id.fullTrip ->{
                    if(viewModel.getCollections() != null) {
                        this.findNavController()
                            .navigate(DashBoardDirections.actionDashBoardToFullTripFragment(viewModel.getCollections()!!))
                    }
                    true
                }
                R.id.order ->{
                    this.requireActivity().startActivity(Intent(this.activity, OrderActivity::class.java))
                    true
                }
                R.id.editProfile ->{
                    if(viewModel.getCollections() != null) {
                        this.findNavController()
                            .navigate(DashBoardDirections.actionDashBoardToEditProfile2(userData!!,viewModel.getCollections()!!))
                        viewModel.resetData()
                    }
                    true
                }
                R.id.changePassword ->{
                    this.findNavController().navigate(DashBoardDirections.actionDashBoardToChangePassword2(userData!!))
                    viewModel.resetData()
                    true
                }
                R.id.changeLanguage ->{
                    ChangeLanguagePopUp.changeLanguage(this.requireContext(),ChangeLanguagePopUp.returnLanguageID(this.requireContext())){ lang ->
                        ChangeLanguagePopUp.checkIfItCurrentLanguage(lang){ isCurrent ->
                            if (!isCurrent){
                                Local.saveLanguage(lang)
                                this.requireActivity().startActivity(Intent(this.activity, MainActivity::class.java))
                                this.requireActivity().finish()
                            }
                        }
                    }
                    true
                }
                R.id.signOut ->{
                    Token.removeToken()
                    this.requireActivity().startActivity(Intent(this.activity, MainActivity::class.java))
                    this.requireActivity().finish()
                    true
                }
                else -> false
            }
        }
        //onClick
        binding.hotelsSeeAll.setOnClickListener {
            this.findNavController().navigate(DashBoardDirections.actionDashBoardToHotelFragment(viewModel.getCollections()!!))
        }
        binding.transportSeeAll.setOnClickListener {
            this.findNavController().navigate(DashBoardDirections.actionDashBoardToTransportationFragment(viewModel.getCollections()!!))
        }
        binding.tripsSeeAll.setOnClickListener {
            this.findNavController().navigate(DashBoardDirections.actionDashBoardToTripsFragment(viewModel.getCollections()!!))
        }
        binding.packageSeeAll.setOnClickListener {
            this.findNavController().navigate(DashBoardDirections.actionDashBoardToFullTripFragment(viewModel.getCollections()!!))
        }

        //paging
        binding.forTrips.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(tripLoadMore){
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
                    val isNotAtBeginning = firstVisibleItemPosition >= 0
                    val isTotalMoreThanVisible = totalItemCount >= 5
                    val shouldPaginate =   isAtLastItem && isNotAtBeginning &&
                            isTotalMoreThanVisible && isScrolling
                    if(shouldPaginate) {
                        viewModel.callRequestTrip()
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
        binding.forHotels.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(hotelLoadMore){
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
                    val isNotAtBeginning = firstVisibleItemPosition >= 0
                    val isTotalMoreThanVisible = totalItemCount >= 5
                    val shouldPaginate =   isAtLastItem && isNotAtBeginning &&
                            isTotalMoreThanVisible && isScrolling
                    if(shouldPaginate) {
                        viewModel.callRequestHotel()
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
        binding.forTransports.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(transportLoadMore){
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
                    val isNotAtBeginning = firstVisibleItemPosition >= 0
                    val isTotalMoreThanVisible = totalItemCount >= 5
                    val shouldPaginate =   isAtLastItem && isNotAtBeginning &&
                            isTotalMoreThanVisible && isScrolling
                    if(shouldPaginate) {
                        viewModel.callRequestTransport()
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
        binding.forPackages.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(packageLoadMore){
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
                    val isNotAtBeginning = firstVisibleItemPosition >= 0
                    val isTotalMoreThanVisible = totalItemCount >= 5
                    val shouldPaginate =   isAtLastItem && isNotAtBeginning &&
                            isTotalMoreThanVisible && isScrolling
                    if(shouldPaginate) {
                        viewModel.callRequestPackage()
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