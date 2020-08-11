package com.saku.approval_sju.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saku.approval_sju.LoginActivity
import com.saku.approval_sju.Preferences
import com.saku.approval_sju.ProsesActivity
import com.saku.approval_sju.R
import com.saku.approval_sju.adapter.DokumenAdapter
import com.saku.approval_sju.api_service.UtilsApi
import com.saku.approval_sju.models.ModelDokumen
import kotlinx.android.synthetic.main.fragment_dokumen.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class DokumenFragment : Fragment() {
    var MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 100
    var MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 101
    private lateinit var myview: View
//        val apiservice= UtilsApi().getAPIService(activity!!.applicationContext)
    var preferences  = Preferences()
    var myctx : Context? = null
    var displayopt : String? = null
    var myadapter : DokumenAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_dokumen, container, false)
        return myview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myctx=context
        preferences.setPreferences(myctx!!)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        myview.rv_dokumen.layoutManager = layoutManager
        myview.rv_dokumen.adapter = myadapter
        val noAju = activity!!.intent.extras!!.getString("no_aju")
        val modul = activity!!.intent.extras!!.getString("modul")
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(myctx!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

        if (ContextCompat.checkSelfPermission(myctx!!,
                Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(activity!!,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }
        initData(noAju,preferences.getToken(),preferences.getTokenType())
    }

    fun initData(no_aju : String?, token: String?, bearer : String?) {
        val apiservice= UtilsApi().getAPIService(activity!!.applicationContext)
        apiservice?.detaildokumen(no_aju)?.enqueue(object : Callback<ResponseBody?> {
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
                                TypeToken<ArrayList<ModelDokumen?>>() {}.type
                            val datapengajuan: ArrayList<ModelDokumen> =
                                gson.fromJson(dataobj.optString("data"), type)
                            myadapter =
                                DokumenAdapter(
                                    rawData = datapengajuan
                                )
                            myview.rv_dokumen.adapter = myadapter
                        } catch (e: Exception) {
                            myview.empty_view.visibility=View.VISIBLE
                            myview.rv_dokumen.visibility=View.GONE
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
        fun newInstance(): DokumenFragment =
            DokumenFragment()
    }
}