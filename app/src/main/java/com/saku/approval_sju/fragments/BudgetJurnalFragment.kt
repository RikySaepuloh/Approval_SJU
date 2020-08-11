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
import com.saku.approval_sju.adapter.BudgetJurnalAdapter
import com.saku.approval_sju.api_service.UtilsApi
import com.saku.approval_sju.models.ModelBudgetJurnal
import kotlinx.android.synthetic.main.fragment_budget_jurnal.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class BudgetJurnalFragment : Fragment() {
    private lateinit var myview: View
    var preferences  = Preferences()
    var myadapter : BudgetJurnalAdapter? = null
    var myctx : Context? = null
    private var displayopt : String? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_budget_jurnal, container, false)
        displayopt = activity!!.intent.extras!!.getString("displayopt")
        if(displayopt.equals("history")){
            myview.rv_budget.setPadding(0,0,0,0)
        }
        return myview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myctx=context
        preferences.setPreferences(myctx!!)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        myview.rv_budget.layoutManager = layoutManager
        myview.rv_budget.adapter = myadapter
        val noAju = activity!!.intent.extras!!.getString("no_aju")
        initData(noAju)
        val modul = activity!!.intent.extras!!.getString("modul")



    }

    private fun initData(no_aju : String?) {
        val apiservice= UtilsApi().getAPIService(activity!!.applicationContext)
        apiservice?.detailjurnal(no_aju)?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            val obj = JSONObject(response.body()!!.string())
                            val dataobj: JSONObject = obj.getJSONObject("success")
//
                            val gson = Gson()
                            val type: Type = object :
                                TypeToken<ArrayList<ModelBudgetJurnal?>?>() {}.type
                            val databudgetjurnal: ArrayList<ModelBudgetJurnal> =
                                gson.fromJson(dataobj.optString("data"), type)
//                        for (data in datapengajuan) {
//                            Log.i(
//                                "Data Pengajuan",
//                                data.no_dokumen.toString() + "-" + data.pembuat + "-" + data.due_date
//                            )
//                        }
                            myadapter =
                                BudgetJurnalAdapter(
                                    databudgetjurnal
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
                                        myview.rv_budget.visibility=View.GONE
                                    }else{
                                        myview.empty_view.visibility=View.GONE
                                        myview.rv_budget.visibility=View.VISIBLE
                                    }

                                }
                            })
                            myview.rv_budget.adapter = myadapter
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
//                    progressBar.setVisibility(View.GONE)
                Toast.makeText(myctx, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
//        fun login(nik:String?,password:String?){
//
//        }
    }


//    fun expandcollapse(title : RelativeLayout, content : LinearLayout, icon : ImageView){
//        title.setOnClickListener {
//            if (content.visibility==View.GONE){
//                TransitionManager.beginDelayedTransition(content,  AutoTransition())
//                content.visibility=View.VISIBLE
//                icon.animate().rotationBy(180f).start()
////                expand(content,icon)
//            }else{
//                TransitionManager.beginDelayedTransition(content,  AutoTransition())
////        content.animate().translationY(0F);
//                content.visibility=View.GONE
//                icon.animate().rotationBy(180f).start()
////                collapse(content,icon)
//            }
//        }
//    }

//    fun expand(content: LinearLayout, icon: ImageView){
////        content.animate().translationY(content.height.toFloat());
//        TransitionManager.beginDelayedTransition(content,  AutoTransition())
//        content.visibility=View.VISIBLE
//        icon.rotation = 180f
////        icon.animate().rotationBy(180f).start()
//    }
//
//    fun collapse(content: LinearLayout, icon: ImageView){
//        TransitionManager.beginDelayedTransition(content,  AutoTransition())
////        content.animate().translationY(0F);
//        content.visibility=View.GONE
//        icon.rotation = 0f
////        icon.animate().rotationBy(180f).start()
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

    }


//    fun initData(token: String?, bearer : String?) {
//        apiservice?.daftarpengajuan(token,bearer)?.enqueue(object : Callback<ResponseBody?> {
//            override fun onResponse(
//                call: Call<ResponseBody?>,
//                response: Response<ResponseBody?>
//            ) {
//                if (response.isSuccessful) {
//                    if (response.body() != null) {
//                        val obj = JSONObject(response.body()!!.string())
//                        val dataobj: JSONObject = obj.getJSONObject("success")
//
//                        val gson = Gson()
//                        val type: Type = object :
//                            TypeToken<ArrayList<ModelDataPengajuan?>?>() {}.type
//                        val datapengajuan: ArrayList<ModelDataPengajuan> =
//                            gson.fromJson(dataobj.optString("data"), type)
////                        for (data in datapengajuan) {
////                            Log.i(
////                                "Data Pengajuan",
////                                data.no_dokumen.toString() + "-" + data.pembuat + "-" + data.due_date
////                            )
////                        }
//                        myadapter = DataPengajuanAdapter(datapengajuan)
//                        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
//                        myview.rv_pengajuan.layoutManager = layoutManager
//                        myview.rv_pengajuan.adapter = myadapter
//                    }else{
//                        //                        if (data.length() > 0) {
//                        Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
//                    }
//                } else if(response.code().equals(422)) {
//                    Toast.makeText(myctx, "Username/Password masih kosong", Toast.LENGTH_SHORT).show()
//                } else if(response.code().equals(401)){
//                    Toast.makeText(myctx, "Username/Password salah", Toast.LENGTH_SHORT).show()
//                } else if(response.code().equals(403)){
//                    Toast.makeText(myctx, "Token Invalid", Toast.LENGTH_SHORT).show()
//                } else if(response.code().equals(404)){
//                    Toast.makeText(myctx, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
//                Toast.makeText(myctx, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }


    companion object {
        fun newInstance(): BudgetJurnalFragment =
            BudgetJurnalFragment()
    }
}