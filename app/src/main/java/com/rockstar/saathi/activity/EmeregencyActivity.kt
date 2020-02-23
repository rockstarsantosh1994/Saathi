package com.rockstar.saathi.activity

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.asmobisoft.digishare.CommonMethods
import com.google.gson.Gson
import com.rockstar.saathi.R
import com.rockstar.saathi.adapter.EmergencyAdapter
import com.rockstar.saathi.modal.userdata.UserResponse

class EmeregencyActivity : AppCompatActivity() {

    var rvEmergency: RecyclerView?=null
    var TAG:String?="EmeregencyActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emeregency)

        //Basic intialisation...
        initViews()

        //load Data to recycler view...
        if(CommonMethods.isNetworkAvailable(this)){
            loadData()
        }else{
            Toast.makeText(applicationContext,"No Network available", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun initViews(){
        //toolbar intialisation....
        var toolbar= this.findViewById(R.id.toolbar_emergency) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title ="Emergency"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Recycler view.
        rvEmergency=findViewById(R.id.rvEmergency)
        rvEmergency?.layoutManager= LinearLayoutManager(this)
    }

    private fun loadData(){
        val progressDialog = ProgressDialog(this@EmeregencyActivity)
        progressDialog.setTitle("Saathi")
        progressDialog.show()
        progressDialog.setCancelable(false)
        val stringRequest: StringRequest = object: StringRequest(
            Request.Method.POST,"https://ladiesapp.000webhostapp.com/satthi_app_api/getallusers.php",
            Response.Listener {
                    response->
                val gson: Gson = Gson()
                val userDataResponse=gson.fromJson(response, UserResponse::class.java)
                if(userDataResponse.Responsecode.equals("200")){
                    progressDialog.dismiss()
                    if(userDataResponse.Data!=null){
                        val emergencyAdapter= EmergencyAdapter(applicationContext,userDataResponse.Data)
                        rvEmergency?.adapter=emergencyAdapter
                    }
                }else{
                    progressDialog.dismiss()
                }
            },
            Response.ErrorListener {
                progressDialog.dismiss()
                Log.e(TAG,"error Listener $it")
            }){
            override fun getParams(): MutableMap<String, String> {
                    val params=HashMap<String,String>()
                    params.put("user_location",CommonMethods.getPrefrence(applicationContext,CommonMethods.CITY_NAME).toString())
                    return params;
                }

        }
        val mQueue= Volley.newRequestQueue(this)
        mQueue.add(stringRequest)
    }
}
