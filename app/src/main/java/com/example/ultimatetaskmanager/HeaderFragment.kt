package com.example.ultimatetaskmanager


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.ultimatetaskmanager.network.Api
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.fragment_header.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


class HeaderFragment : Fragment() {

    private val coroutineScope = MainScope()
    private val userViewModel by lazy {
        ViewModelProviders.of(this).get(UserViewModel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_header, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        userViewModel.getUserInfos().observe(this, Observer {
            if (it != null) {
                username_text.setText(it.email)
                Glide.with(this).load(it.avatar).apply(
                    RequestOptions.circleCropTransform()).into(image_view)
            }
        })
        super.onCreate(savedInstanceState)
    }

    override fun onResume()
    {
        super.onResume()
        Glide.with(this).load("https://cdn.myanimelist.net/images/anime/1153/99850.jpg").apply(
            RequestOptions.circleCropTransform()).into(image_view)

        coroutineScope.launch {
            Api.userService.getInfo()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

}
