package com.saku.approval_sju.models

import com.google.gson.annotations.SerializedName

data class ModelDokumen(

	@field:SerializedName("no_gambar")
	val no_gambar: String? = null,

	@field:SerializedName("kode_jenis")
	val kode_jenis: String? = null,

	@field:SerializedName("host")
	val host: String? = null
)