package com.example.ultimatetaskmanager


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.ultimatetaskmanager.databinding.FragmentUserInfoBinding
import kotlinx.coroutines.MainScope


class UserInfoFragment : Fragment() {


    lateinit var binding:FragmentUserInfoBinding

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

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_info,container, false)
        var view = binding.root
        binding.backFromUserinfos.setOnClickListener()
        {
            findNavController().navigate(R.id.action_userInfoFragment_to_mainActivity2)
        }

        return view
    }



}
