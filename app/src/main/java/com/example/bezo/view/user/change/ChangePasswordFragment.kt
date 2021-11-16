package com.example.bezo.view.user.change

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.R
import com.example.bezo.databinding.FragmentChangePasswordBinding
import com.example.bezo.databinding.FragmentSignInBinding
import com.example.bezo.view.registration.signin.SignInViewModel
import com.example.bezo.view.registration.signin.SignInViewModelFactory


class ChangePasswordFragment : Fragment() {
    private lateinit var binding: FragmentChangePasswordBinding
    private lateinit var viewModel: ChangePasswordViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //initialization
        binding = FragmentChangePasswordBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val viewModelFactory = ChangePasswordViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(ChangePasswordViewModel::class.java)
        binding.lifecycleOwner = this

        return binding.root
    }

}