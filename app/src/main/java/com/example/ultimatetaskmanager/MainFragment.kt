package com.example.ultimatetaskmanager


import android.os.Bundle
import android.preference.PreferenceManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.ultimatetaskmanager.databinding.FragmentMainBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*


class MainFragment : Fragment() {


    lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main,container, false)

        var view = binding.root


        binding.addTask.setOnClickListener({
            nav_host_fragment_main.findNavController().navigate(R.id.action_mainFragment_to_taskFormActivity)
        })



        var disconnectButton = view.findViewById<Button>(R.id.disconnect)

        disconnectButton.setOnClickListener(
            {
                PreferenceManager.getDefaultSharedPreferences(inflater.context).edit {
                    putString(SHARED_PREF_TOKEN_KEY, null)
                }

                nav_host_fragment_main.findNavController().navigate(R.id.action_mainFragment_to_authenticationActivity)
            }
        )
        var editProfile = view.findViewById<Button>(R.id.edit_profile)
        editProfile.setOnClickListener(
            {

                nav_host_fragment_main.findNavController().navigate(R.id.action_mainFragment_to_userInfoActivity)
            }
        )
        navController=findNavController()
        return view
    }

    companion object
    {
        lateinit var  navController : NavController
    }
}
