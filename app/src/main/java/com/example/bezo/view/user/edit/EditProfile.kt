package com.example.bezo.view.user.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.bezo.R
import com.example.bezo.databinding.EditProfileBinding
import com.example.bezo.view.util.PopUpMsg


class EditProfile : Fragment() {
    private lateinit var binding: EditProfileBinding
    private lateinit var viewModel: EditProfileViewModel
    private var username:Boolean = false
    private var email:Boolean = false
    private var phone:Boolean = false
    private var items = listOf<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        //initialization
        binding = EditProfileBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val userData = EditProfileArgs.fromBundle(requireArguments()).userData
        val collection = EditProfileArgs.fromBundle(requireArguments()).collection
        val viewModelFactory = EditProfileViewModelFactory(application,userData,collection)
        viewModel = ViewModelProvider(this,viewModelFactory).get(EditProfileViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.data = viewModel
        val navController = this.requireActivity().findNavController(R.id.nav_fragment_app)
        // if the user backstack then use user`s old data
        navController.previousBackStackEntry?.savedStateHandle?.set(this.resources.getString(R.string.USER), userData)

        //dropdownMenu
        setUpCity()

        //observer
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

        // if there is any update put it in backStackEntry and pop back
        viewModel.newData.observe(this.viewLifecycleOwner,{
            if(it != null){
                navController.previousBackStackEntry?.savedStateHandle?.set(this.resources.getString(R.string.USER), it)
                PopUpMsg.toastMsg(this.requireContext(),this.resources.getString(R.string.Updated))
                this.findNavController().popBackStack()
            }
        })

        viewModel.loading.observe(this.viewLifecycleOwner,{
            if(it == true){
                PopUpMsg.showDialogue(this.requireContext())
            }else{
                    PopUpMsg.hideDialogue()
            }
        })

        viewModel.noAuth.observe(this.viewLifecycleOwner,{
            if(it == true){
                PopUpMsg.showLoginAgainDialogue(this)
            }
        })

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

        //onClickListeners
        binding.confirmUpdate.setOnClickListener {
            if(!phone || !email || !username || binding.phone.editText?.text.isNullOrEmpty()|| binding.username.editText?.text.isNullOrEmpty()
                || binding.email.editText?.text.isNullOrEmpty() || binding.city.editText?.text.isNullOrEmpty()  ){
                PopUpMsg.alertMsg(this.requireView(),this.resources.getString(R.string.NO_DATA))
            }else if(!items.contains(binding.city.editText?.text.toString().trim())){
                binding.city.editText?.error = this.resources.getString(R.string.FROM_LIST)
            }else{
                viewModel.getData(binding.username.editText?.text.toString(),binding.email.editText?.text?.toString()!!.trim(),binding.phone.editText?.text?.toString()!!.trim(),binding.city.editText?.text.toString().trim())
            }
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