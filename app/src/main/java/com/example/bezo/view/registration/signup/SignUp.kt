package com.example.bezo.view.registration.signup

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bezo.R
import com.example.bezo.databinding.FragmentSignUpBinding
import com.example.bezo.model.data.UserSignUpData
import com.example.bezo.view.dashboard.DashBoardActivity
import com.example.bezo.view.util.PopUpMsg


class SignUp : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpViewModel
    private var username:Boolean = false
    private var email:Boolean = false
    private var phone:Boolean = false
    private var password:Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        //initialization
        binding = FragmentSignUpBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val viewModelFactory = SignUpViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(SignUpViewModel::class.java)
        binding.lifecycleOwner = this


        //observers
        viewModel.error.observe(this.viewLifecycleOwner,{
            if(it != null){
                PopUpMsg.alertMsg(this.requireView(),it)
            }

        })

        viewModel.isSuccess.observe(this.viewLifecycleOwner,{
            if(it == true){
                startActivity(Intent(this.activity, DashBoardActivity::class.java))
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

        //dropdownMenu
        val items = listOf("Alex", "Cairo", "Option 3", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4", "Option 4")
        val adapter = ArrayAdapter(requireContext(),  android.R.layout.simple_dropdown_item_1line, items)
        (binding.city.editText as? AutoCompleteTextView)?.setAdapter(adapter)

        //changeListeners
        binding.username.editText?.doOnTextChanged { text, _, _, _ ->
            if(text.isNullOrEmpty()){
                binding.username.editText?.error = this.resources.getString(R.string.NO_NAME)
                binding.username.endIconDrawable = null
            }else if (text.trim().length <= 2){
                binding.username.editText?.error = this.resources.getString(R.string.INVALID_NAME)
                binding.username.endIconDrawable = null
                username = false
            }else{
                username = true
                binding.username.setEndIconDrawable(R.drawable.check)
            }
        }

        binding.email.editText?.doOnTextChanged { text, _, _, _ ->
            if(text.isNullOrEmpty()){
                binding.email.editText?.error= this.resources.getString(R.string.NO_EMAIL)
                binding.email.endIconDrawable = null
            }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()){
                binding.email.editText?.error = this.resources.getString(R.string.INVALID_EMAIL_FORM)
                email = false
                binding.email.endIconDrawable = null
            }else{
                email = true
                binding.email.setEndIconDrawable(R.drawable.check)
            }
        }

        binding.phone.editText?.doOnTextChanged { text, _, _, _ ->
            if(text.isNullOrEmpty()){
                binding.phone.editText?.error= this.resources.getString(R.string.NO_PHONE)
                binding.phone.endIconDrawable = null
            }else if(text.trim().length < 11 ||text.trim().length > 11 ){
                binding.phone.editText?.error = this.resources.getString(R.string.INVALID_PHONE)
                binding.phone.endIconDrawable = null
                phone = false
            }else{
                phone = true
                binding.phone.setEndIconDrawable(R.drawable.check)
            }
        }

        binding.password.editText?.doOnTextChanged { text, _, _, _ ->
            if(text.isNullOrEmpty()){
                binding.password.editText?.error = this.resources.getString(R.string.NO_PASSWORD)
            }else if(text.trim().length < 8  ){
                binding.password.editText?.error = this.resources.getString(R.string.INVALID_PASSWORD)
                password = false
            }else{
                password = true
            }
        }
            
        //onClickListeners
        binding.signIn.setOnClickListener {
            if(!phone || !email || !username || !password || binding.phone.editText?.text.isNullOrEmpty()|| binding.username.editText?.text.isNullOrEmpty()
                || binding.email.editText?.text.isNullOrEmpty() || binding.password.editText?.text.isNullOrEmpty() || binding.city.editText?.text.isNullOrEmpty()){
                PopUpMsg.alertMsg(this.requireView(),this.resources.getString(R.string.NO_DATA))
            }else{
                viewModel.getData(UserSignUpData(binding.username.editText?.text.toString(),binding.email.editText?.text?.toString()!!.trim()
                                                 ,binding.phone.editText?.text?.toString()!!.trim(), binding.city.editText?.text.toString(),binding.password.editText?.text?.toString()!!.trim()))
            }
        }

        binding.social.setOnClickListener {
            this.findNavController().navigate(SignUpDirections.actionSignUpToSignIn())
        }




        return binding.root
    }
}