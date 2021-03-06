package com.saku.approval_sju.models

import com.google.gson.annotations.SerializedName

class ModelDataPengajuan {
    @SerializedName("due_date")
    var due_date: String? = null
    @SerializedName("no_pb")
    var no_pb: String? = null
    @SerializedName("status")
    var status: String? = null
    @SerializedName("tgl")
    var tgl: String? = null
    @SerializedName("tgl2")
    var tgl2: String? = null
    @SerializedName("modul")
    var modul: String? = null
    @SerializedName("nama_pp")
    var nama_pp: String? = null
    @SerializedName("kode_pp")
    var kode_pp: String? = null
    @SerializedName("no_dokumen")
    var no_dokumen: String? = null
    @SerializedName("keterangan")
    var keterangan: String? = null
    @SerializedName("nilai")
    var nilai: String? = null
    @SerializedName("nama_buat")
    var nama_buat: String? = null
    @SerializedName("no_app2")
    var no_app2: String? = null
    @SerializedName("kode_lokasi")
    var kode_lokasi: String? = null
    @SerializedName("tgl_input")
    var tgl_input: String? = null
}
