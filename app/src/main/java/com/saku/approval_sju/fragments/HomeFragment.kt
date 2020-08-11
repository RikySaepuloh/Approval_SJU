package com.saku.approval_sju.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saku.approval_sju.*
import com.saku.approval_sju.adapter.DataPengajuanAdapter
import com.saku.approval_sju.api_service.UtilsApi
import com.saku.approval_sju.database.FilterHandler
import com.saku.approval_sju.models.ModelDataPengajuan
import kotlinx.android.synthetic.main.fragment_home.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class HomeFragment : Fragment(){
    private lateinit var myview: View
    var preferences  = Preferences()
    var myadapter : DataPengajuanAdapter? = null
    var myctx :Context? = null
    private var periode : String? = null
//    private var periode: String =
//        java.text.SimpleDateFormat("yyyyMM", java.util.Locale.getDefault()).format(java.util.Date())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_home, container, false)
        periode = (activity as MainActivity).periode
        myctx=context

//        try {
//            if(arguments?.getString(" periode")!=""|| arguments!!.getString("periode")!=null){
//                periode = arguments!!.getString("periode").toString()
//            }
//        } catch (e: Exception) {
//        }
        return myview
    }

    override fun onResume() {
        super.onResume()
        periode = (activity as MainActivity).periode
        initData()
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

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    private fun initData() {
        val apiservice = UtilsApi().getAPIService(myctx!!)
        apiservice?.daftarpengajuan(periode)?.enqueue(object : Callback<ResponseBody?> {
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
                            val status = dataobj.optString("message")
                            if(status=="Data Kosong!"){
                                myview.empty_view.visibility=View.VISIBLE
                                myview.rv_pengajuan.visibility=View.GONE
                            }else{
                                myview.empty_view.visibility=View.GONE
                                myview.rv_pengajuan.visibility=View.VISIBLE
                            }
//                            val mydata: ArrayList<ModelDataPengajuan>? = null
                            myadapter =
                                DataPengajuanAdapter(
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
                                        myview.rv_pengajuan.visibility=View.GONE
                                    }else{
                                        myview.empty_view.visibility=View.GONE
                                        myview.rv_pengajuan.visibility=View.VISIBLE
                                    }

                                }
                            })
                            myview.rv_pengajuan.adapter = myadapter
                        } catch (e: Exception) {
                            myview.empty_view.visibility=View.VISIBLE
                            myview.rv_pengajuan.visibility=View.GONE
                        }


//                        for (data in datapengajuan) {
//                            if (
//                                data.pp?.toLowerCase()?.contains(filter_pp)
//                            ){
//                                mydata?.add(data)
//                            }
////                            Log.i(
////                                "Data Pengajuan",
////                                data.no_dokumen.toString() + "-" + data.pembuat + "-" + data.due_date
////                            )
//                        }

                    }else{
                        //                        if (data.length() > 0) {
                        Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if(response.code() == 422) {
                    Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 401){
                    val intent = Intent(myctx, LoginActivity::class.java)
                    startActivity(intent)
                    preferences.preferencesLogout()
                    activity?.finish()
                    Toast.makeText(myctx, "Sesi telah berakhir, silahkan login kembali", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 403){
                    Toast.makeText(myctx, "Unauthorized", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 404){
                    Toast.makeText(myctx, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(myctx, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dbhandler =
            FilterHandler(myctx!!, null, null, 1)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(myctx)
        myview.rv_pengajuan.adapter = myadapter
        myview.rv_pengajuan.layoutManager = layoutManager
        myview.rv_pengajuan.setHasFixedSize(true)
        myview.btn_filter.setOnClickListener {
            val intent = Intent(myctx, FilterActivity::class.java)
            startActivity(intent)
        }
        preferences.setPreferences(myctx!!)
        initData()
        search(myview.sv_search)
        myview.swipe_refresh.setOnRefreshListener {
            initData()
            myview.swipe_refresh.isRefreshing = false
        }
        try {
            Log.d("isi dari filter : ", dbhandler.getAllFilter().toString())
        } catch (e: Exception) {
            Log.d("isi dari filter : ", "kosong")
        }
    }

    companion object {
        fun newInstance(): HomeFragment =
            HomeFragment()
    }
}