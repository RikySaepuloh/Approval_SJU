package com.saku.approval_sju.models;

import com.google.gson.annotations.SerializedName;

public class ProfileUser {

	@SerializedName("path_view")
	private String pathView;

	@SerializedName("kode_klp_menu")
	private String kodeKlpMenu;

	@SerializedName("kode_lokasi")
	private String kodeLokasi;

	@SerializedName("jabatan")
	private String jabatan;

	@SerializedName("nmlok")
	private String nmlok;

	@SerializedName("kode_pp")
	private String kodePp;

	@SerializedName("nik")
	private String nik;

	@SerializedName("kode_lokkonsol")
	private String kodeLokkonsol;

	@SerializedName("nama")
	private String nama;

	@SerializedName("foto")
	private String foto;

	@SerializedName("kode_bidang")
	private String kodeBidang;

	@SerializedName("nama_pp")
	private String namaPp;

	@SerializedName("logo")
	private String logo;

	@SerializedName("no_telp")
	private String noTelp;

	@SerializedName("status_admin")
	private String statusAdmin;

	@SerializedName("klp_akses")
	private String klpAkses;

	public void setPathView(String pathView){
		this.pathView = pathView;
	}

	public String getPathView(){
		return pathView;
	}

	public void setKodeKlpMenu(String kodeKlpMenu){
		this.kodeKlpMenu = kodeKlpMenu;
	}

	public String getKodeKlpMenu(){
		return kodeKlpMenu;
	}

	public void setKodeLokasi(String kodeLokasi){
		this.kodeLokasi = kodeLokasi;
	}

	public String getKodeLokasi(){
		return kodeLokasi;
	}

	public void setJabatan(String jabatan){
		this.jabatan = jabatan;
	}

	public String getJabatan(){
		return jabatan;
	}

	public void setNmlok(String nmlok){
		this.nmlok = nmlok;
	}

	public String getNmlok(){
		return nmlok;
	}

	public void setKodePp(String kodePp){
		this.kodePp = kodePp;
	}

	public String getKodePp(){
		return kodePp;
	}

	public void setNik(String nik){
		this.nik = nik;
	}

	public String getNik(){
		return nik;
	}

	public void setKodeLokkonsol(String kodeLokkonsol){
		this.kodeLokkonsol = kodeLokkonsol;
	}

	public String getKodeLokkonsol(){
		return kodeLokkonsol;
	}

	public void setNama(String nama){
		this.nama = nama;
	}

	public String getNama(){
		return nama;
	}

	public void setFoto(String foto){
		this.foto = foto;
	}

	public String getFoto(){
		return foto;
	}

	public void setKodeBidang(String kodeBidang){
		this.kodeBidang = kodeBidang;
	}

	public String getKodeBidang(){
		return kodeBidang;
	}

	public void setNamaPp(String namaPp){
		this.namaPp = namaPp;
	}

	public String getNamaPp(){
		return namaPp;
	}

	public void setLogo(String logo){
		this.logo = logo;
	}

	public String getLogo(){
		return logo;
	}

	public void setNoTelp(String noTelp){
		this.noTelp = noTelp;
	}

	public String getNoTelp(){
		return noTelp;
	}

	public void setStatusAdmin(String statusAdmin){
		this.statusAdmin = statusAdmin;
	}

	public String getStatusAdmin(){
		return statusAdmin;
	}

	public void setKlpAkses(String klpAkses){
		this.klpAkses = klpAkses;
	}

	public String getKlpAkses(){
		return klpAkses;
	}

	@Override
 	public String toString(){
		return 
			"UserItem{" + 
			"path_view = '" + pathView + '\'' + 
			",kode_klp_menu = '" + kodeKlpMenu + '\'' + 
			",kode_lokasi = '" + kodeLokasi + '\'' + 
			",jabatan = '" + jabatan + '\'' + 
			",nmlok = '" + nmlok + '\'' + 
			",kode_pp = '" + kodePp + '\'' + 
			",nik = '" + nik + '\'' + 
			",kode_lokkonsol = '" + kodeLokkonsol + '\'' + 
			",nama = '" + nama + '\'' + 
			",foto = '" + foto + '\'' + 
			",kode_bidang = '" + kodeBidang + '\'' + 
			",nama_pp = '" + namaPp + '\'' + 
			",logo = '" + logo + '\'' + 
			",no_telp = '" + noTelp + '\'' + 
			",status_admin = '" + statusAdmin + '\'' + 
			",klp_akses = '" + klpAkses + '\'' + 
			"}";
		}
}