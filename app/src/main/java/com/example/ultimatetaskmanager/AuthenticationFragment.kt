package com.example.ultimatetaskmanager


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.ultimatetaskmanager.network.Api

class AuthenticationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view =  inflater.inflate(R.layout.fragment_authentication, container, false)

        var loginButton = view.findViewById<Button>(R.id.auth_login)
        loginButton.setOnClickListener()
        {
            findNavController().navigate(R.id.action_authenticationFragment_to_loginFragment)
        }

        var signupButton = view.findViewById<Button>(R.id.auth_signup)
        signupButton.setOnClickListener()
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
