package com.saku.approval_sju.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.saku.approval_sju.DetailPengajuanActivity
import com.saku.approval_sju.R
import com.saku.approval_sju.models.ModelDataPengajuan
import kotlinx.android.synthetic.main.layout_pengajuan.view.*


class DataPengajuanAdapter(rawData:ArrayList<ModelDataPengajuan>) :
    RecyclerView.Adapter<DataPengajuanAdapter.DataPengajuanViewHolder>() {
    var context : Context? = null
    private var mFilteredList: ArrayList<ModelDataPengajuan>? = rawData
    private var mArrayList: ArrayList<ModelDataPengajuan>? = rawData


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataPengajuanViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        this.context=parent.context
        val view: View = layoutInflater.inflate(R.layout.layout_pengajuan, parent, false)
        return DataPengajuanViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: DataPengajuanViewHolder, position: Int) {
        holder.tvDesc?.text = mFilteredList?.get(position)?.keterangan
        holder.tvPembuat?.text = mFilteredList?.get(position)?.nama_pp+" - "+mFilteredList?.get(position)?.nama_buat
        holder.tvDue?.text = mFilteredList?.get(position)?.no_pb +" - "+ mFilteredList?.get(position)?.due_date
//        holder.tvTotal?.text = mFilteredList?.get(position)?.nilai + " IDR"
//        val kursa : String = if(mFilteredList?.get(position)?.nilai?.length!! <=7){
//            " Rb IDR"
//        }else{
//            " Jt IDR"
//        }

        holder.tvTotal?.text = mFilteredList?.get(position)?.nilai + " IDR"
//
//        if(mFilteredList?.get(position)?.nilai!!.length<=7){
//            holder.tvTotal?.text = mFilteredList?.get(position)?.nilai?.substring(0,
//                mFilteredList?.get(position)?.nilai?.indexOf('.')!! +2)+kursa
//        }


        holder.layout?.setOnClickListener {
                        val intent = Intent(context, DetailPengajuanActivity::class.java)
                .apply {
                    putExtra("no_aju", mFilteredList?.get(position)?.no_pb)
                    putExtra("modul", mFilteredList?.get(position)?.modul)
                    putExtra("displayopt", "pengajuan")
                }
            context?.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return mFilteredList?.size!!
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
                            dataPengajuan.no_pb?.toLowerCase()?.contains(charString)!!||dataPengajuan.kode_pp?.toLowerCase()?.contains(charString)!!||
                            dataPengajuan.nama_buat?.toLowerCase()?.contains(charString)!!||dataPengajuan.keterangan?.toLowerCase()?.contains(charString)!!||
                            dataPengajuan.due_date?.toLowerCase()?.contains(charString)!!||dataPengajuan.nama_pp?.toLowerCase()?.contains(charString)!!
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

    class DataPengajuanViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        val tvDesc : TextView? = itemView?.tv_desc
        val tvPembuat : TextView? = itemView?.tv_pembuat
        val tvDue : TextView? = itemView?.tv_due_date
        val tvTotal : TextView? = itemView?.tv_total
        val btnDetail : ImageView? = itemView?.btn_status
        val layout : CardView? = itemView?.layout

    }
}