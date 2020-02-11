package com.rockstar.saathi.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.asmobisoft.digishare.CommonMethods
import com.google.android.gms.location.*
import com.rockstar.saathi.R

class MainActivity : AppCompatActivity() {

    /** Duration of wait **/
    private var SPLASH_DISPLAY_LENGTH:Long=2000;
    private var mDelayHandler: Handler? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         val mRunnable: Runnable = Runnable {
            if (!isFinishing) {
                if(CommonMethods.getPrefrence(applicationContext,CommonMethods.USER_ID).equals("DNF")){
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(applicationContext, DashBoardActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }

        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DISPLAY_LENGTH)


    }
}
