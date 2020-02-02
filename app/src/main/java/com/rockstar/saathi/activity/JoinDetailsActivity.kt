package com.rockstar.saathi.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.rockstar.saathi.R
import com.rockstar.saathi.modal.CommonResponse

class JoinDetailsActivity : AppCompatActivity(), View.OnClickListener {

    var etUserName: EditText? = null
    var etMobileNumber: EditText? = null
    var etAddress: EditText? = null
    var btnSubmit: AppCompatButton? = null
    val TAG:String?="JoinDetailsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_details)

        //basic intialisation...
        initViews()
    }

    private fun initViews() {
        etUserName = findViewById(R.id.et_username)
        etMobileNumber = findViewById(R.id.et_mobilenumber)
        etAddress = findViewById(R.id.et_address)
        btnSubmit = findViewById(R.id.btn_submit)
        btnSubmit?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_submit -> {
                if (isValidated()) {
                    addJoineeDetails()
                }
            }
        }
    }

    private fun addJoineeDetails() {
        val stringRequest: StringRequest = object : StringRequest(Request.Method.POST, "https://ladiesapp.000webhostapp.com/satthi_app_api/user_signup.php", Response.Listener {
                response->
                Log.e(TAG,"response $response")

                val gson= Gson()

                val commonResponse=gson.fromJson(response,CommonResponse::class.java)

                if(commonResponse.Responsecode.equals("200")){
                    val intent: Intent = Intent(applicationContext,DashBoardActivity::class.java)
                    startActivity(intent)
                    finish()

                    Toast.makeText(this@JoinDetailsActivity,"message:-"+commonResponse.Message,Toast.LENGTH_LONG).show()
                }else{

                    Toast.makeText(this@JoinDetailsActivity,"message:- "+commonResponse.Message,Toast.LENGTH_LONG).show()

                }

            },
            Response.ErrorListener {
                Log.e(TAG,"error $it")
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                 val params=HashMap<String,String>()
                    params.put("user_name",etUserName?.text.toString())
                    params.put("user_phoneno",etMobileNumber?.text.toString())
                    params.put("user_address",etAddress?.text.toString())
                    params.put("user_location",etAddress?.text.toString())
                    params.put("user_type",intent.getStringExtra("type"))
                    params.put("user_password","")

                    Log.e(TAG,"getParams $params")
                return params
            }
        }
        val mQueue = Volley.newRequestQueue(this)
        mQueue.add(stringRequest)
    }

    private fun isValidated(): Boolean {
        var mobileno: String = etMobileNumber?.text.toString()
        if (etUserName?.text.toString().isEmpty()) {
            etUserName?.setError("Name required!")
            etUserName?.requestFocus()
            return false
        }


        if (etMobileNumber?.text.toString().isEmpty()) {
            etMobileNumber?.setError("Mobile Number required!")
            etMobileNumber?.requestFocus()
            return false
        }
        if (mobileno?.length < 10) {
            etMobileNumber?.setError("Invalid mobile number!")
            etMobileNumber?.requestFocus()
            return false
        }

        if (mobileno.startsWith("1") || mobileno.startsWith("2") || mobileno.startsWith("3") ||
            mobileno.startsWith("4") || mobileno.startsWith("5")
        ) {
            etMobileNumber?.setError("Invalid mobile number!")
            etMobileNumber?.requestFocus()
            return false
        }

        if (etAddress?.text.toString().isEmpty()) {
            etAddress?.setError("Address required!")
            etAddress?.requestFocus()
            return false
        }
        return true
    }
}
