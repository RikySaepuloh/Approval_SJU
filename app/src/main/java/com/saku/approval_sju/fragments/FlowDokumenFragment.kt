package com.saku.approval_sju.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saku.approval_sju.LoginActivity
import com.saku.approval_sju.Preferences
import com.saku.approval_sju.ProsesActivity
import com.saku.approval_sju.R
import com.saku.approval_sju.adapter.FlowDokumen
import com.saku.approval_sju.api_service.UtilsApi
import com.saku.approval_sju.models.ModelHistoryApproval
import kotlinx.android.synthetic.main.fragment_budget_jurnal.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class FlowDokumenFragment : Fragment() {
    private lateinit var myview: View
    var preferences  = Preferences()
    var myctx : Context? = null
    var displayopt : String? = null
    var myadapter : FlowDokumen? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_budget_jurnal, container, false)
        displayopt = activity!!.intent.extras!!.getString("displayopt")
        if(displayopt.equals("history")){
            myview.layout_appv.visibility = View.GONE
            myview.rv_budget.setPadding(0,0,0,0)
        }
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        myview.rv_budget.layoutManager = layoutManager
        myview.rv_budget.adapter = myadapter

        return myview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myctx=context
        preferences.setPreferences(myctx!!)
        val noAju = activity!!.intent.extras!!.getString("no_aju")
        val modul = activity!!.intent.extras!!.getString("modul")

        initData(noAju,preferences.getToken(),preferences.getTokenType())
        myview.btn_approve.setOnClickListener {
            val intent = Intent(context, ProsesActivity::class.java)
                .apply {
                    putExtra("no_aju", noAju)
                    putExtra("status", "Approve")
                    putExtra("modul", modul)
                }
            startActivity(intent)
        }
        myview.btn_reject.setOnClickListener {
            val intent = Intent(context, ProsesActivity::class.java)
                .apply {
                    putExtra("no_aju", noAju)
                    putExtra("status", "Reject")
                    putExtra("modul", modul)
                }
            startActivity(intent)
        }
    }

    fun initData(no_aju : String?, token: String?, bearer : String?) {
        val apiservice= UtilsApi().getAPIService(activity!!.applicationContext)
        apiservice?.detailflow(no_aju)?.enqueue(object : Callback<ResponseBody?> {
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
                                TypeToken<ArrayList<ModelHistoryApproval?>>() {}.type
                            val datapengajuan: ArrayList<ModelHistoryApproval> =
                                gson.fromJson(dataobj.optString("data"), type)
                            myadapter =
                                FlowDokumen(
                                    rawData = datapengajuan
                                )
                            myview.rv_budget.adapter = myadapter
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
                                        myview.rv_budget.visibility=View.GONE
                                    }else{
                                        myview.empty_view.visibility=View.GONE
                                        myview.rv_budget.visibility=View.VISIBLE
                                    }

                                }
                            })

                        } catch (e: Exception) {
                            myview.empty_view.visibility=View.VISIBLE
                            myview.rv_budget.visibility=View.GONE
                        }
                    }else{
                        Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if(response.code() == 422) {
                    Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 401){
                    val intent = Intent(context, LoginActivity::class.java)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

    }


    companion object {
        fun newInstance(): FlowDokumenFragment =
            FlowDokumenFragment()
    }
}