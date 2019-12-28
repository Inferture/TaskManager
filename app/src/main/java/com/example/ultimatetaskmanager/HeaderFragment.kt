package com.example.ultimatetaskmanager


import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ultimatetaskmanager.databinding.FragmentHeaderBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.load.DecodeFormat

class HeaderFragment : Fragment() {


    private lateinit var binding: FragmentHeaderBinding

    private val coroutineScope = MainScope()
    private val userViewModel by lazy {
        ViewModelProviders.of(this).get(UserViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("MyStuff", "onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_header,container, false)
        var view =  binding.root


        Log.i("MyStuff", "end onCreateView")
        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {



        Log.i("MyStuff", "onCreate")
        userViewModel.getUserInfos().observe(this, Observer {
            if (it != null) {
                binding.currentUser=it
                Log.i("MyStuff", "onCreate zz")
               Glide.with(this).load(it.avatar).apply(
                    RequestOptions.circleCropTransform()).into(binding.imageView)
                //binding.invalidateAll()
            }
        })
        super.onCreate(savedInstanceState)
    }

    override fun onResume()
    {
        super.onResume()
        userViewModel.getUserInfos()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

}
