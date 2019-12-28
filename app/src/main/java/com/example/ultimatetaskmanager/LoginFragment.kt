package com.example.ultimatetaskmanager


import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.ultimatetaskmanager.databinding.FragmentLoginBinding
import com.example.ultimatetaskmanager.network.Api
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception


class LoginFragment : Fragment() {


    lateinit var binding: FragmentLoginBinding

    private val coroutineScope = MainScope()
    private val userViewModel by lazy {
        ViewModelProviders.of(this).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login,container, false)
        var view =  binding.root

        binding.login.setOnClickListener()
        {
            if(binding.emailText.text.toString().equals("") )
            {
                Toast.makeText(context, "Email empty", Toast.LENGTH_LONG).show()
            }
            else if(binding.passwordText.text.toString().equals(""))
            {
                Toast.makeText(context, "Password empty", Toast.LENGTH_LONG).show()
            }
            else
            {
                var loginInfos = LoginForm(binding.emailText.text.toString(), binding.passwordText.text.toString())
                var tokenResponse = login(loginInfos)
            }
        }
        return view
    }

    fun login(loginForm: LoginForm): LiveData<TokenResponse?> {
        val tokenData = MutableLiveData<TokenResponse?>()

        coroutineScope.launch {
            var response = loadLogin(loginForm)
            tokenData.postValue(response)
        }
        return tokenData
    }

    suspend fun loadLogin(loginForm: LoginForm): TokenResponse? {
        Log.i("MyStuff","about to try logging in")

        lateinit var tokenResponse: Response<TokenResponse>
        try {
            tokenResponse = Api.INSTANCE.userService.login(loginForm)
        }
        catch(e :Exception){
            Log.i("MyStuff","Error:" + e.toString())
            Toast.makeText(context, "Error when connecting to the Task api", Toast.LENGTH_LONG).show()
            return null
        }


        if(tokenResponse.isSuccessful)
        {
            PreferenceManager.getDefaultSharedPreferences(context).edit {
                putString(SHARED_PREF_TOKEN_KEY, tokenResponse?.body()?.token)
            }
            findNavController().navigate(R.id.action_loginFragment_to_mainActivity)
            return tokenResponse.body()
        }
        else
        {
            if(tokenResponse.code()==401)
            {
                Toast.makeText(context, "Unauthorized: wrong email/password combination", Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(context, "Unknown error: code " + tokenResponse.code(), Toast.LENGTH_LONG).show()
            }
            return null
        }
    }
}
