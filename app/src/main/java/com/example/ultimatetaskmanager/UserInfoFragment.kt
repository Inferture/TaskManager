package com.example.ultimatetaskmanager


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.MainScope


class UserInfoFragment : Fragment() {

    lateinit var inflaterContext:Context

    private val coroutineScope = MainScope()
    private val userViewModel by lazy {
        ViewModelProviders.of(this).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        inflaterContext=inflater.context

        var view = inflater.inflate(R.layout.fragment_user_info, container, false)

        var backButton = view.findViewById<Button>(R.id.back_from_userinfos)
        backButton.setOnClickListener()
        {
            findNavController().navigate(R.id.action_userInfoFragment_to_mainActivity2)
        }

        return view
    }



}
