package com.rockstar.saathi.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.rockstar.saathi.R
import com.rockstar.saathi.modal.userdata.UserData


class EmergencyAdapter (val context: Context, val userDataArrayList:ArrayList<UserData>): RecyclerView.Adapter<EmergencyAdapter.EmergecnyViewHolder>() {

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
            val uri = Uri.parse("smsto:"+userDataArrayList.get(position).user_phoneno)
            val intent = Intent(Intent.ACTION_SENDTO, uri)
            intent.putExtra("sms_body", "I am in emergency please help me ")
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

}