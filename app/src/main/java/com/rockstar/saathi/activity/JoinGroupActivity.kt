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
        supportActionBar?.title ="Join Group"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Recycler view.
        rvJoinGroup=findViewById(R.id.rvJoinGroup)
        rvJoinGroup?.layoutManager=LinearLayoutManager(this)
    }

    private fun loadData(){
        val joinData=ArrayList<JoinData>()
        joinData.add(JoinData(R.drawable.ic_account,R.string.address))
        joinData.add(JoinData(R.drawable.ic_account,R.string.address))
        joinData.add(JoinData(R.drawable.ic_account,R.string.address))
        joinData.add(JoinData(R.drawable.ic_account,R.string.address))
        joinData.add(JoinData(R.drawable.ic_account,R.string.address))
        joinData.add(JoinData(R.drawable.ic_account,R.string.address))

        val joinGroupAdapter=JoinGroupAdapter(applicationContext,joinData)
        rvJoinGroup?.adapter=joinGroupAdapter
    }
}
