package com.rockstar.saathi.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.asmobisoft.digishare.CommonMethods
import com.rockstar.saathi.R
import com.rockstar.saathi.activity.EmeregencyActivity
import com.rockstar.saathi.modal.userdata.UserData
import org.json.JSONObject


class EmergencyAdapter (var context: Context, val userDataArrayList:ArrayList<UserData>): RecyclerView.Adapter<EmergencyAdapter.EmergecnyViewHolder>() {

    private val URL = "https://fcm.googleapis.com/fcm/send"
    private var mRequestQue: RequestQueue?=null
    private val TAG:String="EmergencyAdapter"


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmergecnyViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View =inflater.inflate(R.layout.layout_emergency_row,parent,false)
        return EmergecnyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userDataArrayList.size
    }

    override fun onBindViewHolder(holder: EmergecnyViewHolder, position: Int) {
        holder.tvName.setText(userDataArrayList.get(position).user_name)
        holder.tvPhoneNumber.setText(userDataArrayList.get(position).user_phoneno)
        holder.tvAddress.setText(userDataArrayList.get(position).user_address)
        holder.tvType.setText(userDataArrayList.get(position).user_type)

        holder.llCall.setOnClickListener(View.OnClickListener {
            var intent = Intent(Intent.ACTION_DIAL);
            intent.data = Uri.parse("tel:"+userDataArrayList.get(position).user_phoneno)
            context.startActivity(intent)
        })

        holder.llMessage.setOnClickListener(View.OnClickListener {

            var message="My Location is http://maps.google.com/maps?f=d&daddr="+CommonMethods.getPrefrence(context,CommonMethods.LAT)+","+CommonMethods.getPrefrence(context,CommonMethods.LONG)

            sendNotification(message,userDataArrayList.get(position).gcm_token)

            val uri = Uri.parse("smsto:"+userDataArrayList.get(position).user_phoneno)
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", message)
            context.startActivity(intent)
        })
    }

    class EmergecnyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvName: TextView =itemView.findViewById(R.id.tv_name)
        val tvPhoneNumber: TextView =itemView.findViewById(R.id.tv_phonenumber)
        val tvAddress: TextView =itemView.findViewById(R.id.tv_address)
        val tvType: TextView =itemView.findViewById(R.id.tv_type)
        val llCall:LinearLayout=itemView.findViewById(R.id.ll_call)
        val llMessage: LinearLayout =itemView.findViewById(R.id.ll_message)
    }

        fun sendNotification( message:String,gcm_token:String){

            Log.e(TAG,"insendnotification")
            val notificationData= JSONObject()

            val notification= JSONObject()
            try{
                notificationData.put("title","Saathi")
                notificationData.put("message",message)


                //notification.put("to","fXEZoWEeBNQ:APA91bFfNgb3mh8iQ70ePYyIhFfKpEW-QG10K4aaexZS_0bCzu_2Y9W50f3h2LfKz0C_kVSFt2hwQ_GioizXT2ZF43C9JnAXnKk_53_nDp_AKmSXtk4P-5mC-KdkBBHOVYxfAMU9vBa4")
                //notification.put("to",CommonMethods.getPrefrence(applicationContext,AllKeys.FCM_TOKEN))
                notification.put("to",gcm_token)
                notification.put("data",notificationData)

                Log.e(TAG,"Gcm TOken" +gcm_token)


                val request = object: JsonObjectRequest(
                    Request.Method.POST, URL, notification,
                    object: Response.Listener<JSONObject> {
                        override fun onResponse(response:JSONObject) {
                            Log.e(TAG, "onResponse: "+response)
                        }
                    }, object: Response.ErrorListener {
                        override fun onErrorResponse(error: VolleyError) {
                            Log.e(TAG, "onError: " + error.networkResponse)
                        }
                    }
                ) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders():Map<String, String> {
                        val params = HashMap<String, String>()
                        params.put("content-type","application/json")
                        params.put("authorization","key=AIzaSyA4jykyiACgaY76UDsIE6ku_0j4tXzBUis")

                        Log.e(TAG, "params " +params)
                        return params
                    }
                }
                mRequestQue= Volley.newRequestQueue(context)
                mRequestQue?.add(request)

            }catch (e:Exception){
                e.printStackTrace()
            }
        }

}