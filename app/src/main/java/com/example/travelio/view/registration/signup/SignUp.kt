package com.example.travelio.view.registration.signup

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
import com.example.travelio.R
import com.example.travelio.databinding.FragmentSignUpBinding
import com.example.travelio.model.data.UserSignUpData
import com.example.travelio.view.dashboard.DashBoardActivity
import com.example.travelio.view.util.PopUpMsg


class SignUp : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var viewModel: SignUpViewModel
    private var username:Boolean = false
    private var email:Boolean = false
    private var phone:Boolean = false
    private var password:Boolean = false
    private var items = listOf<String>()
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
        viewModel.citiesList.observe(this.viewLifecycleOwner,{
            if(!it.isNullOrEmpty()){
                items = it
                setUpCity()
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
            if(it == true && items.isNotEmpty()){
                PopUpMsg.showDialogue(this.requireContext())
            }else{
                if(items.isNotEmpty()){
                    PopUpMsg.hideDialogue()
                }
            }
        })

        //dropdownMenu
        setUpCity()

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
                binding.username.setEndIconDrawable(R.drawable.outline_done_24)
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
                binding.email.setEndIconDrawable(R.drawable.outline_done_24)
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
                binding.phone.setEndIconDrawable(R.drawable.outline_done_24)
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
            }else if(!items.contains(binding.city.editText?.text.toString().trim())){
                binding.city.editText?.error = this.resources.getString(R.string.FROM_LIST)
            }else{
                viewModel.getData(UserSignUpData(name = binding.username.editText?.text.toString(), email = binding.email.editText?.text?.toString()!!.trim()
                                                 ,phone = binding.phone.editText?.text?.toString()!!.trim(),city_id = viewModel.getID(binding.city.editText?.text.toString().trim()) ?: 0,password = binding.password.editText?.text?.toString()!!.trim()))
            }
        }

        binding.social.setOnClickListener {
            this.findNavController().navigate(SignUpDirections.actionSignUpToSignIn())
        }

        return binding.root
    }
    private fun setUpCity() {
        //dropdownMenu
        val adapter = ArrayAdapter(requireContext(),  android.R.layout.simple_dropdown_item_1line, this.items)
        (binding.city.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        (binding.city.editText as? AutoCompleteTextView)?.threshold = 1

    }
}