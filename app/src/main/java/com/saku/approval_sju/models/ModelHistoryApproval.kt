package com.saku.approval_sju.models

import com.google.gson.annotations.SerializedName

data class ModelHistoryApproval(

	@field:SerializedName("no_app")
	val no_app: String? = null,

	@field:SerializedName("tgl")
	val tgl: String? = null,

	@field:SerializedName("nik_user")
	val nik_user: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("form")
	val form: String? = null,

	@field:SerializedName("status")
	val status: String? = null,
	
	@field:SerializedName("catatan")
	val catatan: String? = null

)