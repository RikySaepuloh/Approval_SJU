package com.saku.approval_sju.models;

import com.google.gson.annotations.SerializedName;

public class ProfileKodeFS {

	@SerializedName("kode_fs")
	private String kodeFs;

	public void setKodeFs(String kodeFs){
		this.kodeFs = kodeFs;
	}

	public String getKodeFs(){
		return kodeFs;
	}

	@Override
 	public String toString(){
		return 
			"KodeFsItem{" + 
			"kode_fs = '" + kodeFs + '\'' + 
			"}";
		}
}