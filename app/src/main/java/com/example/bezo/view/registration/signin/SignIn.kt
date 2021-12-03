package com.example.bezo.view.registration.signin

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bezo.R
import com.example.bezo.databinding.FragmentSignInBinding
import com.example.bezo.model.data.UserLoginData
import com.example.bezo.view.dashboard.DashBoardActivity
import com.example.bezo.view.util.PopUpMsg



class SignIn : Fragment() {
    private lateinit var binding: FragmentSignInBinding
    private lateinit var viewModel: SignInViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        //initialization
        binding = FragmentSignInBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val viewModelFactory = SignInViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(SignInViewModel::class.java)
        binding.lifecycleOwner = this

        //observers
        viewModel.error.observe(this.viewLifecycleOwner,{
            if(it != null){
                PopUpMsg.alertMsg(this.requireView(),it)
            }

        })

        viewModel.isSuccess.observe(this.viewLifecycleOwner,{
            if(it != null){
                val intent = Intent(this.activity, DashBoardActivity::class.java)
                intent.putExtra(this.resources.getString(R.string.USER),it)
                startActivity(intent)
                this.activity?.finish()
            }
        })

        viewModel.loading.observe(this.viewLifecycleOwner,{
            if(it == true){
                 PopUpMsg.showDialogue(this.requireContext())
            }else{
                PopUpMsg.hideDialogue()
            }
        })

        //onClickListeners
        binding.signIn.setOnClickListener {
            if(  binding.phone.editText?.text.isNullOrEmpty() || binding.password.editText?.text.isNullOrEmpty() ){
                PopUpMsg.alertMsg(this.requireView(),this.resources.getString(R.string.NO_DATA))
            }else{
                viewModel.getData(UserLoginData(binding.phone.editText?.text?.toString()!!.trim(),binding.password.editText?.text?.toString()!!.trim()))
            }
        }

        binding.social.setOnClickListener{
            this.findNavController().navigate(SignInDirections.actionSignInToSignUp())
        }

        binding.forgetPassword.setOnClickListener {

        }


       return binding.root
    }


}