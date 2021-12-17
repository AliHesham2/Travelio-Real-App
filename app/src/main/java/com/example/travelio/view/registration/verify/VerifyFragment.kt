package com.example.travelio.view.registration.verify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.example.travelio.databinding.FragmentVerifyBinding


class VerifyFragment : Fragment() {
    lateinit var binding : FragmentVerifyBinding
    lateinit var viewModel: VerifyViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //initialization
        binding = FragmentVerifyBinding.inflate(inflater)
        viewModel =  ViewModelProvider(this).get(VerifyViewModel::class.java)
        binding.lifecycleOwner = this

        //observers


        //onClickListeners


        //ChangeListeners
        binding.num1.editText?.doOnTextChanged { text, _, _, _ ->
            if(text.toString().trim().isNotEmpty()){
                binding.num2.requestFocus()
            }
        }
        binding.num2.editText?.doOnTextChanged { text, _, _, _ ->
            if(text.toString().trim().isNotEmpty()){
                binding.num3.requestFocus()
            }
        }
        binding.num3.editText?.doOnTextChanged { text, _, _, _ ->
            if(text.toString().trim().isNotEmpty()){
                binding.num4.requestFocus()
            }
        }



        return  binding.root
    }

}