package com.saku.approval_sju

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.saku.approval_sju.adapter.PageAdapter
import kotlinx.android.synthetic.main.activity_detail.*


class DetailPengajuanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
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
    }

}