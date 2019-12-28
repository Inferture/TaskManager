package com.example.ultimatetaskmanager

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.ultimatetaskmanager.databinding.ActivityUserInfoBinding
import com.example.ultimatetaskmanager.network.UserInfo
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class UserInfoActivity : AppCompatActivity() {


    private lateinit var binding: ActivityUserInfoBinding


    private val coroutineScope = MainScope()
    private val userViewModel by lazy {
        ViewModelProviders.of(this).get(UserViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info)

        Log.i("MyStuff","create userinfoact")


        userViewModel.getPicture().observe(this, Observer {

            if (it != null) {

                Glide.with(this).load(it).apply(
                    RequestOptions.circleCropTransform()).into(binding.photoView)
            }
        })

        userViewModel.getUserInfos().observe(this, Observer {

            if (it != null) {


                userViewModel.userInfo=it;
                binding.apply{
                    currentUser=it
                    invalidateAll()
                }
                Glide.with(this).load(it.avatar).apply(
                    RequestOptions.circleCropTransform()).into(binding.photoView)

            }
        })

        Log.i("MyStuff","create stuff")


        binding.textFirstname.setOnClickListener()
        {
            binding.apply {
                editFirstname.visibility = View.VISIBLE
                changeFirstname.visibility = View.VISIBLE
                cancelFirstname.visibility = View.VISIBLE
                textFirstname.visibility = View.INVISIBLE
                editFirstname.requestFocus();
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editFirstname, 0)
            }
        }
        binding.changeFirstname.setOnClickListener()
        {
            var currentInfo = userViewModel.userInfo

            binding.apply {
                currentUser=UserInfo(editFirstname.text.toString(), currentInfo.lastName, currentInfo.email, currentInfo.avatar)
                userViewModel.updateUser(currentUser!!)

                changeFirstname.visibility=View.GONE
                cancelFirstname.visibility=View.GONE
                editFirstname.visibility=View.INVISIBLE
                textFirstname.visibility=View.VISIBLE
                invalidateAll()


            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(changeFirstname.windowToken, 0)
            }
        }

        binding.cancelFirstname.setOnClickListener()
        {
            cancelFirstname()
        }

        binding.editFirstname.setOnFocusChangeListener()
        {
            view:View, b:Boolean->
                if(!b)
                {
                    cancelFirstname()
                }
        }




        binding.textLastname.setOnClickListener()
        {
            binding.apply {
                editLastname.visibility = View.VISIBLE
                changeLastname.visibility = View.VISIBLE
                cancelLastname.visibility = View.VISIBLE
                textLastname.visibility = View.INVISIBLE
                editLastname.requestFocus();

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(editFirstname, 0)
            }

        }
        
        binding.changeLastname.setOnClickListener()
        {
            var currentInfo = userViewModel.userInfo
            binding.apply {
                currentUser=UserInfo(currentInfo.firstName, editLastname.text.toString(), currentInfo.email, currentInfo.avatar)
                userViewModel.updateUser(currentUser!!)

                changeLastname.visibility=View.GONE
                cancelLastname.visibility=View.GONE
                editLastname.visibility=View.INVISIBLE
                textLastname.visibility=View.VISIBLE
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(changeLastname.windowToken, 0)
                invalidateAll()
            }

        }

        binding.cancelLastname.setOnClickListener()
        {
            cancelLastname()
        }
        binding.editLastname.setOnFocusChangeListener()
        {
                view:View, b:Boolean->
            if(!b)
            {
                cancelLastname()
            }
        }
        
        binding.textEmail.setOnClickListener()
        {
            binding.apply {
                editEmail.visibility = View.VISIBLE
                changeEmail.visibility = View.VISIBLE
                cancelEmail.visibility = View.VISIBLE
                textEmail.visibility = View.INVISIBLE
                editEmail.requestFocus()

                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(editEmail, 0)
            }


        }


        binding.changeEmail.setOnClickListener()
        {

            var currentInfo = userViewModel.userInfo
            binding.apply {
                currentUser = UserInfo(currentInfo.firstName, currentInfo.lastName, editEmail.text.toString(), currentInfo.avatar)
                userViewModel.updateUser(currentUser!!)

                changeEmail.visibility=View.GONE
                cancelEmail.visibility=View.GONE
                editEmail.visibility=View.INVISIBLE
                textEmail.visibility=View.VISIBLE
                val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(changeEmail.windowToken, 0)
                //invalidateAll()
            }
        }

        binding.cancelEmail.setOnClickListener()
        {
            cancelEmail()
        }
        binding.editEmail.setOnFocusChangeListener()
        {
                view:View, b:Boolean->
            if(!b)
            {
                cancelEmail()
            }
        }
        
        binding.uploadImageButton.setOnClickListener({ askStoragePermission() })
        binding.takePictureButton.setOnClickListener({ askCameraPermissionAndOpenCamera()})
        Log.i("MyStuff","OnCreateEnded")
    }

    companion object {
        const val CAMERA_PERMISSION_CODE = 1000
        const val CAMERA_REQUEST_CODE=2001;

        const val READ_STORAGE_PERMISSION_CODE = 3000;
        const val READ_STORAGE_REQUEST_CODE = 4001;

    }

    private fun cancelLastname()
    {
        binding.apply {
            editLastname.setText(textLastname.text.toString())
            cancelLastname.visibility=View.GONE
            changeLastname.visibility=View.GONE
            editLastname.visibility=View.INVISIBLE
            textLastname.visibility=View.VISIBLE
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(cancelLastname.windowToken, 0)
        }

    }
    private fun cancelFirstname()
    {
        binding.apply {
            editFirstname.setText(binding.textFirstname.text.toString())
            cancelFirstname.visibility=View.GONE
            changeFirstname.visibility=View.GONE
            editFirstname.visibility=View.INVISIBLE
            textFirstname.visibility=View.VISIBLE
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(cancelFirstname.windowToken, 0)
        }

    }
    private fun cancelEmail()
    {
        binding.apply {
            editEmail.setText(textEmail.text.toString())
            cancelEmail.visibility=View.GONE
            changeEmail.visibility=View.GONE
            editEmail.visibility=View.INVISIBLE
            textEmail.visibility=View.VISIBLE
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(cancelEmail.windowToken, 0)
        }

    }
    private fun askStoragePermission()
    {
        Toast.makeText(this, "AccessStorage", Toast.LENGTH_LONG).show()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_STORAGE_PERMISSION_CODE )
                Log.i("MyStuff","Checking1")
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), READ_STORAGE_PERMISSION_CODE )
                Log.i("MyStuff","Checking2")
            }
        } else {
            Log.i("MyStuff","Already okay")
            openStorage()
        }
    }

    private fun openStorage() {
        val storageIntent = Intent()
        storageIntent.setType("image/*");
        storageIntent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(storageIntent, "Select Picture"), READ_STORAGE_REQUEST_CODE);
    }


    public  fun askCameraPermissionAndOpenCamera() {

        Log.i("MyStuff","AskCamera")
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE )
                Log.i("MyStuff","Checking1")
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE )
                Log.i("MyStuff","Checking2")
            }
        } else {
            Log.i("MyStuff","Already okay")
            openCamera()
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        Log.i("MyStuff","Intent launched")
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.i("MyStuff","OnRequestPermissionResult")
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Toast.makeText(this, "onRequestPermissionResult :" + CAMERA_PERMISSION_CODE.toString(), Toast.LENGTH_LONG).show()

        when(requestCode)
        {
            CAMERA_PERMISSION_CODE->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    openCamera()
                }
                else
                {
                    Toast.makeText(this, "We need access to your camera to take a picture :'(", Toast.LENGTH_LONG).show()
                }
                return

            }
            READ_STORAGE_PERMISSION_CODE->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    openStorage()
                }
                else
                {
                    Toast.makeText(this, "We need access to your external storage to allow you to choose a picture :'(", Toast.LENGTH_LONG).show()
                }
                return

            }
            else ->{}
        }
    }


    private fun handlePhotoTaken(data: Intent?) {
        val image = data?.extras?.get("data") as? Bitmap
        val imageBody = imageToBody(image)
        Log.i("MyStuff", "about to glide")
        // Afficher l'image
        Glide.with(this).load(image).apply(
            RequestOptions.circleCropTransform()).into(binding.photoView)


        Log.i("MyStuff", "Handled photo ")

        // Plus tard : Envoie de l'avatar au serveur
        if(imageBody!=null)
        {
            Log.i("MyStuff", "Update avatar ?")
            coroutineScope.launch {
                userViewModel.loadUpdateAvatar(imageBody)
            }
        }
        userViewModel.getUserInfos()
    }

    private fun handlePictureStorage(data: Intent?) {

        val selectedImageUri = data?.extras?.get("data") as? Uri

        Glide.with(this).load(data?.data).apply(
            RequestOptions.circleCropTransform()).into(binding.photoView)


        Glide.with(this)
            .asBitmap()
            .load(data?.data)
            .into(object : CustomTarget<Bitmap>(){
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val imageBody = imageToBody(resource)
                    if(imageBody!=null)
                    {
                        userViewModel.updateAvatar(imageBody)
                    }
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })
        userViewModel.getUserInfos()
    }


    // Vous pouvez ignorer cette fonction...
    private fun imageToBody(image: Bitmap?): MultipartBody.Part? {
        val f = File(cacheDir, "tmpfile.jpg")
        f.createNewFile()
        try {
            val fos = FileOutputStream(f)
            image?.compress(Bitmap.CompressFormat.PNG, 100, fos)

            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()

        }

        val body = RequestBody.create(MediaType.parse("image/png"), f)
        return MultipartBody.Part.createFormData("avatar", f.path ,body)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i("MyStuff","Results!!!!")
        if(requestCode==CAMERA_REQUEST_CODE)
        {
            Log.i("MyStuff", "about to handle")
            handlePhotoTaken(data)
        }
        else if(requestCode== READ_STORAGE_REQUEST_CODE) {
            handlePictureStorage(data)
        }

    }

}
