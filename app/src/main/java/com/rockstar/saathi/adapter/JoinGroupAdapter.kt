package com.rockstar.saathi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rockstar.saathi.R
import com.rockstar.saathi.modal.JoinData
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class JoinGroupAdapter(val context: Context,val joinTypeData:ArrayList<JoinData> ) : RecyclerView.Adapter<JoinGroupAdapter.JoinViewHolder>() {


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

        holder.tvType?.setText(joinTypeData.get(position).tvType)
    }

    public class JoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivImage:CircleImageView?=null
        var tvType:TextView?=null
    }

}