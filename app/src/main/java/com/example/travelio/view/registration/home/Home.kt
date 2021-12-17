package com.example.travelio.view.registration.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.travelio.databinding.FragmentHomeBinding

class Home : Fragment() {
    private lateinit var binding:FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        //initialization
        binding = FragmentHomeBinding.inflate(inflater)

        //onClickListeners
        binding.signIn.setOnClickListener {
            this.findNavController().navigate(HomeDirections.actionHomeFragmentToSignIn())
        }

        binding.signUp.setOnClickListener {
            this.findNavController().navigate(HomeDirections.actionHomeFragmentToSignUp())
        }

        return binding.root
    }
}