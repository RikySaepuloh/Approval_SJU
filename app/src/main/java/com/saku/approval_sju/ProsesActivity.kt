package com.saku.approval_sju

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.saku.approval_sju.api_service.UtilsApi
import kotlinx.android.synthetic.main.activity_proses.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*


class ProsesActivity : AppCompatActivity() {
    var preferences  = Preferences()
    var noUrut : String? = null
    lateinit var status:String
    lateinit var noAju:String
    lateinit var modul:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proses)
        setSupportActionBar(toolbar)
        preferences.setPreferences(this)
        noAju = intent.getStringExtra("no_aju")
        status = intent.getStringExtra("status")
        modul = intent.getStringExtra("modul")
//        modul = this.intent.extras!!.getString("modul")
//        noAju = this.intent.extras!!.getString("no_aju")
//        var status = this.intent.extras!!.getString("status")

        if(status == "Approve"){
            status_pengajuan.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this,
                R.drawable.ic_approved_small),null,null,null)
            status_pengajuan.text = "Approved"
            status_pengajuan.setTextColor(ContextCompat.getColor(this,
                R.color.green))
        }else{
            status_pengajuan.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(this,
                R.drawable.ic_rejected_small),null,null,null)
            status_pengajuan.setTextColor(ContextCompat.getColor(this,
                R.color.red))
            status_pengajuan.text = "Return"
        }

        back_btn.setOnClickListener { finish() }

        btn_proses.setOnClickListener {
            showConfirm(status)
        }
    }

    fun showConfirm(status: String?){
//        Toast.makeText(context, "34-PB2005.0004 PBBAU", Toast.LENGTH_SHORT).show()
        if(input_keterangan.text.toString().trim() == ""){
            Toast.makeText(this, "Keterangan masih kosong!", Toast.LENGTH_SHORT).show()
        }else{
            if (status == "Approve"){
                val builder = AlertDialog.Builder(this)
                //            builder.setTitle("Peringatan");
                builder.setMessage("No Pengajuan: $noAju - $modul akan di approve!\n\nYakin akan me-approve ?")
                builder.setCancelable(true)
                builder.setPositiveButton(
                    "Ya"
                ) {
                        _, _ ->
//                    Toast.makeText(this,"Hardcoded",Toast.LENGTH_SHORT).show()
                    sendApprove("APPROVE") }
                builder.setNegativeButton(
                    "Tidak"
                ) { _, _ ->
                    // Do something when want to stay in the app
                    //                    Toast.makeText(getApplicationContext(),"thank you",Toast.LENGTH_LONG).show();
                }
                // Create the alert dialog using alert dialog builder
                val dialog = builder.create()
                // Finally, display the dialog when user press back button
                dialog.show()
            }else{
                val builder = AlertDialog.Builder(this)
                //            builder.setTitle("Peringatan");
                builder.setMessage("No Pengajuan: $noAju - $modul akan di reject!\nYakin akan me-reject?")
                builder.setCancelable(true)
                builder.setPositiveButton(
                    "Ya"
                ) {
                        _, _ ->
//                    Toast.makeText(this,"Hardcoded",Toast.LENGTH_SHORT).show()
                    sendApprove("RETURN") }
                builder.setNegativeButton(
                    "Tidak"
                ) { _, _ ->
                    // Do something when want to stay in the app
                    //                    Toast.makeText(getApplicationContext(),"thank you",Toast.LENGTH_LONG).show();
                }
                // Create the alert dialog using alert dialog builder
                val dialog = builder.create()
                // Finally, display the dialog when user press back button
                dialog.show()
            }
        }
    }

