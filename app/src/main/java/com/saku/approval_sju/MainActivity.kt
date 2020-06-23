package com.saku.approval_sju

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.saku.approval_sju.fragments.HistoryFragment
import com.saku.approval_sju.fragments.HomeFragment
import com.saku.approval_sju.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

    var periode : String = java.text.SimpleDateFormat("yyyyMM", java.util.Locale.getDefault()).format(java.util.Date())
//    lateinit var layout_toolbar: ActionBar

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            if(intent.hasExtra("filter")){
                periode = intent.getStringExtra("filter")
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(mMessageReceiver, IntentFilter("filter_intent"))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(mMessageReceiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFragment(HomeFragment.newInstance());
//        layout_toolbar = supportActionBar!!
        val bottomNavigation: BottomNavigationView = findViewById(R.id.navigationView)
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

//
//        val layout_toolbar = findViewById<Toolbar>(R.id.layout_toolbar);
//        setSupportActionBar(layout_toolbar);

    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                val homeFragment = HomeFragment.newInstance()
                openFragment(homeFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_history -> {
                val historyFragment = HistoryFragment.newInstance()
                openFragment(historyFragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                val profileFragment = ProfileFragment.newInstance()
                openFragment(profileFragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
//        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun initFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_search_and_filter, menu)
//
//        return true
//    }

    override fun onBackPressed() {
        val backStackEntryCount = supportFragmentManager.backStackEntryCount
        if (backStackEntryCount == 0) {
            val builder = AlertDialog.Builder(this)
            //            builder.setTitle("Peringatan");
            builder.setMessage("Yakin ingin keluar?")
            builder.setCancelable(true)
            builder.setPositiveButton(
                "Ya"
            ) {
                    _, _ -> super@MainActivity.onBackPressed() }
            builder.setNegativeButton(
                "Tidak"
            ) { _, _ ->
                // Do something when want to stay in the app
                //                    Toast.makeText(getApplicationContext(),"thank you",Toast.LENGTH_LONG).show();
            }
            // Create the alert dialog using alert dialog builder
            val dialog = builder.create()
            // Finally, display the dialog when user press back button
            dialog.show()
        } else {
            super.onBackPressed()
        }
        //        }
    }
}
