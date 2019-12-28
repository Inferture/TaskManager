package com.example.ultimatetaskmanager


import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.ultimatetaskmanager.databinding.FragmentSignupBinding
import com.example.ultimatetaskmanager.network.Api
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception


class SignupFragment : Fragment() {


    lateinit var binding: FragmentSignupBinding
    private val coroutineScope = MainScope()
    private val userViewModel by lazy {
        ViewModelProviders.of(this).get(UserViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup,container, false)

        var view =  binding.root
        binding.signup.setOnClickListener()
        {

            if(binding.signupEmail.text.toString().equals("") )
            {
                Toast.makeText(context, "Email empty", Toast.LENGTH_LONG).show()
            }
            else if(!binding.signupEmail.text.toString().contains("@") || binding.signupEmail.text.toString().split("@")[0].length==0|| binding.signupEmail.text.toString().split("@")[1].length==0)
            {
                Toast.makeText(context, "Email is invalid", Toast.LENGTH_LONG).show()
            }
            else if(binding.signupPassword.text.toString().equals(""))
            {
                Toast.makeText(context, "Password empty", Toast.LENGTH_LONG).show()
            }
            else if(binding.signupPassword.text.toString().length<6)
            {
                Toast.makeText(context, "Password should have at least 6 characters", Toast.LENGTH_LONG).show()
            }
            else if(binding.signupPasswordConfirm.text.toString().equals(""))
            {
                Toast.makeText(context, "Please confirm your password", Toast.LENGTH_LONG).show()
            }
            else if(!binding.signupPasswordConfirm.text.toString().equals(binding.signupPassword.text.toString()))
            {
                Toast.makeText(context, "Password and password confirmation don't match", Toast.LENGTH_LONG).show()
            }
            else
            {
                var signupInfos = SignupForm(binding.signupFirstname.text.toString(), binding.signupLastname.text.toString(),binding.signupEmail.text.toString(), binding.signupPassword.text.toString(), binding.signupPasswordConfirm.text.toString())
                var tokenResponse = signup(signupInfos)
            }

        }

        return view
    }



    fun signup(signupForm: SignupForm): LiveData<TokenResponse?> {
        val tokenData = MutableLiveData<TokenResponse?>()

        coroutineScope.launch {
            var response = loadSignup(signupForm)
            tokenData.postValue(response)
        }
        return tokenData
    }

    suspend fun loadSignup(signupForm: SignupForm): TokenResponse? {
        Log.i("MyStuff","about to try signing up")

        lateinit var tokenResponse: Response<TokenResponse>
        try {
            tokenResponse = Api.INSTANCE.userService.signup(signupForm)
        }
        catch(e : Exception){
            Log.i("MyStuff","Error:" + e.toString())
            Toast.makeText(context, "Error when connecting to the Task api", Toast.LENGTH_LONG).show()
            return null
        }


        if(tokenResponse.isSuccessful)
        {
            PreferenceManager.getDefaultSharedPreferences(context).edit {
                putString(SHARED_PREF_TOKEN_KEY, tokenResponse?.body()?.token)
            }
            findNavController().navigate(R.id.action_signupFragment_to_mainActivity)
            return tokenResponse.body()
        }
        else
        {
            if(tokenResponse.code()==401)
            {
                Toast.makeText(context, "Unauthorized", Toast.LENGTH_LONG).show()
            }
            else if(tokenResponse.code() == 422)
            {
                Toast.makeText(context, "Unprocessable entity (Is your email valid ?)" , Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(context, "Unknown error: code " + tokenResponse.code() + ";" + tokenResponse.message(), Toast.LENGTH_LONG).show()
            }
            return null
        }
    }

}
