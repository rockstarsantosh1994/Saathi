package com.rockstar.saathi.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import butterknife.BindView
import butterknife.ButterKnife
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.asmobisoft.digishare.CommonMethods
import com.google.gson.Gson
import com.rockstar.saathi.R
import com.rockstar.saathi.modal.CommonResponse
import com.rockstar.saathi.modal.login.LoginResponse

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    @BindView(R.id.et_mobilenumber)
    var etMobileNumber:EditText?=null
    @BindView(R.id.et_password)
    var etPassword:EditText?=null
    @BindView(R.id.btn_submit)
    var btnSubmit:AppCompatButton?=null
    @BindView(R.id.tv_signup)
    var tvSignUp: TextView?=null
    val TAG:String?="LoginActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        //Basic intialisation....
        initViews()
    }

    private fun initViews(){
        //EditText intialisation...
        etMobileNumber=findViewById(R.id.et_mobilenumber)
        etPassword=findViewById(R.id.et_password)

        //TextView intialisation
        tvSignUp=findViewById(R.id.tv_signup)
        tvSignUp?.setOnClickListener(this)

        //button intialisation...
        btnSubmit=findViewById(R.id.btn_submit)
        btnSubmit?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_submit ->{
                if(isValidated()){
                    if(CommonMethods.isNetworkAvailable(this)){
                        userAuthentication()
                    }else{
                        Toast.makeText(applicationContext,"No Network available",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            R.id.tv_signup ->{
                val intent= Intent(applicationContext,
                    SignUpActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
    }

    private fun userAuthentication() {
        val progressDialog = ProgressDialog(this@LoginActivity)
        progressDialog.setMessage("Please wait...")
        progressDialog.setTitle("Saathi")
        progressDialog.show()
        progressDialog.setCancelable(false)
        val stringRequest: StringRequest = object : StringRequest(
            Request.Method.POST, "https://ladiesapp.000webhostapp.com/satthi_app_api/login.php", Response.Listener {
                    response->
                Log.e(TAG,"response $response")

                val gson= Gson()

                val loginResponse=gson.fromJson(response, LoginResponse::class.java)

                if(loginResponse.Responsecode.equals("200")){
                    progressDialog.dismiss()
                    CommonMethods.setPreference(applicationContext,CommonMethods.USER_ID,loginResponse.Data.user_id)
                    val intent= Intent(applicationContext,
                        DashBoardActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

                    Toast.makeText(this@LoginActivity,""+loginResponse.Message,
                        Toast.LENGTH_LONG).show()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@LoginActivity," "+loginResponse.Message,
                        Toast.LENGTH_LONG).show()

                }

            },
            Response.ErrorListener {
                Log.e(TAG,"error $it")
                progressDialog.dismiss()
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                val params=HashMap<String,String>()
                params.put("user_phoneno",etMobileNumber?.text.toString())
                params.put("user_password",etPassword?.text.toString())

                Log.e(TAG,"getParams $params")
                return params
            }
        }
        val mQueue = Volley.newRequestQueue(this)
        mQueue.add(stringRequest)
    }

    private fun isValidated():Boolean{

        var mobileno: String = etMobileNumber?.text.toString()
        if(etMobileNumber?.text.toString().isEmpty()){
             etMobileNumber?.setError("Mobile Number required!")
             etMobileNumber?.requestFocus()
             return false
        }

        if(etPassword?.text.toString().isEmpty()){
            etPassword?.setError("Password required1")
            etPassword?.requestFocus()
            return false
        }

        if(mobileno?.length<10){
            etMobileNumber?.setError("Invalid mobile number!")
            etMobileNumber?.requestFocus()
            return false
        }

        if (mobileno.startsWith("1") || mobileno.startsWith("2") || mobileno.startsWith("3") ||
            mobileno.startsWith("4") || mobileno.startsWith("5")) {
            etMobileNumber?.setError("Invalid mobile number!")
            etMobileNumber?.requestFocus()
            return false
        }
        return true
    }
}
