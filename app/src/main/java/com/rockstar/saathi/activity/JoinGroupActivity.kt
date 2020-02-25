package com.rockstar.saathi.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rockstar.saathi.R
import com.rockstar.saathi.adapter.JoinGroupAdapter
import com.rockstar.saathi.modal.JoinData

class JoinGroupActivity : AppCompatActivity() {

    var rvJoinGroup:RecyclerView?=null
    var TAG:String?="JoinGroupActivity"
    var joinData:ArrayList<JoinData>?=ArrayList<JoinData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join_group)

        //Basic intialisation...
        initViews()

        //load Data to recycler view...
        loadData()
    }

    private fun initViews(){
        //toolbar intialisation....
        var toolbar= this.findViewById(R.id.toolbar_joingroup) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        if(intent.getStringExtra("type").equals("JoinGroup")){
            supportActionBar?.title ="Join Group"
        }else if(intent.getStringExtra("type").equals("ViewGroup")){
            supportActionBar?.title ="View Group"
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Recycler view.
        rvJoinGroup=findViewById(R.id.rvJoinGroup)
        rvJoinGroup?.layoutManager=LinearLayoutManager(this)
    }

    private fun loadData(){
        //joinData=ArrayList()
        joinData!!.add(JoinData(R.drawable.ic_fire_extinguisher,"Garage"))
        joinData!!.add(JoinData(R.drawable.ic_ambulance,"Panic"))
        joinData!!.add(JoinData(R.drawable.ic_doctor,"Doctor"))
        joinData!!.add(JoinData(R.drawable.ic_policeman,"Police"))

        var joinGroupAdapter = this?.let { JoinGroupAdapter(it, joinData!!,intent.getStringExtra("type")) }
        rvJoinGroup!!.adapter=joinGroupAdapter
    }
}