//    fun sendApprove(tanggal : String?, modul : String?,  status : String?,no_aju : String?, keterangan : String?) {
//        fun sendApprove(tanggal : String?, modul : String?,  status : String?,no_aju : String?, keterangan : String?) {
//        val apiservice= UtilsApi().getAPIService(this)
//        apiservice?.approval(modul,status,no_aju,keterangan)?.enqueue(object : Callback<ResponseBody?> {
//            override fun onResponse(
//                call: Call<ResponseBody?>,
//                response: Response<ResponseBody?>
//            ) {
//                if (response.isSuccessful) {
//                    if (response.body() != null) {
//                        val obj = JSONObject(response.body()!!.string())
////                        Toast.makeText(context, obj.optString("id")+" \n "+obj.optString("message"), Toast.LENGTH_SHORT).show()
////                        val builder = AlertDialog.Builder(context)
////                        //            builder.setTitle("Peringatan");
////                        builder.setMessage("Kembali ke Home?")
////                        builder.setCancelable(true)
////                        builder.setPositiveButton(
////                            "Ya"
////                        ) {
////                                _, _ ->
//                            val intent = Intent(context, SuccessActivity::class.java)
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                            startActivity(intent)
////                        }
////                        builder.setNegativeButton(
////                            "Tidak"
////                        ) { _, _ ->
////
////                        }
////                        val dialog = builder.create()
////                        dialog.show()
//                    }else{
//                        //                        if (data.length() > 0) {
//                        Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
//                    }
//                } else if(response.code() == 422) {
//                    Toast.makeText(context, "Keterangan Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
//                } else if(response.code() == 401){
//                    Toast.makeText(context, "Format Keterangan Salah", Toast.LENGTH_SHORT).show()
//                } else if(response.code() == 403){
//                    Toast.makeText(context, "Token Invalid", Toast.LENGTH_SHORT).show()
//                } else if(response.code() == 404){
//                    Toast.makeText(context, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
//                Toast.makeText(context, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

    fun sendApprove(stat:String) {
        val tanggal: String =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val apiservice= UtilsApi().getAPIService(this@ProsesActivity)
        apiservice?.approval(noAju,stat,input_keterangan.text.toString(),noUrut,tanggal)?.enqueue(object :
            Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            val obj = JSONObject(response.body()!!.string())
                            val idDevice = obj.optString("id_device")
                            val nik = obj.optString("nik_app")
                            sendNotification(idDevice,nik)
                            Toast.makeText(this@ProsesActivity, obj.optString("message"), Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@ProsesActivity, MainActivity::class.java)
                            intent.putExtra("history",true)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                            startActivity(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }else{
                        Toast.makeText(this@ProsesActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if(response.code() == 422) {
                    Toast.makeText(this@ProsesActivity, "Keterangan Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 401){
                    Toast.makeText(this@ProsesActivity, "Format Keterangan Salah", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 403){
                    Toast.makeText(this@ProsesActivity, "Token Invalid", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 404){
                    Toast.makeText(this@ProsesActivity, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@ProsesActivity, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendNotification(idDevice:String,nik:String) {

        val title  = "Pengajuan Pembayaran"
        val message  = "Pengajuan Pembayaran $noAju menunggu approval anda."

        val apiservice = UtilsApi().getAPIService(this@ProsesActivity!!)
        apiservice?.sendNotif(idDevice,title,message,nik)?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        try {
                            val obj = JSONObject(response.body()!!.string())
                            Log.e("Response:", obj.toString())
//                                if(obj.optString("status").isNotEmpty()&&obj.optString("status")=="false"){
//                                    Toast.makeText(this@ProsesActivity,"Terjadi kesalahan notif",Toast.LENGTH_SHORT).show()
//                                }

                        } catch (e: Exception) {

                        }
                    }else{
                        Toast.makeText(this@ProsesActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if(response.code() == 422) {
                    Toast.makeText(this@ProsesActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 401){
                    val intent = Intent(this@ProsesActivity, LoginActivity::class.java)
                    startActivity(intent)
                    preferences.preferencesLogout()
                    finishAffinity()
                    Toast.makeText(this@ProsesActivity, "Sesi telah berakhir, silahkan login kembali", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 403){
                    Toast.makeText(this@ProsesActivity, "Unauthorized", Toast.LENGTH_SHORT).show()
                } else if(response.code() == 404){
                    Toast.makeText(this@ProsesActivity, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody?>, t: Throwable) {
                Toast.makeText(this@ProsesActivity, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })


//        try {
//            val instance = FCMInstance
//            instance.setContext(context!!)
//            val response = instance.api.sendNotif("notification")
//            if(response.isSuccessful){
//                Log.d(TAG,"Response: ${Gson().toJson(response)}")
//            }else{
//                Log.d(TAG,response.errorBody().toString())
//            }
//        } catch (e: Exception) {
//            Log.d(TAG,e.toString())
//        }
    }

}
