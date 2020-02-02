package com.rockstar.saathi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rockstar.saathi.R
import com.rockstar.saathi.modal.userdata.UserData

class NearMeAdapter (val context:Context, val userDataArrayList:ArrayList<UserData>): RecyclerView.Adapter<NearMeAdapter.NearMeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearMeViewHolder {
        val inflater:LayoutInflater= LayoutInflater.from(parent.context)
        val view:View=inflater.inflate(R.layout.layout_nearme_row,parent,false)
        return NearMeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userDataArrayList.size
    }

    override fun onBindViewHolder(holder: NearMeViewHolder, position: Int) {
        holder.tvName.setText(userDataArrayList.get(position).user_name)
        holder.tvPhoneNumber.setText(userDataArrayList.get(position).user_phoneno)
        holder.tvAddress.setText(userDataArrayList.get(position).user_address)
        holder.tvType.setText(userDataArrayList.get(position).user_type)
    }

    public class NearMeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView =itemView.findViewById(R.id.tv_name)
        val tvPhoneNumber:TextView=itemView.findViewById(R.id.tv_phonenumber)
        val tvAddress:TextView=itemView.findViewById(R.id.tv_address)
        val tvType:TextView=itemView.findViewById(R.id.tv_type)
    }

}