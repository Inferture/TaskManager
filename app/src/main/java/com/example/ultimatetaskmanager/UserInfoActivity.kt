package com.example.ultimatetaskmanager

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.core.view.drawToBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.ultimatetaskmanager.network.Api
import com.example.ultimatetaskmanager.network.UserInfo
import kotlinx.android.synthetic.main.activity_user_info.*
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

    private val coroutineScope = MainScope()
    private val userViewModel by lazy {
        ViewModelProviders.of(this).get(UserViewModel::class.java)
    }

    var tempString=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        Log.i("MyStuff","create userinfoact")


        /*userViewModel.getPicture().observe(this, Observer {

            if (it != null) {

                Glide.with(this).load(it).apply(
                    RequestOptions.circleCropTransform()).into(photo_view)
            }
        })*/

        userViewModel.getUserInfos().observe(this, Observer {

            if (it != null) {

                userViewModel.userInfo=it;
                text_firstname.setText(it.firstName);
                text_lastname.setText(it.lastName);
                text_email.setText(it.email);

                edit_firstname.setText(it.firstName);
                edit_lastname.setText(it.lastName);
                edit_email.setText(it.email);
                Glide.with(this).load(it.avatar).apply(
                    RequestOptions.circleCropTransform()).into(photo_view)
            }
        })

        Log.i("MyStuff","create stuff")

        
        text_firstname.setOnClickListener()
        {
            edit_firstname.visibility = View.VISIBLE
            change_firstname.visibility = View.VISIBLE
            cancel_firstname.visibility = View.VISIBLE
            it.visibility = View.INVISIBLE
            edit_firstname.requestFocus();
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(edit_firstname, 0)
        }
        change_firstname.setOnClickListener()
        {
            var currentInfo = userViewModel.userInfo
            userViewModel.updateUser(UserInfo(edit_firstname.text.toString(), currentInfo.lastName, currentInfo.email, currentInfo.avatar))
            text_firstname.setText(edit_firstname.text.toString())
            it.visibility=View.GONE
            cancel_firstname.visibility=View.GONE
            edit_firstname.visibility=View.INVISIBLE
            text_firstname.visibility=View.VISIBLE
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)

        }

        cancel_firstname.setOnClickListener()
        {
            cancelFirstname()
        }
        cancel_firstname.setOnClickListener()
        {
            view->cancelFirstname()
        }

        edit_firstname.setOnFocusChangeListener()
        {
            view:View, b:Boolean->
                if(!b)
                {
                    cancelFirstname()
                }
        }




        text_lastname.setOnClickListener()
        {
            edit_lastname.visibility = View.VISIBLE
            change_lastname.visibility = View.VISIBLE
            cancel_lastname.visibility = View.VISIBLE
            it.visibility = View.INVISIBLE
            edit_lastname.requestFocus();
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(edit_firstname, 0)

        }
        
        change_lastname.setOnClickListener()
        {
            var currentInfo = userViewModel.userInfo
            userViewModel.updateUser(UserInfo(currentInfo.firstName, edit_lastname.text.toString(), currentInfo.email, currentInfo.avatar))
            text_lastname.setText(edit_lastname.text.toString())
            it.visibility=View.GONE
            cancel_lastname.visibility=View.GONE
            edit_lastname.visibility=View.INVISIBLE
            text_lastname.visibility=View.VISIBLE
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

        cancel_lastname.setOnClickListener()
        {
            cancelLastname()
        }
        edit_lastname.setOnFocusChangeListener()
        {
                view:View, b:Boolean->
            if(!b)
            {
                cancelLastname()
            }
        }
        
        text_email.setOnClickListener()
        {
            edit_email.visibility = View.VISIBLE
            change_email.visibility = View.VISIBLE
            cancel_email.visibility = View.VISIBLE
            it.visibility = View.INVISIBLE
            edit_email.requestFocus()

            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(edit_email, 0)

        }


        change_email.setOnClickListener()
        {
            var currentInfo = userViewModel.userInfo
            userViewModel.updateUser(UserInfo(currentInfo.firstName, currentInfo.lastName, edit_email.text.toString(), currentInfo.avatar))
            text_email.setText(edit_email.text.toString())
            it.visibility=View.GONE
            cancel_email.visibility=View.GONE
            edit_email.visibility=View.INVISIBLE
            text_email.visibility=View.VISIBLE
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

        cancel_email.setOnClickListener()
        {
            cancelEmail()
        }
        edit_email.setOnFocusChangeListener()
        {
                view:View, b:Boolean->
            if(!b)
            {
                cancelEmail()
            }
        }
        
        upload_image_button.setOnClickListener({ askStoragePermission() })
        take_picture_button.setOnClickListener({ askCameraPermissionAndOpenCamera()})
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
        edit_lastname.setText(text_lastname.text.toString())
        cancel_lastname.visibility=View.GONE
        change_lastname.visibility=View.GONE
        edit_lastname.visibility=View.INVISIBLE
        text_lastname.visibility=View.VISIBLE
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(cancel_lastname.windowToken, 0)
    }
    private fun cancelFirstname()
    {
        edit_firstname.setText(text_firstname.text.toString())
        cancel_firstname.visibility=View.GONE
        change_firstname.visibility=View.GONE
        edit_firstname.visibility=View.INVISIBLE
        text_firstname.visibility=View.VISIBLE
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(cancel_firstname.windowToken, 0)
    }
    private fun cancelEmail()
    {
        edit_email.setText(text_email.text.toString())
        cancel_email.visibility=View.GONE
        change_email.visibility=View.GONE
        edit_email.visibility=View.INVISIBLE
        text_email.visibility=View.VISIBLE
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(cancel_email.windowToken, 0)
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
        startActivityForResult(Intent.createChooser(storageIntent, "Select Picture"), READ_STORAGE_PERMISSION_CODE);
    }


    private fun askCameraPermissionAndOpenCamera() {

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
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
                Log.i("MyStuff","Checking2")
            }
        } else {
            Log.i("MyStuff","Already okay")
            openCamera()
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
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

        // Afficher l'image
        Glide.with(this).load(image).apply(
            RequestOptions.circleCropTransform()).into(photo_view)


        Log.i("MyStuff", "Handled photo ")

        // Plus tard : Envoie de l'avatar au serveur
        if(imageBody!=null)
        {
            Log.i("MyStuff", "Update avatar ?")
            coroutineScope.launch {
                //Api.userService.updateAvatar(imageBody)
                userViewModel.loadUpdateAvatar(imageBody)
            }
        }
    }

    private fun handlePictureStorage(data: Intent?) {

        val selectedImageUri = data?.extras?.get("data") as? Uri

        Glide.with(this).load(data?.data).apply(
            RequestOptions.circleCropTransform()).into(photo_view)

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
        if(requestCode==CAMERA_PERMISSION_CODE)
        {
            handlePhotoTaken(data)
        }
        else if(requestCode== READ_STORAGE_PERMISSION_CODE)
        {
            handlePictureStorage(data)
        }

    }

}
