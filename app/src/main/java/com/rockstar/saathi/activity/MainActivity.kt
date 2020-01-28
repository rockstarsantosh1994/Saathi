package com.rockstar.saathi.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DISPLAY_LENGTH)
    }
}
