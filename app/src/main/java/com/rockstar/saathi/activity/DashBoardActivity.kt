package com.rockstar.saathi.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.rockstar.saathi.R

class DashBoardActivity : AppCompatActivity(), View.OnClickListener {

    var llJoinGroup:LinearLayout?=null
    var llViewGroup:LinearLayout?=null
    var llNearMe:LinearLayout?=null
    var llEmergency:LinearLayout?=null
    var llLogout:LinearLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dash_board)

        //Basic intialisation...
        initViews()
    }

    private fun initViews(){

        var toolbar= this.findViewById(R.id.toolbar) as androidx.appcompat.widget.Toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title ="DashBoard"
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Linear layout intialisation....
        llJoinGroup=findViewById(R.id.ll_joingroup)
        llViewGroup=findViewById(R.id.ll_view_group)
        llNearMe=findViewById(R.id.ll_near_me)
        llEmergency=findViewById(R.id.ll_emergency)
        llLogout=findViewById(R.id.ll_logout)
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
                startActivity(intent)
            }

            R.id.ll_emergency ->{
                val intent: Intent = Intent(applicationContext,EmeregencyActivity::class.java)
                startActivity(intent)
            }

            R.id.ll_logout ->{

            }
        }
    }
}
