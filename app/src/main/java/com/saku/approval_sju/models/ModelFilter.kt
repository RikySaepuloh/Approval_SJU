package com.saku.approval_sju.models

import com.google.gson.annotations.SerializedName

data class ModelFilter(

	@field:SerializedName("id")
	var id: String? = null,

	@field:SerializedName("value")
	var value: String? = null,

	@field:SerializedName("param")
	var param: String? = null

)