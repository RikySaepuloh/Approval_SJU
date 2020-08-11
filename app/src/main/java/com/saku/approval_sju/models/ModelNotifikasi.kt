package com.saku.approval_sju.models

import com.google.gson.annotations.SerializedName

data class ModelNotifikasi(

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("judul")
	val judul: String? = null,

	@field:SerializedName("pesan")
	val pesan: String? = null,

	@field:SerializedName("tgl_input")
	val tglInput: String? = null
)