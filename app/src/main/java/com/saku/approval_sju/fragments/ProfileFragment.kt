package com.saku.approval_sju.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saku.approval_sju.LoginActivity
import com.saku.approval_sju.Preferences
import com.saku.approval_sju.R
import com.saku.approval_sju.api_service.UtilsApi
import com.saku.approval_sju.models.ProfileUser
import kotlinx.android.synthetic.main.fragment_profile.view.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type


class ProfileFragment : Fragment() {
    private lateinit var myview: View
    val linkImg = "http://bangtelindo.simkug.com/server/media/"
    var preferences  = Preferences()
    var myctx : Context? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        myview = inflater.inflate(R.layout.fragment_profile, container, false)

        return myview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myctx=context
        preferences.setPreferences(myctx!!)
        initData(preferences.getToken(),preferences.getTokenType())
        myview.tv_logout.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            preferences.preferencesLogout()
            activity?.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true

    }

    fun initData(token: String?, bearer : String?) {
        val apiservice= UtilsApi().getAPIService(activity!!.applicationContext)
        apiservice?.user()?.enqueue(object : Callback<ResponseBody?> {
            override fun onResponse(
                call: Call<ResponseBody?>,
                response: Response<ResponseBody?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        val obj = JSONObject(response.body()!!.string())
                        val gson = Gson()
                        val type: Type = object :
                            TypeToken<ArrayList<ProfileUser?>?>() {}.type
                        val datapengajuan: ArrayList<ProfileUser> =
                            gson.fromJson(obj.optString("user"), type)
                        for (data in datapengajuan) {
                            Glide.with(myctx!!).load(data.foto).placeholder(R.drawable.ic_profile).into(myview.profileImage);
                            myview.profileName.text = data.nama
                            myview.profileNik.text = data.nik
                            myview.profileNotelp.text = data.noTelp
                            myview.profileJabatan.text = data.jabatan
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


    companion object {
        fun newInstance(): ProfileFragment =
            ProfileFragment()
    }
}