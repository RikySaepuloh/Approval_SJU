package com.saku.approval_sju.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.saku.approval_sju.R
import com.saku.approval_sju.models.ModelNotifikasi
import kotlinx.android.synthetic.main.layout_notifikasi.view.*

class NotifikasiAdapter(private val context: Context) : RecyclerView.Adapter<NotifikasiAdapter.NamaKelompokViewHolder>() {
    private val dataArray= mutableListOf<ModelNotifikasi>()
    fun initData(data:MutableList<ModelNotifikasi>){
        dataArray.clear()
        dataArray.addAll(data)
        dataArray.sortByDescending { it.tglInput }
        notifyDataSetChanged()
    }

    fun clearData(){
        dataArray.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NamaKelompokViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_notifikasi, parent, false)
        return NamaKelompokViewHolder(view)
    }

    override fun onBindViewHolder(holder: NamaKelompokViewHolder, position: Int) {
        holder.title.text = dataArray[position].judul
        holder.catatan.text = dataArray[position].pesan
        holder.tanggal.text = dataArray[position].tglInput
    }

    override fun getItemCount(): Int {
        return dataArray.size
    }

    inner class NamaKelompokViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.title
        val catatan: TextView = itemView.catatan
        val tanggal: TextView = itemView.tanggal
        val layout: LinearLayout = itemView.layout_notifikasi
    }

}