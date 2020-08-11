package com.saku.approval_sju.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saku.approval_sju.LoginActivity
import com.saku.approval_sju.Preferences
import com.saku.approval_sju.R
import com.saku.approval_sju.adapter.NotifikasiAdapter
import com.saku.approval_sju.api_service.UtilsApi
import com.saku.approval_sju.models.ModelNotifikasi
import kotlinx.android.synthetic.main.fragment_notifikasi.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * A simple [Fragment] subclass.
 */
class NotifikasiFragment : Fragment() {
    private lateinit var myview : View
    private lateinit var dataAdapter: NotifikasiAdapter
    var preferences  = Preferences()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_notifikasi, container, false)
        preferences.setPreferences(context!!)
        dataAdapter = NotifikasiAdapter(context!!)
        myview.recyclerview.layoutManager = LinearLayoutManager(context)
        myview.recyclerview.adapter = dataAdapter
        return myview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myview.swipe_refresh.setOnRefreshListener { getNotif() }
        getNotif()
    }

    fun getNotif(){
        val apiservice = UtilsApi().getAPIService(context!!)
        apiservice?.notif()?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {

                            val obj = JSONObject(response.body()!!.string())
    //                            val dataobj: JSONObject = obj.getJSONObject("success")

                            val gson = Gson()
                            val type = object :
                                TypeToken<MutableList<ModelNotifikasi?>?>() {}.type
                            val data: MutableList<ModelNotifikasi> =
                                gson.fromJson(obj.optString("data"), type)
                            dataAdapter.registerAdapterDataObserver(object :
                                RecyclerView.AdapterDataObserver() {
                                override fun onChanged() {
                                    super.onChanged()
                                    checkEmpty()
                                }

                                override fun onItemRangeInserted(
                                    positionStart: Int,
                                    itemCount: Int
                                ) {
                                    super.onItemRangeInserted(positionStart, itemCount)
                                    checkEmpty()
                                }

                                override fun onItemRangeRemoved(
                                    positionStart: Int,
                                    itemCount: Int
                                ) {
                                    super.onItemRangeRemoved(positionStart, itemCount)
                                    checkEmpty()
                                }

                                fun checkEmpty() {
                                    if (dataAdapter.itemCount == 0 || dataAdapter.itemCount < 1) {
                                        myview.empty_view.visibility = View.VISIBLE
                                        myview.recyclerview.visibility = View.GONE
                                    } else {
                                        myview.empty_view.visibility = View.GONE
                                        myview.recyclerview.visibility = View.VISIBLE
                                    }

                                }
                            })
                            dataAdapter.clearData()
                            dataAdapter.initData(data)
                            myview.swipe_refresh.isRefreshing = false
                        } catch (e: Exception) {
                            myview.swipe_refresh.isRefreshing = false
                        }
                    } else {
                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        myview.swipe_refresh.isRefreshing = false
                    }
                } else if(response.code() == 422) {
                    Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    myview.swipe_refresh.isRefreshing = false
                } else if(response.code() == 401){
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                    preferences.preferencesLogout()
                    activity?.finishAffinity()
                    Toast.makeText(context, "Sesi telah berakhir, silahkan login kembali", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 403){
                    Toast.makeText(context, "Unauthorized", Toast.LENGTH_SHORT).show()
                    myview.swipe_refresh.isRefreshing = false
                } else if(response.code() == 404||response.code()==405){
                    Toast.makeText(context, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                    myview.swipe_refresh.isRefreshing = false
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(context, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
                myview.swipe_refresh.isRefreshing = false

            }
        })
    }


}
