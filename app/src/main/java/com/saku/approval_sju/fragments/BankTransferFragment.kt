package com.saku.approval_sju.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saku.approval_sju.LoginActivity
import com.saku.approval_sju.Preferences
import com.saku.approval_sju.ProsesActivity
import com.saku.approval_sju.R
import com.saku.approval_sju.api_service.UtilsApi
import com.saku.approval_sju.models.ModelBankTransfer
import kotlinx.android.synthetic.main.fragment_bank_transfer.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class BankTransferFragment : Fragment() {
    private lateinit var myview: View
    var preferences  = Preferences()
    var myctx : Context? = null
    var displayopt : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_bank_transfer, container, false)
        displayopt = activity!!.intent.extras!!.getString("displayopt")
        if(displayopt.equals("history")){
            myview.layout_appv.visibility = View.GONE
            myview.scroll_appv.setPadding(0,0,0,0)
        }
        return myview
    }

    fun initData(no_aju : String?,token: String?, bearer : String?) {
        val apiservice= UtilsApi().getAPIService(activity!!.applicationContext)
        apiservice?.detailrekening(no_aju)?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val obj = JSONObject(response.body()!!.string())
                        val dataobj: JSONObject = obj.getJSONObject("success")
//
                        val gson = Gson()
                        val type: Type = object :
                            TypeToken<ArrayList<ModelBankTransfer?>?>() {}.type
                        val datapengajuan: ArrayList<ModelBankTransfer> =
                            gson.fromJson(dataobj.optString("data"), type)
                        for (data in datapengajuan) {
                            if (data.bank == "MANDIRI"){
                                myview.logo_rek.setImageResource(R.drawable.ic_mandiri)
                            }else{
                                myview.logo_rek.setImageResource(R.drawable.ic_bni)
                            }
                            myview.no_rek.text = textspacing(data.noRek)
                            myview.tv_namarek.text = data.namaRek
                            myview.lokasi_rek.text = data.cabang
                            myview.tv_nilaibruto.text = data.bruto
                            myview.tv_potongan.text = data.pajak
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
//                    progressBar.setVisibility(View.GONE)
                Toast.makeText(myctx, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun textspacing(text : String): String {
        val n = 3
        val str = StringBuilder(text)
        var id : Int = 0 + n
        while (id < str.length) {
            str.insert(id, " ")
            id = id + n + 1
        }
        return str.toString()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myctx=context
        preferences.setPreferences(myctx!!)
        val noAju = activity!!.intent.extras!!.getString("no_aju")
        val modul = activity!!.intent.extras!!.getString("modul")
        initData(noAju,preferences.getToken(),preferences.getTokenType())


        myview.btn_approve.setOnClickListener {
            val intent = Intent(myctx, ProsesActivity::class.java)
                .apply {
                    putExtra("no_aju", noAju)
                    putExtra("status", "Approve")
                    putExtra("modul", modul)
                }
            startActivity(intent)
        }
        myview.btn_reject.setOnClickListener {
            val intent = Intent(myctx, ProsesActivity::class.java)
                .apply {
                    putExtra("no_aju", noAju)
                    putExtra("status", "Reject")
                    putExtra("modul", modul)
                }
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance(): BankTransferFragment =
            BankTransferFragment()
    }
}