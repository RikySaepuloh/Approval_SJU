package com.saku.approval_sju.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saku.approval_sju.DetailPengajuanActivity
import com.saku.approval_sju.R
import com.saku.approval_sju.models.ModelDataPengajuan
import kotlinx.android.synthetic.main.layout_pengajuan.view.*
import kotlin.collections.ArrayList

class HistoryPengajuanAdapter(private val rawData:ArrayList<ModelDataPengajuan>) :
    RecyclerView.Adapter<HistoryPengajuanAdapter.DataPengajuanViewHolder>() {
    private var mFilteredList: ArrayList<ModelDataPengajuan>? = rawData
    private var mArrayList: ArrayList<ModelDataPengajuan>? = rawData
    var context : Context? = null
    
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataPengajuanViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        this.context = parent.context
        val view: View = layoutInflater.inflate(R.layout.layout_pengajuan, parent, false)
        return DataPengajuanViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: DataPengajuanViewHolder, position: Int) {
        if (mFilteredList?.get(position)?.status.equals("APPROVE")){
            holder.tvDesc?.text = mFilteredList?.get(position)?.keterangan
            holder.tvPembuat?.text = mFilteredList?.get(position)?.pembuat
            holder.tvDue?.text = mFilteredList?.get(position)?.no_bukti+" - "+mFilteredList?.get(position)?.due_date
            holder.tvTotal?.text = mFilteredList?.get(position)?.nilai + " IDR"
            holder.btnStatus?.setImageResource(R.drawable.ic_approved)
        }else if (mFilteredList?.get(position)?.status.equals("REJECT")){
            holder.tvDesc?.text = mFilteredList?.get(position)?.keterangan
            holder.tvPembuat?.text = mFilteredList?.get(position)?.pembuat
            holder.tvDue?.text = mFilteredList?.get(position)?.no_bukti+" - "+mFilteredList?.get(position)?.due_date
            holder.tvTotal?.text = mFilteredList?.get(position)?.nilai + " IDR"
            holder.btnStatus?.setImageResource(R.drawable.ic_rejected)
        }else{
            holder.tvDesc?.text = mFilteredList?.get(position)?.keterangan
            holder.tvPembuat?.text = mFilteredList?.get(position)?.pembuat
            holder.tvDue?.text = mFilteredList?.get(position)?.no_bukti+" - "+mFilteredList?.get(position)?.due_date
            holder.tvTotal?.text = mFilteredList?.get(position)?.nilai + " IDR"
        }

        holder.btnStatus?.setOnClickListener {
            val intent = Intent(context, DetailPengajuanActivity::class.java)
                .apply {
                    putExtra("no_aju", mFilteredList?.get(position)?.no_bukti)
                    putExtra("modul", mFilteredList?.get(position)?.modul)
                    putExtra("displayopt", "history")
                }
            context?.startActivity(intent)
        }

    }
    fun getFilter(): Filter? {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults? {
                val charString = charSequence.toString().toLowerCase()
                mFilteredList = if (charString.isEmpty()) {
                    mArrayList
                } else {
                    val filteredList: ArrayList<ModelDataPengajuan> = ArrayList()
                    for (dataPengajuan in mArrayList!!) {
                        if (
                            dataPengajuan.no_bukti?.toLowerCase()?.contains(charString)!!||dataPengajuan.kode_pp?.toLowerCase()?.contains(charString)!!||
                            dataPengajuan.pembuat?.toLowerCase()?.contains(charString)!!||dataPengajuan.keterangan?.toLowerCase()?.contains(charString)!!||
                            dataPengajuan.due_date?.toLowerCase()?.contains(charString)!!
                        ) {
                            filteredList.add(dataPengajuan)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = mFilteredList
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults
            ) {
                mFilteredList = filterResults.values as ArrayList<ModelDataPengajuan>?
                notifyDataSetChanged()

            }
        }
    }
    override fun getItemCount(): Int {
        return mFilteredList!!.size
    }

    class DataPengajuanViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        val tvDesc : TextView? = itemView?.tv_desc
        val tvPembuat : TextView? = itemView?.tv_pembuat
        val tvDue : TextView? = itemView?.tv_due_date
        val tvTotal : TextView? = itemView?.tv_total
        val btnStatus : ImageView? = itemView?.btn_status

    }
}