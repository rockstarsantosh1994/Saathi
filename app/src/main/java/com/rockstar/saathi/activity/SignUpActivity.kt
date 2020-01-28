package com.rockstar.saathi.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.rockstar.saathi.R

class SignUpActivity : AppCompatActivity() , View.OnClickListener {

    var etUserName: EditText?=null
    var etMobileNumber:EditText?=null
    var etAddress:EditText?=null
    var etPassword:EditText?=null
    var btnSignUp:AppCompatButton?=null
    var tvLogin: TextView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        //Basic intialisation....
        initViews()
    }

    private fun initViews(){
        //Edittext intialisation..
        etUserName=findViewById(R.id.et_username)
        etMobileNumber=findViewById(R.id.et_mobilenumber)
        etAddress=findViewById(R.id.et_address)
        etPassword=findViewById(R.id.et_password)

        //Button intialisation..
        btnSignUp=findViewById(R.id.btn_signup)
        btnSignUp?.setOnClickListener(this)

        //TextView intialisation....
        tvLogin=findViewById(R.id.tv_login)
        tvLogin?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_signup ->{
                if(isValidated()) {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }

            R.id.tv_signup ->{

                    val intent= Intent(applicationContext,
                        LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    finish()

            }
        }
    }

    private fun isValidated():Boolean{

        var mobileno: String = etMobileNumber?.text.toString()

        if(etUserName?.text.toString().isEmpty()){
            etUserName?.setError("Name required!")
            etUserName?.requestFocus()
            return false
        }

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

        if(etAddress?.text.toString().isEmpty()){
            etMobileNumber?.setError("Address required!")
            etMobileNumber?.requestFocus()
            return false
        }

        if (mobileno.startsWith("1") || mobileno.startsWith("2") || mobileno.startsWith("3") ||
            mobileno.startsWith("4") || mobileno.startsWith("5")) {
            etMobileNumber?.setError("Invalid mobile number!")
            etMobileNumber?.requestFocus()
            return false
        }

        return true;
    }
}
