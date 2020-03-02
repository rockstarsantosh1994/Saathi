package com.rockstar.saathi.activity

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
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
import com.asmobisoft.digishare.CommonMethods
import com.google.gson.Gson
import com.rockstar.saathi.R
import com.rockstar.saathi.modal.CommonResponse

class JoinDetailsActivity : AppCompatActivity(), View.OnClickListener {

    var etUserName: EditText? = null
    var etMobileNumber: EditText? = null
    var etAddress: EditText? = null
    var btnSubmit: AppCompatButton? = null
    var btnGetContacts: AppCompatButton? = null
    val TAG:String?="JoinDetailsActivity"

    companion object {
        val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }


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
        btnGetContacts = findViewById(R.id.btn_getcontact)
        btnSubmit?.setOnClickListener(this)
        btnGetContacts?.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_submit -> {
                if (isValidated()) {
                    if(CommonMethods.isNetworkAvailable(this)){
                        addJoineeDetails()
                    }else{
                        Toast.makeText(applicationContext,"No Network available",Toast.LENGTH_SHORT).show()
                    }
                }
            }

            R.id.btn_getcontact ->{
               // loadContacts()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                        Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                        PERMISSIONS_REQUEST_READ_CONTACTS)
                    //callback onRequestPermissionsResult
                } else {
                    val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                    startActivityForResult(intent, 1)
                }

            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                val intent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
                startActivityForResult(intent, 1)

            } else {
                //  toast("Permission must be granted in order to display contacts information")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val contactData = data!!.data
            val c = contentResolver.query(contactData!!, null, null, null, null)
            if (c!!.moveToFirst()) {

                var phoneNumber = ""
                var emailAddress = ""
                val name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))
                //http://stackoverflow.com/questions/866769/how-to-call-android-contacts-list   our upvoted answer

                var hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (hasPhone.equals("1", ignoreCase = true))
                    hasPhone = "true"
                else
                    hasPhone = "false"

                if (java.lang.Boolean.parseBoolean(hasPhone)) {
                    val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null)
                    while (phones!!.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                    phones.close()
                }

                // Find Email Addresses
                val emails = contentResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId, null, null)
                while (emails!!.moveToNext()) {
                    emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA))
                }
                emails.close()

                //mainActivity.onBackPressed();
                // Toast.makeText(mainactivity, "go go go", Toast.LENGTH_SHORT).show();

                //etUserName!!.text = name.toString()
               // Toast.makeText(applicationContext,"name"+name.toString()+"\n phoneumber"+phoneNumber.toString(),Toast.LENGTH_SHORT).show()

                etUserName!!.setText(name.toString())
                //etMobileNumber!!.text = "Phone: " + phoneNumber
                etMobileNumber!!.setText(phoneNumber.toString())
                //tvmail!!.text = "Email: " + emailAddress
                Log.e("curs", "$name num$phoneNumber mail$emailAddress")
            }
            c.close()
        }
    }

    private fun addJoineeDetails() {
        val progressDialog = ProgressDialog(this@JoinDetailsActivity)
        progressDialog.setMessage("Please wait...")
        progressDialog.setTitle("Saathi")
        progressDialog.show()
        progressDialog.setCancelable(false)
        val stringRequest: StringRequest = object : StringRequest(Request.Method.POST, "https://ladiesapp.000webhostapp.com/satthi_app_api/user_signup.php", Response.Listener {
                response->
                Log.e(TAG,"response $response")

                val gson= Gson()

                val commonResponse=gson.fromJson(response,CommonResponse::class.java)

                if(commonResponse.Responsecode.equals("200")){
                    progressDialog.dismiss()
                    val intent: Intent = Intent(applicationContext,DashBoardActivity::class.java)
                    startActivity(intent)
                    finish()

                    Toast.makeText(this@JoinDetailsActivity,"message:-"+commonResponse.Message,Toast.LENGTH_LONG).show()
                }else{
                    progressDialog.dismiss()
                    Toast.makeText(this@JoinDetailsActivity,"message:- "+commonResponse.Message,Toast.LENGTH_LONG).show()

                }

            },
            Response.ErrorListener {
                progressDialog.dismiss()
                Log.e(TAG,"error $it")
            }) {
            @Throws(AuthFailureError::class)
            override fun getParams(): MutableMap<String, String> {
                 val params=HashMap<String,String>()
                    params.put("user_name",etUserName?.text.toString())
                    params.put("user_phoneno",etMobileNumber?.text.toString())
                    params.put("user_address",etAddress?.text.toString())
                    params.put("user_location",CommonMethods.getPrefrence(applicationContext,CommonMethods.CITY_NAME).toString())
                    params.put("user_type",intent.getStringExtra("type"))
                    params.put("user_password","")
                    params.put("gcm_token",CommonMethods.getPrefrence(this@JoinDetailsActivity,CommonMethods.GCM_TOKEN).toString())

                Log.e(TAG,"getParams $params")
                return params
            }
        }
        val mQueue = Volley.newRequestQueue(this)
        mQueue.add(stringRequest)
    }

    /*private fun loadContacts() {
        var builder = StringBuilder()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS)
            //callback onRequestPermissionsResult
        } else {
            builder = getContacts()

            //etMobileNumber?.text = builder.toString()
            etMobileNumber?.setText(builder.toString())
            Log.e(TAG,"builder String"+ builder.toString())
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
                                            grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                //  toast("Permission must be granted in order to display contacts information")
            }
        }
    }

    private fun getContacts(): StringBuilder {
        val builder = StringBuilder()
        val resolver: ContentResolver = contentResolver;
        val cursor = resolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,
            null)

        if (cursor != null) {
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNumber = (cursor.getString(
                        cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))).toInt()

                    if (phoneNumber > 0) {
                        val cursorPhone = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?", arrayOf(id), null)

                        if (cursorPhone != null) {
                            if(cursorPhone.count > 0) {
                                while (cursorPhone.moveToNext()) {
                                    val phoneNumValue = cursorPhone.getString(
                                        cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                                    builder.append("Contact: ").append(name).append(", Phone Number: ").append(
                                        phoneNumValue).append("\n\n")
                                    Log.e("Name ===>",phoneNumValue);
                                }
                            }
                        }
                        cursorPhone!!.close()
                    }
                }
            } else {
                //   toast("No contacts available!")
            }
        }
        cursor!!.close()
        return builder
    }
*/
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
