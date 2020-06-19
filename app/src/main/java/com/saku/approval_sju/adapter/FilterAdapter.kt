package com.saku.approval_sju.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.saku.approval_sju.R
import com.saku.approval_sju.database.FilterHandler
import com.saku.approval_sju.models.ModelFilter
import kotlinx.android.synthetic.main.layout_filter.view.*


class FilterAdapter(rawData: ArrayList<String?>, var judulFilter: String?) :
    RecyclerView.Adapter<FilterAdapter.DataPengajuanViewHolder>() {
    var context : Context? = null
    private var mFilteredList: ArrayList<String?>? = rawData
    var dbhandler : FilterHandler? = null
    var data: ArrayList<ModelFilter>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataPengajuanViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        this.context=parent.context
        this.dbhandler = FilterHandler(
            parent.context,
            null,
            null,
            1
        )
        this.data = dbhandler!!.getAllFilter()
        val view: View = layoutInflater.inflate(R.layout.layout_filter, parent, false)
        return DataPengajuanViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: DataPengajuanViewHolder, position: Int) {
        holder.isiFilter?.text = mFilteredList?.get(position)
//        TODO("IF ELSE MODEL FILTER")
        Log.e("ini isi data",data.toString())
        if (data!!.contains(
                ModelFilter(
                    "",
                    mFilteredList?.get(position).toString()
                )
            )){
            holder.isiFilter?.setBackgroundResource(R.drawable.bg_primary_rounded_nopadd)
            holder.isiFilter?.setTextColor(ContextCompat.getColor(context!!,R.color.white))
        }else{
            holder.isiFilter?.setBackgroundResource(R.drawable.bg_softgrey_outline)
            holder.isiFilter?.setTextColor(ContextCompat.getColor(context!!,R.color.grey))
        }

        holder.isiFilter?.setOnClickListener {
            if (holder.isiFilter.textColors == ContextCompat.getColorStateList(context!!,R.color.white)){
                holder.isiFilter.setBackgroundResource(R.drawable.bg_softgrey_outline)
                holder.isiFilter.setTextColor(ContextCompat.getColor(context!!,R.color.grey))
                dbhandler?.deleteFilter(mFilteredList?.get(position).toString())
            }else{
                if (judulFilter.equals("PP")){
                    dbhandler?.addKodePP(mFilteredList?.get(position).toString())
                }else{
                    dbhandler?.addModul(mFilteredList?.get(position).toString())
                }
                holder.isiFilter.setBackgroundResource(R.drawable.bg_primary_rounded_nopadd)
                holder.isiFilter.setTextColor(ContextCompat.getColor(context!!,R.color.white))
            }
        }
    }

    override fun getItemCount(): Int {
        return mFilteredList?.size!!
    }

    class DataPengajuanViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        val isiFilter : TextView? = itemView?.isi_filter
    }
}