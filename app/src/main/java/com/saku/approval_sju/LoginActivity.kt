package com.saku.approval_sju

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.saku.approval_sju.api_service.LoginResponse
import com.saku.approval_sju.api_service.UtilsApi
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    var spsave  = Preferences()
    var idDevice : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        spsave.setPreferences(this@LoginActivity)
        firebaseInstance()
        if(spsave.getLogStatus()){
            val intent =
                Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val etNik = findViewById<EditText>(R.id.et_nik);
        val etPassword = findViewById<EditText>(R.id.et_password);
        val btnLogin = findViewById<Button>(R.id.btn_login);
        version_name.text = "Versi "+BuildConfig.VERSION_NAME

        btnLogin.setOnClickListener {
            login(etNik.text.toString(),etPassword.text.toString(),idDevice!!)
        }
    }

    fun firebaseInstance(){
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FIREBASE", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                idDevice = task.result?.token
                Log.e("FIREBASE",idDevice)

                // Log and toast
//                val msg = getString(R.string.msg_token_fmt, token)
//                Log.d(TAG, msg)
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            })

    }

    fun login(nik: String?, password : String?,idDevice:String) {
        val utilsapi= UtilsApi().getAPIService(this)
        utilsapi?.login(nik,password,idDevice)?.enqueue(object : Callback<LoginResponse?> {
            override fun onResponse(
                call: Call<LoginResponse?>,
                response: Response<LoginResponse?>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        spsave.saveToken(response.body()!!.token.toString())
                        spsave.saveExpires(response.body()!!.expires_in.toString())
                        spsave.saveTokenType(response.body()!!.token_type.toString())
                        spsave.saveLogStatus(true)

//                        Toast.makeText(this@LoginActivity, spsave.getToken().toString(), Toast.LENGTH_SHORT).show()
                        val intent =
                                Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        Toast.makeText(this@LoginActivity, "Terjadi kesalahan", Toast.LENGTH_SHORT).show()
                    }
                } else if(response.code().equals(422)) {
                    Toast.makeText(this@LoginActivity, "Username/Password masih kosong", Toast.LENGTH_SHORT).show()
                } else if(response.code().equals(401)){
                    Toast.makeText(this@LoginActivity, "Username/Password salah", Toast.LENGTH_SHORT).show()
                } else if(response.code().equals(403)){
                    Toast.makeText(this@LoginActivity, "Token Invalid", Toast.LENGTH_SHORT).show()
                } else if(response.code().equals(404)||response.code().equals(405)){
                    Toast.makeText(this@LoginActivity, "Terjadi kesalahan server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse?>, t: Throwable) {
//                    progressBar.setVisibility(View.GONE)
                    Toast.makeText(this@LoginActivity, "Koneksi Bermasalah", Toast.LENGTH_SHORT).show()
            }
        })
//        fun login(nik:String?,password:String?){
//
//        }
    }




}
