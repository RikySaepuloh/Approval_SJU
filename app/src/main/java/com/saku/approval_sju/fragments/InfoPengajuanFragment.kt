package com.saku.approval_sju.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saku.approval_sju.LoginActivity
import com.saku.approval_sju.Preferences
import com.saku.approval_sju.ProsesActivity
import com.saku.approval_sju.R
import com.saku.approval_sju.api_service.UtilsApi
import com.saku.approval_sju.models.ModelInfoPengajuan
import kotlinx.android.synthetic.main.fragment_info_pengajuan.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class InfoPengajuanFragment : Fragment() {
    private lateinit var myview: View
//        val apiservice= UtilsApi().getAPIService(activity!!.applicationContext)
    var preferences  = Preferences()
    var myctx : Context? = null
    var displayopt : String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_info_pengajuan, container, false)
        displayopt = activity!!.intent.extras!!.getString("displayopt")
        if(displayopt.equals("history")){
            myview.layout_appv.visibility = View.GONE
            myview.scroll_appv.setPadding(0,0,0,0)
            myview.layout_keterangan.visibility = View.VISIBLE
        }
        return myview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myctx=context
        preferences.setPreferences(myctx!!)

        expand(myview.card_content_nilai,myview.iv_expand_nilai)
        expandcollapse(myview.card_title_nilai,myview.card_content_nilai,myview.iv_expand_nilai)
        expand(myview.card_content_pembuat,myview.iv_expand_pembuat)
        expandcollapse(myview.card_title_pembuat,myview.card_content_pembuat,myview.iv_expand_pembuat)
        expand(myview.card_content_detailp,myview.iv_expand_detailp)
        expandcollapse(myview.card_title_detailp,myview.card_content_detailp,myview.iv_expand_detailp)
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
        val apiservice = UtilsApi().getAPIService(activity!!.applicationContext)
        if(displayopt.equals("history")) {
              apiservice?.detailriwayat(no_aju)?.enqueue(object : Callback<ResponseBody?> {
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
                                    TypeToken<ArrayList<ModelInfoPengajuan?>?>() {}.type
                                val datapengajuan: ArrayList<ModelInfoPengajuan> =
                                    gson.fromJson(dataobj.optString("data"), type)
                                for (data in datapengajuan) {
                                    myview.tv_catatan?.text = "Catatan :\n"+data.getCatatan()
                                    myview.tv_total_pengajuan?.text = data.getNilai() + " IDR"
                                    myview.tv_total_verifikasi?.text = data.getNilai() + " IDR"
                                    myview.tv_total_rekening?.text = data.getNilai() + " IDR"
                                    myview.tv_nilai_verifikasi?.text = data.getNilai() + " IDR"
                                    myview.tv_pp?.text = data.getPp()
                                    myview.tv_deskripsi?.text = data.getKeterangan()
                                    myview.tv_nama_pembuat?.text = data.getPembuat()
                                    myview.tv_no_dokumen?.text = data.getNoDokumen()
                                    myview.tv_nobukti?.text = data.getNoBukti()
                                    myview.tv_modul?.text = data.getModul()
                                    myview.tv_tgl_bukti?.text = data.getTgl()
                                    myview.tv_due?.text = data.getDueDate()

    //                            Log.i(
    //                                "Data Pengajuan",
    //                                data.no_dokumen.toString() + "-" + data.pembuat + "-" + data.due_date
    //                            )
                                }
                            } catch (e: Exception) {
                            }
                        } else {
                            Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    } else if (response.code() == 422) {
                        Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    } else if (response.code() == 401) {
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                        preferences.preferencesLogout()
                        activity?.finish()
                        Toast.makeText(
                            myctx,
                            "Sesi telah berakhir, silahkan login kembali",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (response.code() == 403) {
                        Toast.makeText(myctx, "Unauthorized", Toast.LENGTH_SHORT).show()
                    } else if (response.code() == 404) {
                        Toast.makeText(myctx, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(myctx, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            apiservice?.detailpengajuan(no_aju)?.enqueue(object : Callback<ResponseBody?> {
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
                                    TypeToken<ArrayList<ModelInfoPengajuan?>?>() {}.type
                                val datapengajuan: ArrayList<ModelInfoPengajuan> =
                                    gson.fromJson(dataobj.optString("data"), type)
                                for (data in datapengajuan) {

                                    myview.tv_total_pengajuan?.text = data.getNilai() + " IDR"
                                    myview.tv_total_verifikasi?.text = data.getNilai() + " IDR"
                                    myview.tv_total_rekening?.text = data.getNilai() + " IDR"
                                    myview.tv_nilai_verifikasi?.text = data.getNilai() + " IDR"
                                    myview.tv_pp?.text = data.getPp()
                                    myview.tv_deskripsi?.text = data.getKeterangan()
                                    myview.tv_nama_pembuat?.text = data.getPembuat()
                                    myview.tv_no_dokumen?.text = data.getNoDokumen()
                                    myview.tv_nobukti?.text = data.getNoBukti()
                                    myview.tv_modul?.text = data.getModul()
                                    myview.tv_tgl_bukti?.text = data.getTgl()
                                    myview.tv_due?.text = data.getDueDate()

                                    //                            Log.i(
                                    //                                "Data Pengajuan",
                                    //                                data.no_dokumen.toString() + "-" + data.pembuat + "-" + data.due_date
                                    //                            )
                                }
                            } catch (e: Exception) {
                            }
                        } else {
                            Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                        }
                    } else if (response.code() == 422) {
                        Toast.makeText(myctx, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    } else if (response.code() == 401) {
                        val intent = Intent(context, LoginActivity::class.java)
                        startActivity(intent)
                        preferences.preferencesLogout()
                        activity?.finish()
                        Toast.makeText(
                            myctx,
                            "Sesi telah berakhir, silahkan login kembali",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else if (response.code() == 403) {
                        Toast.makeText(myctx, "Unauthorized", Toast.LENGTH_SHORT).show()
                    } else if (response.code() == 404) {
                        Toast.makeText(myctx, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                    Toast.makeText(myctx, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    fun expandcollapse(title : RelativeLayout, content : LinearLayout, icon : ImageView){
        title.setOnClickListener {
            if (content.visibility==View.GONE){
                TransitionManager.beginDelayedTransition(content,  AutoTransition())
                content.visibility=View.VISIBLE
                icon.animate().rotationBy(180f).start()
            }else{
                TransitionManager.beginDelayedTransition(content,  AutoTransition())
                content.visibility=View.GONE
                icon.animate().rotationBy(180f).start()
            }
        }
    }

    fun expand(content: LinearLayout, icon: ImageView){
        TransitionManager.beginDelayedTransition(content,  AutoTransition())
        content.visibility=View.VISIBLE
        icon.rotation = 180f
    }

    fun collapse(content: LinearLayout, icon: ImageView){
        TransitionManager.beginDelayedTransition(content,  AutoTransition())
        content.visibility=View.GONE
        icon.rotation = 0f
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

    }


    fun showConfirm(status: String){
        if (status == "Approve"){
            val builder = AlertDialog.Builder(myctx)
            //            builder.setTitle("Peringatan");
            builder.setMessage("Yakin akan me-approve?")
            builder.setCancelable(true)
            builder.setPositiveButton(
                "Ya"
            ) {
                    _, _ ->  }
            builder.setNegativeButton(
                "Tidak"
            ) { _, _ ->
                // Do something when want to stay in the app
            }
            val dialog = builder.create()
            dialog.show()
        }else{
            val builder = AlertDialog.Builder(myctx)
            builder.setMessage("Yakin akan me-reject?")
            builder.setCancelable(true)
            builder.setPositiveButton(
                "Ya"
            ) {
                    _, _ ->  }
            builder.setNegativeButton(
                "Tidak"
            ) { _, _ ->
                // Do something when want to stay in the app
            }
            val dialog = builder.create()
            dialog.show()
        }

    }

    companion object {
        fun newInstance(): InfoPengajuanFragment =
            InfoPengajuanFragment()
    }
}