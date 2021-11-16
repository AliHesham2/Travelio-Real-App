package com.example.bezo.view.user.edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.bezo.R
import com.example.bezo.databinding.FragmentEditProfileBinding
import com.example.bezo.databinding.FragmentSignInBinding
import com.example.bezo.view.registration.signin.SignInViewModel
import com.example.bezo.view.registration.signin.SignInViewModelFactory


class EditProfileFragment : Fragment() {
    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //initialization
        binding = FragmentEditProfileBinding.inflate(inflater)
        val application = requireNotNull(activity).application
        val viewModelFactory = EditProfileViewModelFactory(application)
        viewModel = ViewModelProvider(this,viewModelFactory).get(EditProfileViewModel::class.java)
        binding.lifecycleOwner = this


        return binding.root
    }
}