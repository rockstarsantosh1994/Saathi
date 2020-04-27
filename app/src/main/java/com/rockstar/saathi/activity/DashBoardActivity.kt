package com.rockstar.saathi.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.asmobisoft.digishare.CommonMethods
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.rockstar.saathi.R
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.util.*

class DashBoardActivity : AppCompatActivity(), View.OnClickListener {

    var llJoinGroup:LinearLayout?=null
    var llViewGroup:LinearLayout?=null
    var llNearMe:LinearLayout?=null
    var llEmergency:LinearLayout?=null
    var llLogout:LinearLayout?=null
    var tvAddress:TextView?=null
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var TAG:String="DashBoardActivity"
    var city: String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        //Basic intialisation...
        initViews()

        //request permissions
        requestPermissionss()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        //getLast location of user....
        getLastLocation()
    }

    private fun initViews(){

        var toolbar= this.findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)

        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Linear layout intialisation....
        llJoinGroup=findViewById(R.id.ll_joingroup)
        llViewGroup=findViewById(R.id.ll_view_group)
        llNearMe=findViewById(R.id.ll_near_me)
        llEmergency=findViewById(R.id.ll_emergency)
        llLogout=findViewById(R.id.ll_logout)

        tvAddress=findViewById(R.id.tv_address)
        llJoinGroup?.setOnClickListener(this)
        llViewGroup?.setOnClickListener(this)
        llNearMe?.setOnClickListener(this)
        llEmergency?.setOnClickListener(this)
        llLogout?.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ll_joingroup ->{
                val intent: Intent = Intent(applicationContext,JoinGroupActivity::class.java)
                intent.putExtra("type","JoinGroup")
                startActivity(intent)
            }

            R.id.ll_view_group ->{
                val intent: Intent = Intent(applicationContext,JoinGroupActivity::class.java)
                intent.putExtra("type","ViewGroup")
                startActivity(intent)
            }

            R.id.ll_near_me ->{
                val intent: Intent = Intent(applicationContext,NearMeActivity::class.java)
                intent.putExtra("type","NearMe")
                startActivity(intent)
            }

            R.id.ll_emergency ->{
                val intent: Intent = Intent(applicationContext,EmeregencyActivity::class.java)
                startActivity(intent)
            }

            R.id.ll_logout ->{
                val builder = AlertDialog.Builder(this)
                //set title for alert dialog
                builder.setTitle(R.string.app_name)
                //set message for alert dialog
                builder.setMessage("Are you sure want to logout")
                builder.setIcon(android.R.drawable.ic_dialog_alert)

                //performing positive action
                builder.setPositiveButton("Yes"){dialogInterface, which ->
                    CommonMethods.setPreference(applicationContext,CommonMethods.USER_ID,"DNF")
                    val intent: Intent = Intent(applicationContext,LoginActivity::class.java)
                    intent.putExtra("type","JoinGroup")
                    startActivity(intent)
                    finish()
                    Toast.makeText(applicationContext,"See you again!",Toast.LENGTH_LONG).show()
                }

                //performing negative action
                builder.setNegativeButton("No"){dialogInterface, which ->

                }
                // Create the AlertDialog
                val alertDialog: AlertDialog = builder.create()
                // Set other dialog properties
                alertDialog.setCancelable(false)
                alertDialog.show()
            }

        }
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        //findViewById<TextView>(R.id.latTextView).text = location.latitude.toString()
                        //findViewById<TextView>(R.id.lonTextView).text = location.longitude.toString()
                        Log.e(TAG,"latitude"+location.latitude.toString())
                        Log.e(TAG,"longitude"+location.longitude.toString())

                        getAddressFromLatLong(location.latitude.toString(),location.longitude.toString())

                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent,200)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation

            Log.e(TAG,"last location latitude"+mLastLocation.latitude.toString())
            Log.e(TAG,"last location longitude"+mLastLocation.longitude.toString())
            getAddressFromLatLong(mLastLocation.latitude.toString(),mLastLocation.longitude.toString())
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==200){
            //getting location of user...
            getLastLocation()
        }
    }

    private fun getAddressFromLatLong(latitude:String,longitude:String){
        try{
        var geocoder: Geocoder? = null
        var addresses: List<Address>? = null
        geocoder = Geocoder(applicationContext, Locale.getDefault())
        addresses = geocoder.getFromLocation(latitude.toDouble(), longitude.toDouble(), 1)
        val address: String = addresses.get(0).getAddressLine(0)
        city= addresses.get(0).locality
        val knownname = addresses.get(0).featureName

        supportActionBar?.title ="DashBoard ($city)"
        tvAddress?.setText(address)
        CommonMethods.setPreference(applicationContext,CommonMethods.CITY_NAME,city)
        CommonMethods.setPreference(applicationContext,CommonMethods.LAT,latitude)
        CommonMethods.setPreference(applicationContext,CommonMethods.LONG,longitude)

        Log.e(TAG,"address name $address")
        Log.e(TAG,"city name $city")
        Log.e(TAG,"knownname name $knownname")

        }catch (e: InvocationTargetException){
            e.printStackTrace()
        }catch (e:IOException){
            e.printStackTrace()
        }
    }

    private fun requestPermissionss(){
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.CALL_PHONE,
                Manifest.permission.SEND_SMS,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) { /* ... */
                }
            }).check()
    }
}
