package com.example.bezo.view.dashboard.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.bezo.R
import com.example.bezo.databinding.FragmentDashBoardBinding
import com.example.bezo.model.data.UserData
import com.example.bezo.model.preference.Token
import com.example.bezo.view.dashboard.orders.OrderActivity
import com.example.bezo.view.splash.MainActivity
import com.example.bezo.view.util.PopUpMsg
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DashBoard : Fragment() {
    private lateinit var binding: FragmentDashBoardBinding
    private lateinit var viewModel: DashBoardViewModel
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
                R.id.signOut ->{
                    Token.removeToken()
                    this.requireActivity().startActivity(Intent(this.activity, MainActivity::class.java))
                    this.requireActivity().finish()
                    true
                }
                else -> false
            }
        }
        return binding.root
    }
}