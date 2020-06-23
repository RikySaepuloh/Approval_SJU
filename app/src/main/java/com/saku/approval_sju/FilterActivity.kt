package com.saku.approval_sju

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.saku.approval_sju.api_service.UtilsApi
import com.saku.approval_sju.fragments.HomeFragment
import kotlinx.android.synthetic.main.activity_filter.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList


class FilterActivity : AppCompatActivity() {
    var preferences  = Preferences()
//    var myadapter : FilterAdapter? = null
//    var dbhandler : FilterHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        setSupportActionBar(toolbar)
        preferences.setPreferences(this)

        initData()


//        dbhandler =
//            FilterHandler(this, null, null, 1)
//        initData()



        back_btn.setOnClickListener {
            finish()
        }
        btn_filter_reset.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putString("periode", java.text.SimpleDateFormat("yyyyMM", java.util.Locale.getDefault()).format(java.util.Date()))
//            val fragobj = HomeFragment()
//            fragobj.arguments = bundle

//            dbhandler!!.resetFilter()
//            initData()
        }
        btn_submit_filter.setOnClickListener {
            val mydata = Intent("filter_intent")
            mydata.putExtra("filter", spinner.selectedItem.toString())
            LocalBroadcastManager.getInstance(this).sendBroadcast(mydata)
//            val bundle = Bundle()
//            bundle.putString("periode", spinner.selectedItem.toString())
//            val fragobj = HomeFragment()
//            fragobj.arguments = bundle
//            (activity as MainActivity).periode
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
            finish()
        }

//        Toast.makeText(this@FilterActivity, "Filter will be included in the next version. In Syaa Allaah.", Toast.LENGTH_LONG).show()

    }

    private fun initData() {
        val apiservice = UtilsApi().getAPIService(this@FilterActivity)
        apiservice?.periode()?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            val obj = JSONObject(response.body()!!.string())
                            val dataobj: JSONObject = obj.getJSONObject("success")
                            val data = dataobj.optJSONArray("data")
                            val spinnerArray: MutableList<String> = ArrayList()
                            try {
                                for (i in 0 until data!!.length()) {
                                    val didi: JSONObject = data.getJSONObject(i)
                                    spinnerArray.add(didi.optString("periode"))
                                }
                            } catch (e: Exception) {
                            }
                            val adapter = ArrayAdapter(
                                this@FilterActivity, android.R.layout.simple_spinner_item, spinnerArray
                            )

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinner.adapter = adapter


                        } catch (e: Exception) {

                        }
                    }else{
                        Toast.makeText(this@FilterActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if(response.code() == 422) {
                    Toast.makeText(this@FilterActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 401){
                    val intent = Intent(this@FilterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    preferences.preferencesLogout()
                    finishAffinity()
                    Toast.makeText(this@FilterActivity, "Sesi telah berakhir, silahkan login kembali", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 403){
                    Toast.makeText(this@FilterActivity, "Unauthorized", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 404){
                    Toast.makeText(this@FilterActivity, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@FilterActivity, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }

//    private fun initData() {
//        val apiservice = UtilsApi().getAPIService(applicationContext)
//        apiservice?.daftarpengajuan(periode)?.enqueue(object : Callback<ResponseBody?> {
//            override fun onResponse(
//                call: Call<ResponseBody?>,
//                response: Response<ResponseBody?>
//            ) {
//                if (response.isSuccessful) {
//                    if (response.body() != null) {
//                        try {
//                            val obj = JSONObject(response.body()!!.string())
//                            val dataobj: JSONObject = obj.getJSONObject("success")
//
//                            val gson = Gson()
//                            val type: Type = object :
//                                TypeToken<ArrayList<ModelDataPengajuan?>?>() {}.type
//                            val datapengajuan: ArrayList<ModelDataPengajuan> =
//                                gson.fromJson(dataobj.optString("data"), type)
//
//                            val kodepptmpr: HashSet<String> = HashSet()
//                            val modultmpr: HashSet<String> = HashSet()
//
//                            for (data in datapengajuan) {
//                                kodepptmpr.add(data.pp.toString())
//                                modultmpr.add(data.modul.toString())
//    //                            Log.i(
//    //                                "Data Pengajuan",
//    //                                data.no_dokumen.toString() + "-" + data.pembuat + "-" + data.due_date
//    //                            )
//                            }
//                            val pp = ArrayList(kodepptmpr)
//                            val modul = ArrayList(modultmpr)
//
//                            myadapter =
//                                FilterAdapter(
//                                    pp,
//                                    "PP"
//                                )
//                            gv_pp.layoutManager = GridLayoutManager(this@FilterActivity,3)
//                            gv_pp.adapter = myadapter
//                            myadapter =
//                                FilterAdapter(
//                                    modul,
//                                    "Modul"
//                                )
//                            gv_modul.layoutManager = GridLayoutManager(this@FilterActivity,3)
//                            gv_modul.adapter = myadapter
//                        } catch (e: Exception) {
//                            empty_view.visibility=View.VISIBLE
//                            tv_modul.visibility=View.GONE
//                            tv_pp.visibility= View.GONE
//                            gv_modul.visibility=View.GONE
//                            gv_pp.visibility= View.GONE
//                        }
////                        rv_pengajuan.adapter = myadapter
//                    }else{
//                        //                        if (data.length() > 0) {
//                        Toast.makeText(this@FilterActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
//                    }
//                } else if(response.code() == 422) {
//                    Toast.makeText(this@FilterActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
//                } else if(response.code() == 401){
//                    val intent = Intent(this@FilterActivity, LoginActivity::class.java)
//                    startActivity(intent)
//                    preferences.preferencesLogout()
//                    finish()
//                    Toast.makeText(this@FilterActivity, "Sesi telah berakhir, silahkan login kembali", Toast.LENGTH_SHORT).show()
//                } else if(response.code() == 403){
//                    Toast.makeText(this@FilterActivity, "Unauthorized", Toast.LENGTH_SHORT).show()
//                } else if(response.code() == 404){
//                    Toast.makeText(this@FilterActivity, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
//                Toast.makeText(this@FilterActivity, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }



}