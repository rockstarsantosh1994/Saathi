package com.rockstar.saathi.activity

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.rockstar.saathi.R
import com.rockstar.saathi.adapter.NearMeAdapter
import com.rockstar.saathi.modal.userdata.UserResponse
import java.io.StringReader

class NearMeActivity : AppCompatActivity() {

    var rvNearMe: RecyclerView?=null
    var TAG:String?="JoinGroupActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_near_me)


        //Basic intialisation...
        initViews()

        //load Data to recycler view...
        loadData()
    }

    private fun initViews(){
        //toolbar intialisation....
        var toolbar= this.findViewById(R.id.toolbar_nearme) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title ="Near Me"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Recycler view.
        rvNearMe=findViewById(R.id.rvNearMe)
        rvNearMe?.layoutManager= LinearLayoutManager(this)
    }

    private fun loadData(){
        val stringRequest:StringRequest= object:StringRequest(Request.Method.POST,"https://ladiesapp.000webhostapp.com/satthi_app_api/getallusers.php",Response.Listener {
            response->

            val gson: Gson = Gson()

            val userDataResponse=gson.fromJson(response,UserResponse::class.java)
            if(userDataResponse.Responsecode.equals("200")){
                val nearMeAdapter=NearMeAdapter(applicationContext,userDataResponse.Data)
                rvNearMe?.adapter=nearMeAdapter
            }
        },Response.ErrorListener {
            Log.e(TAG,"error Listener $it")
        }){

        }
        val mQueue=Volley.newRequestQueue(this)
        mQueue.add(stringRequest)
    }
}
