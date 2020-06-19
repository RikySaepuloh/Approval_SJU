package com.saku.approval_sju.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saku.approval_sju.LoginActivity
import com.saku.approval_sju.Preferences
import com.saku.approval_sju.R
import com.saku.approval_sju.adapter.HistoryPengajuanAdapter
import com.saku.approval_sju.api_service.UtilsApi
import com.saku.approval_sju.models.ModelDataPengajuan
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.fragment_history.view.*
import kotlinx.android.synthetic.main.fragment_history.view.empty_view
import kotlinx.android.synthetic.main.fragment_history.view.sv_search
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class HistoryFragment : Fragment() {
    private lateinit var myview: View
    var preferences  = Preferences()
    var myctx : Context? = null
    var myadapter : HistoryPengajuanAdapter?=null
    var myparams : String? = "All"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_history, container, false)
        myview.swipe_refresh.setOnRefreshListener {
            initData(preferences.getToken(),preferences.getTokenType(),"INPROG",myparams)
            myview.swipe_refresh.isRefreshing = false

        }
        return myview
    }

    private fun search(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                myadapter?.getFilter()?.filter(newText)
                return true
            }
        })
    }

    fun initData(token: String?, bearer : String?, status : String?,params : String?) {
        myview.progress_bar.visibility = View.VISIBLE
        val apiservice= UtilsApi().getAPIService(myctx!!)
        when {
            params.equals("All") -> {
                apiservice?.daftarhistoryall()?.enqueue(object : Callback<ResponseBody?> {
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
                                    myadapter =
                                        HistoryPengajuanAdapter(
                                            datapengajuan
                                        )
                                    myadapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                                        override fun onChanged() {
                                            super.onChanged()
                                            checkEmpty()
                                        }

                                        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                                            super.onItemRangeInserted(positionStart, itemCount)
                                            checkEmpty()
                                        }

                                        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                                            super.onItemRangeRemoved(positionStart, itemCount)
                                            checkEmpty()
                                        }

                                        fun checkEmpty() {
                                            if(myadapter!!.itemCount==0){
                                                myview.empty_view.visibility=View.VISIBLE
                                                myview.rv_history.visibility=View.GONE
                                            }else{
                                                myview.empty_view.visibility=View.GONE
                                                myview.rv_history.visibility=View.VISIBLE
                                            }
                                        }
                                    })
                                    myview.rv_history.adapter = myadapter
                                    myview.empty_view.visibility=View.GONE
                                    myview.rv_history.visibility=View.VISIBLE
                                    myview.progress_bar.visibility = View.GONE
                                } catch (e: Exception) {
                                    myview.empty_view.visibility=View.VISIBLE
                                    myview.rv_history.visibility=View.GONE
                                    myview.progress_bar.visibility = View.GONE
                                }
                            }else{
                                Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                                myview.progress_bar.visibility = View.GONE
                            }
                        } else if(response.code() == 422) {
                            Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                            myview.progress_bar.visibility = View.GONE
                        } else if(response.code() == 401){
                            myview.progress_bar.visibility = View.GONE
                            val intent = Intent(context, LoginActivity::class.java)
                            startActivity(intent)
                            preferences.preferencesLogout()
                            activity?.finish()
                            Toast.makeText(myctx, "Sesi telah berakhir, silahkan login kembali", Toast.LENGTH_SHORT).show()
                        } else if(response.code() == 403){
                            myview.progress_bar.visibility = View.GONE
                            Toast.makeText(myctx, "Unauthorized", Toast.LENGTH_SHORT).show()
                        } else if(response.code() == 404){
                            myview.progress_bar.visibility = View.GONE
                            Toast.makeText(myctx, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        Toast.makeText(myctx, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
                        myview.progress_bar.visibility = View.GONE
                    }
                })
            }
            params.equals("Approve") -> {
                apiservice?.daftarhistoryapprove()?.enqueue(object : Callback<ResponseBody?> {
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
                                    myadapter =
                                        HistoryPengajuanAdapter(
                                            datapengajuan
                                        )
                                    myadapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                                        override fun onChanged() {
                                            super.onChanged()
                                            checkEmpty()
                                        }

                                        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                                            super.onItemRangeInserted(positionStart, itemCount)
                                            checkEmpty()
                                        }

                                        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                                            super.onItemRangeRemoved(positionStart, itemCount)
                                            checkEmpty()
                                        }

                                        fun checkEmpty() {
                                            if(myadapter!!.itemCount==0){
                                                myview.empty_view.visibility=View.VISIBLE
                                                myview.rv_history.visibility=View.GONE
                                            }else{
                                                myview.empty_view.visibility=View.GONE
                                                myview.rv_history.visibility=View.VISIBLE
                                            }
                                        }
                                    })
                                    myview.rv_history.adapter = myadapter
                                    myview.empty_view.visibility=View.GONE
                                    myview.rv_history.visibility=View.VISIBLE
                                    myview.progress_bar.visibility = View.GONE
                                } catch (e: Exception) {
                                    myview.empty_view.visibility=View.VISIBLE
                                    myview.rv_history.visibility=View.GONE
                                    myview.progress_bar.visibility = View.GONE
                                }
                            }else{
                                Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                                myview.progress_bar.visibility = View.GONE
                            }
                        } else if(response.code() == 422) {
                            Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                            myview.progress_bar.visibility = View.GONE
                        } else if(response.code() == 401){
                            myview.progress_bar.visibility = View.GONE
                            val intent = Intent(context, LoginActivity::class.java)
                            startActivity(intent)
                            preferences.preferencesLogout()
                            activity?.finish()
                            Toast.makeText(myctx, "Sesi telah berakhir, silahkan login kembali", Toast.LENGTH_SHORT).show()
                        } else if(response.code() == 403){
                            myview.progress_bar.visibility = View.GONE
                            Toast.makeText(myctx, "Unauthorized", Toast.LENGTH_SHORT).show()
                        } else if(response.code() == 404){
                            myview.progress_bar.visibility = View.GONE
                            Toast.makeText(myctx, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        myview.progress_bar.visibility = View.GONE
                        Toast.makeText(myctx, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
                    }
                })
            }
            else -> {
                apiservice?.daftarhistoryreject()?.enqueue(object : Callback<ResponseBody?> {
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
    //                        for (data in datapengajuan) {
    //                            Log.i(
    //                                "Data Pengajuan",
    //                                data.no_dokumen.toString() + "-" + data.pembuat + "-" + data.due_date
    //                            )
    //                        }
                                    myadapter =
                                        HistoryPengajuanAdapter(
                                            datapengajuan
                                        )
                                    myadapter!!.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                                        override fun onChanged() {
                                            super.onChanged()
                                            checkEmpty()
                                        }

                                        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                                            super.onItemRangeInserted(positionStart, itemCount)
                                            checkEmpty()
                                        }

                                        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
                                            super.onItemRangeRemoved(positionStart, itemCount)
                                            checkEmpty()
                                        }

                                        fun checkEmpty() {
                                            if(myadapter!!.itemCount==0){
                                                myview.empty_view.visibility=View.VISIBLE
                                                myview.rv_history.visibility=View.GONE
                                            }else{
                                                myview.empty_view.visibility=View.GONE
                                                myview.rv_history.visibility=View.VISIBLE
                                            }
                                        }
                                    })
                                    myview.rv_history.adapter = myadapter
                                    myview.progress_bar.visibility = View.GONE
                                    myview.rv_history.visibility=View.VISIBLE
                                    myview.empty_view.visibility=View.GONE
                                } catch (e: Exception) {
                                    myview.empty_view.visibility=View.VISIBLE
                                    myview.rv_history.visibility=View.GONE
                                    myview.progress_bar.visibility = View.GONE
                                }
                            }else{
                                Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                                myview.progress_bar.visibility = View.GONE
                            }
                        } else if(response.code() == 422) {
                            Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                            myview.progress_bar.visibility = View.GONE
                        } else if(response.code() == 401){
                            myview.progress_bar.visibility = View.GONE
                            val intent = Intent(context, LoginActivity::class.java)
                            startActivity(intent)
                            preferences.preferencesLogout()
                            activity?.finish()
                            Toast.makeText(myctx, "Sesi telah berakhir, silahkan login kembali", Toast.LENGTH_SHORT).show()
                        } else if(response.code() == 403){
                            myview.progress_bar.visibility = View.GONE
                            Toast.makeText(myctx, "Unauthorized", Toast.LENGTH_SHORT).show()
                        } else if(response.code() == 404){
                            myview.progress_bar.visibility = View.GONE
                            Toast.makeText(myctx, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                        myview.progress_bar.visibility = View.GONE
                        Toast.makeText(myctx, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myctx=context
        preferences.setPreferences(myctx!!)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        myview.rv_history.layoutManager = layoutManager
        myview.rv_history.adapter = myadapter
        myview.rv_history.setHasFixedSize(true)
        initData(preferences.getToken(),preferences.getTokenType(),"INPROG",myparams)
        search(myview.sv_search)


        myview.btn_filter_semua.setOnClickListener {
            btn_filter_semua.setBackgroundResource(R.drawable.bg_primary_rounded)
            btn_filter_semua.setTextColor(ContextCompat.getColor(myctx!!,R.color.white))
            btn_filter_appv.setBackgroundResource(R.drawable.bg_white_rounded)
            btn_filter_appv.setTextColor(ContextCompat.getColor(myctx!!,R.color.grey))
            btn_filter_rjtd.setBackgroundResource(R.drawable.bg_white_rounded)
            btn_filter_rjtd.setTextColor(ContextCompat.getColor(myctx!!,R.color.grey))
            myparams = "All"
            initData(preferences.getToken(),preferences.getTokenType(),"INPROG",myparams)
        }

        myview.btn_filter_appv.setOnClickListener {
            btn_filter_appv.setBackgroundResource(R.drawable.bg_primary_rounded)
            btn_filter_appv.setTextColor(ContextCompat.getColor(myctx!!,R.color.white))
            btn_filter_semua.setBackgroundResource(R.drawable.bg_white_rounded)
            btn_filter_semua.setTextColor(ContextCompat.getColor(myctx!!,R.color.grey))
            btn_filter_rjtd.setBackgroundResource(R.drawable.bg_white_rounded)
            btn_filter_rjtd.setTextColor(ContextCompat.getColor(myctx!!,R.color.grey))
            myparams = "Approve"
            initData(preferences.getToken(),preferences.getTokenType(),"INPROG",myparams)
        }

        myview.btn_filter_rjtd.setOnClickListener {
            btn_filter_rjtd.setBackgroundResource(R.drawable.bg_primary_rounded)
            btn_filter_rjtd.setTextColor(ContextCompat.getColor(myctx!!,R.color.white))
            btn_filter_appv.setBackgroundResource(R.drawable.bg_white_rounded)
            btn_filter_appv.setTextColor(ContextCompat.getColor(myctx!!,R.color.grey))
            btn_filter_semua.setBackgroundResource(R.drawable.bg_white_rounded)
            btn_filter_semua.setTextColor(ContextCompat.getColor(myctx!!,R.color.grey))
            myparams = "REJECTED"
            initData(preferences.getToken(),preferences.getTokenType(),"INPROG",myparams)
        }

    }



    companion object {
        fun newInstance(): HistoryFragment =
            HistoryFragment()
    }
}