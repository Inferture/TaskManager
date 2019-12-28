package com.example.ultimatetaskmanager


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.ultimatetaskmanager.databinding.FragmentAuthenticationBinding
import com.example.ultimatetaskmanager.network.Api

class AuthenticationFragment : Fragment() {


    lateinit var binding:FragmentAuthenticationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_authentication,container, false)
        var view =  binding.root

        binding.authLogin.setOnClickListener()
        {
            findNavController().navigate(R.id.action_authenticationFragment_to_loginFragment)
        }

        binding.authSignup.setOnClickListener()
        {
            findNavController().navigate(R.id.action_authenticationFragment_to_signupFragment)
        }

        if(Api.INSTANCE.getToken()!=null)
        {
            findNavController().navigate(R.id.action_authenticationFragment_to_mainActivity)
        }
        return view
    }

}
