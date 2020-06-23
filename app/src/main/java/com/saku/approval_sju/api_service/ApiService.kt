package com.saku.approval_sju.api_service

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("nik") nik: String?,
        @Field("password") password: String?
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("app")
    fun approval(
//        @Field("tanggal") tanggal: String?,
        @Field("modul") modul: String?,
        @Field("status") status: String?,
        @Field("no_aju") no_aju: String?,
        @Field("keterangan") keterangan: String?
    ): Call<ResponseBody>

    @Streaming
    @GET
    fun download(@Url urlString: String?): Call<ResponseBody?>

    @GET("aju")
    fun daftarpengajuan(
        @Query("periode") periode:String?
    ): Call<ResponseBody>

    @GET("periode_aju")
    fun periode(
    ): Call<ResponseBody>

    @GET("aju_history/all")
    fun daftarhistoryall(
    ): Call<ResponseBody>

    @GET("aju_history/approve")
    fun daftarhistoryapprove(
    ): Call<ResponseBody>

    @GET("aju_history/reject")
    fun daftarhistoryreject(
    ): Call<ResponseBody>

    @GET("ajudet/{no_aju}")
    fun detailpengajuan(
        @Path("no_aju") no_aju: String?
    ): Call<ResponseBody>

    @GET("ajudet_dok/{no_aju}")
    fun detaildokumen(
        @Path("no_aju") no_aju: String?
    ): Call<ResponseBody>

    @GET("ajudet_history/{no_aju}")
    fun detailriwayat(
        @Path("no_aju") no_aju: String?
    ): Call<ResponseBody>

    @GET("ajudet_approval/{no_aju}")
    fun detailflow(
        @Path("no_aju") no_aju: String?
    ): Call<ResponseBody>

    @GET("ajurek/{no_aju}")
    fun detailrekening(
        @Path("no_aju") no_aju: String?
    ): Call<ResponseBody>

    @GET("ajujurnal/{no_aju}")
    fun detailjurnal(
        @Path("no_aju") no_aju: String?
    ): Call<ResponseBody>

    @GET("profile")
    fun user(
    ): Call<ResponseBody>
}