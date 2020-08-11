package com.saku.approval_sju.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.saku.approval_sju.R
import com.saku.approval_sju.models.ModelHistoryApproval
import kotlinx.android.synthetic.main.layout_flow_dokumen.view.*


class FlowDokumen(rawData:ArrayList<ModelHistoryApproval>) :
    RecyclerView.Adapter<FlowDokumen.DataPengajuanViewHolder>() {
    var context : Context? = null
    private var mFilteredList: ArrayList<ModelHistoryApproval>? = rawData
    private var mArrayList: ArrayList<ModelHistoryApproval>? = rawData


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataPengajuanViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        this.context=parent.context
        val view: View = layoutInflater.inflate(R.layout.layout_flow_dokumen, parent, false)
        return DataPengajuanViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: DataPengajuanViewHolder, position: Int) {
        holder.tvCatatan?.text = "Catatan :\n"+mFilteredList?.get(position)?.catatan
        holder.tvForm?.text = mFilteredList?.get(position)?.modul
        holder.tvNama?.text = mFilteredList?.get(position)?.nama
        holder.tvNik?.text = mFilteredList?.get(position)?.nik_user
        holder.tvNoApp?.text = mFilteredList?.get(position)?.no_app
        holder.tvTgl?.text = mFilteredList?.get(position)?.tgl
        if (mFilteredList?.get(position)?.status?.toLowerCase() == "approve"){
            holder.btnStatus?.setImageResource(R.drawable.ic_approved)
        }else{
            holder.btnStatus?.setImageResource(R.drawable.ic_rejected)
        }


    }

    override fun getItemCount(): Int {
        return mFilteredList?.size!!
    }

    class DataPengajuanViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        val tvNoApp : TextView? = itemView?.tv_no_approval
        val tvForm : TextView? = itemView?.tv_form
        val tvCatatan : TextView? = itemView?.tv_catatan
        val tvNama : TextView? = itemView?.tv_nama
        val tvNik : TextView? = itemView?.tv_nik
        val tvTgl : TextView? = itemView?.tv_tgl_approval
        val btnStatus : ImageView? = itemView?.btn_status

    }
}