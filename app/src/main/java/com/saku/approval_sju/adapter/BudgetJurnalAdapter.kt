package com.saku.approval_sju.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.saku.approval_sju.R
import com.saku.approval_sju.models.ModelBudgetJurnal
import kotlinx.android.synthetic.main.layout_budget_jurnal.view.*


class BudgetJurnalAdapter(rawData:ArrayList<ModelBudgetJurnal>) :
    RecyclerView.Adapter<BudgetJurnalAdapter.DataBudgetJurnalViewHolder>() {
    var context : Context? = null
    private var mFilteredList: ArrayList<ModelBudgetJurnal>? = rawData
    private var mArrayList: ArrayList<ModelBudgetJurnal>? = rawData


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataBudgetJurnalViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        this.context=parent.context
        val view: View = layoutInflater.inflate(R.layout.layout_budget_jurnal, parent, false)
        return DataBudgetJurnalViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: DataBudgetJurnalViewHolder, position: Int) {
        if(mFilteredList?.get(position)?.dc.equals("D")){
            holder.tvJenisbj?.text = "Jurnal"
        }else{
            holder.tvJenisbj?.text = "Budget"
        }
//        val kursa : String = if(mFilteredList?.get(position)?.nilai?.length!! <=7){
//            " Rb IDR | "
//        }else{
//            " Jt IDR | "
//        }

                holder.tvNama?.text = mFilteredList?.get(position)?.namaAkun+"\n"+ mFilteredList?.get(position)?.namaPp

//        holder.tvNama?.text = (mFilteredList?.get(position)?.namaAkun
//                +"\n"+
//                mFilteredList?.get(position)?.nilai?.substring(0,
//                    mFilteredList?.get(position)?.nilai?.indexOf('.')!! +2)
//                +kursa+
//                mFilteredList?.get(position)?.namaPp
//                )
        holder.tvKeterangan?.text = (mFilteredList?.get(position)?.keterangan)
        holder.tvNilai?.text = (mFilteredList?.get(position)?.nilai+" IDR")
//        holder.tvKodepp?.text = (mFilteredList?.get(position)?.kodePp)
//        holder.tvNamapp?.text = (mFilteredList?.get(position)?.namaPp)
//        holder.tvKodeproyek?.text = (mFilteredList?.get(position)?.kodeProyek)
//        holder.tvNamaproyek?.text = (mFilteredList?.get(position)?.namaProyek)
        expand(holder.tvCardContent,holder.ivExpand)
        expandcollapse(holder.tvCardTitle,holder.tvCardContent,holder.ivExpand)
    }

    override fun getItemCount(): Int {
        return mFilteredList?.size!!
    }

    class DataBudgetJurnalViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        val tvJenisbj : TextView? = itemView?.tv_jenis_bj
        val tvCardTitle : RelativeLayout? = itemView?.card_title_bj
        val tvCardContent : LinearLayout? = itemView?.card_content_bj
        val tvNama : TextView? = itemView?.tv_nama_bj
        val tvKeterangan : TextView? = itemView?.tv_keterangan_bj
        val ivExpand : ImageView? = itemView?.iv_expand_bj
        val tvNilai : TextView? = itemView?.tv_nilai_bj
//        val tvKodepp : TextView? = itemView?.tv_kodepp_bj
//        val tvNamapp : TextView? = itemView?.tv_namapp_bj
//        val tvKodeproyek : TextView? = itemView?.tv_kodeproyek_bj
//        val tvNamaproyek : TextView? = itemView?.tv_namaproyek_bj
    }

    fun expandcollapse(title: RelativeLayout?, content: LinearLayout?, icon: ImageView?){
        title?.setOnClickListener {
            if (content?.visibility==View.GONE){
                TransitionManager.beginDelayedTransition(content!!,  AutoTransition())
                content.visibility=View.VISIBLE
                icon?.animate()?.rotationBy(180f)?.start()
//                expand(content,icon)
            }else{
                TransitionManager.beginDelayedTransition(content!!,  AutoTransition())
//        content.animate().translationY(0F);
                content.visibility=View.GONE
                icon?.animate()?.rotationBy(180f)?.start()
//                collapse(content,icon)
            }
        }
    }

    fun expand(content: LinearLayout?, icon: ImageView?){
//        content.animate().translationY(content.height.toFloat());
        TransitionManager.beginDelayedTransition(content!!,  AutoTransition())
        content.visibility=View.VISIBLE
        icon?.rotation = 180f
//        icon.animate().rotationBy(180f).start()
    }

    fun collapse(content: LinearLayout?, icon: ImageView?){
        TransitionManager.beginDelayedTransition(content!!,  AutoTransition())
//        content.animate().translationY(0F);
        content.visibility=View.GONE
        icon?.rotation = 0f
//        icon.animate().rotationBy(180f).start()
    }
}