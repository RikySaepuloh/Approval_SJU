package com.saku.approval_sju.models

import com.google.gson.annotations.SerializedName

class ModelInfoPengajuan {

    @SerializedName("pp")
    private var pp: String? = null

    @SerializedName("tglinput")
    private var tglinput: String? = null

    @SerializedName("no_bukti")
    private var noBukti: String? = null

    @SerializedName("keterangan")
    private var keterangan: String? = null

    @SerializedName("kode_lokasi")
    private var kodeLokasi: String? = null

    @SerializedName("nilai")
    private var nilai: String? = null

    @SerializedName("due_date")
    private var dueDate: String? = null

    @SerializedName("modul")
    private var modul: String? = null

    @SerializedName("tgl2")
    private var tgl2: String? = null

    @SerializedName("kode_pp")
    private var kodePp: String? = null

    @SerializedName("no_app2")
    private var noApp2: String? = null

    @SerializedName("tgl")
    private var tgl: String? = null

    @SerializedName("no_dokumen")
    private var noDokumen: String? = null

    @SerializedName("status")
    private var status: String? = null

    @SerializedName("pembuat")
    private var pembuat: String? = null

    @SerializedName("catatan")
    private var catatan: String? = null

    fun setCatatan(catatan: String?) {
        this.catatan = catatan
    }

    fun getCatatan(): String? {
        return catatan
    }

    fun setPp(pp: String?) {
        this.pp = pp
    }

    fun getPp(): String? {
        return pp
    }

    fun setTglinput(tglinput: String?) {
        this.tglinput = tglinput
    }

    fun getTglinput(): String? {
        return tglinput
    }

    fun setNoBukti(noBukti: String?) {
        this.noBukti = noBukti
    }

    fun getNoBukti(): String? {
        return noBukti
    }

    fun setKeterangan(keterangan: String?) {
        this.keterangan = keterangan
    }

    fun getKeterangan(): String? {
        return keterangan
    }

    fun setKodeLokasi(kodeLokasi: String?) {
        this.kodeLokasi = kodeLokasi
    }

    fun getKodeLokasi(): String? {
        return kodeLokasi
    }

    fun setNilai(nilai: String?) {
        this.nilai = nilai
    }

    fun getNilai(): String? {
        return nilai
    }

    fun setDueDate(dueDate: String?) {
        this.dueDate = dueDate
    }

    fun getDueDate(): String? {
        return dueDate
    }

    fun setModul(modul: String?) {
        this.modul = modul
    }

    fun getModul(): String? {
        return modul
    }

    fun setTgl2(tgl2: String?) {
        this.tgl2 = tgl2
    }

    fun getTgl2(): String? {
        return tgl2
    }

    fun setKodePp(kodePp: String?) {
        this.kodePp = kodePp
    }

    fun getKodePp(): String? {
        return kodePp
    }

    fun setNoApp2(noApp2: String?) {
        this.noApp2 = noApp2
    }

    fun getNoApp2(): String? {
        return noApp2
    }

    fun setTgl(tgl: String?) {
        this.tgl = tgl
    }

    fun getTgl(): String? {
        return tgl
    }

    fun setNoDokumen(noDokumen: String?) {
        this.noDokumen = noDokumen
    }

    fun getNoDokumen(): String? {
        return noDokumen
    }

    fun setStatus(status: String?) {
        this.status = status
    }

    fun getStatus(): String? {
        return status
    }

    fun setPembuat(pembuat: String?) {
        this.pembuat = pembuat
    }

    fun getPembuat(): String? {
        return pembuat
    }

}