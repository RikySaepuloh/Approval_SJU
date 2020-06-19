package com.saku.approval_sju.models;

import com.google.gson.annotations.SerializedName;

public class ModelBankTransfer {

	@SerializedName("nama_rek")
	private String namaRek;

	@SerializedName("bank")
	private String bank;

	@SerializedName("cabang")
	private String cabang;

	@SerializedName("no_rek")
	private String noRek;

	@SerializedName("pajak")
	private String pajak;

	@SerializedName("bruto")
	private String bruto;

	public void setNamaRek(String namaRek){
		this.namaRek = namaRek;
	}

	public String getNamaRek(){
		return namaRek;
	}

	public void setBank(String bank){
		this.bank = bank;
	}

	public String getBank(){
		return bank;
	}

	public void setCabang(String cabang){
		this.cabang = cabang;
	}

	public String getCabang(){
		return cabang;
	}

	public void setNoRek(String noRek){
		this.noRek = noRek;
	}

	public String getNoRek(){
		return noRek;
	}

	public void setPajak(String pajak){
		this.pajak = pajak;
	}

	public String getPajak(){
		return pajak;
	}

	public void setBruto(String bruto){
		this.bruto = bruto;
	}

	public String getBruto(){
		return bruto;
	}
}