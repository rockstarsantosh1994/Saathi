package com.rockstar.saathi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import butterknife.BindView
import butterknife.ButterKnife

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    @BindView(R.id.et_mobilenumber)
    var etMobileNumber:EditText?=null
    @BindView(R.id.et_password)
    var etPassword:EditText?=null
    @BindView(R.id.btn_submit)
    var btnSubmit:AppCompatButton?=null
    @BindView(R.id.tv_signup)
    var tvSignUp:AppCompatButton?=null

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
            R.id.btn_submit->{
                if(isValidated()){
                    val intent= Intent(applicationContext,DashBoardActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()
                }
            }

            R.id.tv_signup->{
                val intent= Intent(applicationContext,SignUpActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
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
