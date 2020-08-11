package com.saku.approval_sju

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.saku.approval_sju.adapter.PageAdapter
import kotlinx.android.synthetic.main.activity_detail.*


class DetailPengajuanActivity : AppCompatActivity() {
    lateinit var noAju:String
    lateinit var modul:String
    lateinit var displayopt:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        noAju = intent.getStringExtra("no_aju")
        modul = intent.getStringExtra("modul")
        displayopt = intent.getStringExtra("displayopt")

        val viewPager = findViewById<ViewPager>(R.id.viewpager);
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout);
        val pageAdapter =
            PageAdapter(
                supportFragmentManager,
                tabLayout.tabCount
            )
        viewPager.adapter = pageAdapter;
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tabLayout))

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        back_btn.setOnClickListener {
            finish()
        }

        if(displayopt == "history"){
            layout_appv.visibility = View.GONE
        }
        btn_approve.setOnClickListener {
            val intent = Intent(this, ProsesActivity::class.java)
                .apply {
                    putExtra("no_aju", noAju)
                    putExtra("status", "Approve")
                    putExtra("modul", modul)
                }
            startActivity(intent)
        }
        btn_reject.setOnClickListener {
            val intent = Intent(this, ProsesActivity::class.java)
                .apply {
                    putExtra("no_aju", noAju)
                    putExtra("status", "Reject")
                    putExtra("modul", modul)
                }
            startActivity(intent)
        }
    }

}