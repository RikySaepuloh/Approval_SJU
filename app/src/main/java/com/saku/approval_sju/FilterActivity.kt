package com.saku.approval_sju

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saku.approval_sju.adapter.FilterAdapter
import com.saku.approval_sju.api_service.UtilsApi
import com.saku.approval_sju.database.FilterHandler
import com.saku.approval_sju.models.ModelDataPengajuan
import kotlinx.android.synthetic.main.activity_filter.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class FilterActivity : AppCompatActivity() {
    var preferences  = Preferences()
    var myadapter : FilterAdapter? = null
    var dbhandler : FilterHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)
        setSupportActionBar(toolbar)
        preferences.setPreferences(this)
        dbhandler =
            FilterHandler(this, null, null, 1)
        initData(preferences.getToken(),preferences.getTokenType())
        back_btn.setOnClickListener {
            finish()
        }
        btn_filter_reset.setOnClickListener {
            dbhandler!!.resetFilter()
            initData(preferences.getToken(),preferences.getTokenType())
        }
        btn_submit_filter.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
        Toast.makeText(this@FilterActivity, "Filter will be included in the next version. In Syaa Allaah.", Toast.LENGTH_LONG).show()

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    fun initData(token : String?, bearer : String?) {
        val apiservice = UtilsApi().getAPIService(applicationContext)
        apiservice?.daftarpengajuan()?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            val obj = JSONObject(response.body()!!.string())
                            val dataobj: JSONObject = obj.getJSONObject("success")

                            val gson = Gson()
                            val type: Type = object :
                                TypeToken<ArrayList<ModelDataPengajuan?>?>() {}.type
                            val datapengajuan: ArrayList<ModelDataPengajuan> =
                                gson.fromJson(dataobj.optString("data"), type)

                            val kodepptmpr: HashSet<String> = HashSet()
                            val modultmpr: HashSet<String> = HashSet()

                            for (data in datapengajuan) {
                                kodepptmpr.add(data.pp.toString())
                                modultmpr.add(data.modul.toString())
    //                            Log.i(
    //                                "Data Pengajuan",
    //                                data.no_dokumen.toString() + "-" + data.pembuat + "-" + data.due_date
    //                            )
                            }
                            val pp = ArrayList(kodepptmpr)
                            val modul = ArrayList(modultmpr)

                            myadapter =
                                FilterAdapter(
                                    pp,
                                    "PP"
                                )
                            gv_pp.layoutManager = GridLayoutManager(this@FilterActivity,3)
                            gv_pp.adapter = myadapter
                            myadapter =
                                FilterAdapter(
                                    modul,
                                    "Modul"
                                )
                            gv_modul.layoutManager = GridLayoutManager(this@FilterActivity,3)
                            gv_modul.adapter = myadapter
                        } catch (e: Exception) {
                            empty_view.visibility=View.VISIBLE
                            tv_modul.visibility=View.GONE
                            tv_pp.visibility= View.GONE
                            gv_modul.visibility=View.GONE
                            gv_pp.visibility= View.GONE
                        }
//                        rv_pengajuan.adapter = myadapter
                    }else{
                        //                        if (data.length() > 0) {
                        Toast.makeText(this@FilterActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if(response.code() == 422) {
                    Toast.makeText(this@FilterActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 401){
                    val intent = Intent(this@FilterActivity, LoginActivity::class.java)
                    startActivity(intent)
                    preferences.preferencesLogout()
                    finish()
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

}