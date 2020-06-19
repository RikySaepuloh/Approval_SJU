package com.saku.approval_sju.models;

import com.google.gson.annotations.SerializedName;

public class ProfilePeriode {

	@SerializedName("periode")
	private String periode;

	public void setPeriode(String periode){
		this.periode = periode;
	}

	public String getPeriode(){
		return periode;
	}

	@Override
 	public String toString(){
		return 
			"PeriodeItem{" + 
			"periode = '" + periode + '\'' + 
			"}";
		}
}