package com.rockstar.saathi.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rockstar.saathi.R
import com.rockstar.saathi.activity.JoinDetailsActivity
import com.rockstar.saathi.activity.NearMeActivity
import com.rockstar.saathi.modal.JoinData

class JoinGroupAdapter(val context: Context,val joinTypeData:ArrayList<JoinData> ,var btnType:String) : RecyclerView.Adapter<JoinGroupAdapter.JoinViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JoinViewHolder {
        val inflater:LayoutInflater= LayoutInflater.from(parent.context)
        var view:View=inflater.inflate(R.layout.layout_joingroup_row,parent,false)
        return JoinViewHolder(view)
    }

    override fun getItemCount(): Int {
        return joinTypeData.size
    }

    override fun onBindViewHolder(holder: JoinViewHolder, position: Int) {

        /*val picasso = Picasso.get()
        picasso.load(joinTypeData.get(position).ivImage)
            .into(holder.ivImage)*/

        holder.ivImage?.let { Glide.with(context).load(joinTypeData.get(position).ivImage).into(it) };

        holder.tvType?.setText(joinTypeData.get(position).tvType)

        holder.cardView?.setOnClickListener(View.OnClickListener {
            if(btnType.equals("JoinGroup")){
                var intent:Intent= Intent(context,JoinDetailsActivity::class.java)
                intent.putExtra("type",joinTypeData.get(position).tvType)
                context.startActivity(intent)
            }else if(btnType.equals("ViewGroup")){
                val intent: Intent = Intent(context, NearMeActivity::class.java)
                intent.putExtra("type","ViewGroup")
                intent.putExtra("user_type",joinTypeData.get(position).tvType)
                context.startActivity(intent)
            }
        })


    }

    class JoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var ivImage:ImageView?=itemView.findViewById(R.id.profile_image)
        var tvType:TextView?=itemView.findViewById(R.id.tv_name_type)
        var cardView:CardView?=itemView.findViewById(R.id.cardview1)


    }
}