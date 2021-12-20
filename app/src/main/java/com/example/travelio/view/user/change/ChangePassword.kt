package com.example.travelio.view.user.change

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.travelio.R
import com.example.travelio.databinding.ChangePasswordBinding
import com.example.travelio.view.util.PopUpMsg


class ChangePassword : Fragment() {
    private lateinit var binding: ChangePasswordBinding
    private lateinit var viewModel: ChangePasswordViewModel
    private var password:Boolean = false
    private var password1:Boolean = false
    private var passwordText:String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        //initialization
        binding = ChangePasswordBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val userData = ChangePasswordArgs.fromBundle(requireArguments()).userData
        val viewModelFactory = ChangePasswordViewModelFactory(application,userData)
        viewModel = ViewModelProvider(this,viewModelFactory).get(ChangePasswordViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        val navController = this.requireActivity().findNavController(R.id.nav_fragment_app)
        // if the user backstack then use user`s old data
        navController.previousBackStackEntry?.savedStateHandle?.set(this.resources.getString(R.string.USER), userData)

        //observer
        viewModel.error.observe(this.viewLifecycleOwner,{
            if(it != null){
                PopUpMsg.alertMsg(this.requireView(),it)
            }
        })

        // if there is any update put it in backStackEntry and pop back
        viewModel.newData.observe(this.viewLifecycleOwner,{
            if(it != null){
                navController.previousBackStackEntry?.savedStateHandle?.set(this.resources.getString(R.string.USER), it)
                PopUpMsg.toastMsg(this.requireContext(),this.resources.getString(R.string.BOOKING_DONE))
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

        binding.password.editText?.doOnTextChanged { text, _, _, _ ->
            passwordText = text.toString()
            when {
                text.isNullOrEmpty() -> {
                    binding.password.editText?.error = this.resources.getString(R.string.NO_PASSWORD)
                }
                text.trim().length < 8 -> {
                    binding.password.editText?.error = this.resources.getString(R.string.INVALID_PASSWORD)
                    password = false
                }
                else -> {
                    password = true
                }
            }
        }

        binding.password1.editText?.doOnTextChanged { text, _, _, _ ->
            if(text.isNullOrEmpty()){
                binding.password1.editText?.error = this.resources.getString(R.string.NO_PASSWORD)
            }else{
                password1 = true
            }
        }
        //onClickListeners
        binding.confirm.setOnClickListener {
            if(!password || !password1 || binding.password.editText?.text.isNullOrEmpty()){
                PopUpMsg.alertMsg(this.requireView(),this.resources.getString(R.string.NO_DATA))
            }else if (binding.password.editText?.text.toString() != binding.password1.editText?.text.toString()){     binding.password1.editText?.error = this.resources.getString(R.string.PASSWORD_SAME)}else{
                viewModel.getData(binding.password.editText?.text?.toString()!!.trim())
            }
        }



        //changeListeners
        return binding.root
    }

}