package com.saku.approval_sju.models

import com.google.gson.annotations.SerializedName

data class ModelBudgetJurnal(

	@field:SerializedName("kode_akun")
	val kodeAkun: String? = null,

	@field:SerializedName("keterangan")
	val keterangan: String? = null,

	@field:SerializedName("nama_akun")
	val namaAkun: String? = null,

	@field:SerializedName("nilai")
	val nilai: String? = null,

	@field:SerializedName("kode_proyek")
	val kodeProyek: String? = null,

	@field:SerializedName("nama_pp")
	val namaPp: String? = null,

	@field:SerializedName("nama_proyek")
	val namaProyek: String? = null,

	@field:SerializedName("dc")
	val dc: String? = null,

	@field:SerializedName("kode_pp")
	val kodePp: String? = null
)