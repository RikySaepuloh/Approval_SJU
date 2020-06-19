package com.saku.approval_sju.adapter

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.saku.approval_sju.R
import com.saku.approval_sju.models.ModelDokumen
import kotlinx.android.synthetic.main.layout_dokumen.view.*


class DokumenAdapter(rawData: ArrayList<ModelDokumen>) :
    RecyclerView.Adapter<DokumenAdapter.DataPengajuanViewHolder>() {
    var context : Context? = null
    private var mFilteredList: ArrayList<ModelDokumen>? = rawData
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DataPengajuanViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        this.context=parent.context
        val view: View = layoutInflater.inflate(R.layout.layout_dokumen, parent, false)
        return DataPengajuanViewHolder(
            view
        )
    }

    override fun onBindViewHolder(holder: DataPengajuanViewHolder, position: Int) {
        holder.nama?.text = mFilteredList?.get(position)?.no_gambar
        val rawLink = mFilteredList?.get(position)?.host.toString().replace("\\","")
        val filename = mFilteredList?.get(position)?.no_gambar.toString()
        val filedesc = mFilteredList?.get(position)?.kode_jenis.toString()
        holder.link?.setOnClickListener {
            val url = "$rawLink/$filename"
            val request =
                DownloadManager.Request(Uri.parse(url))
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
            request.setDescription(filedesc)
            request.setAllowedOverRoaming(true)
            request.setTitle(filename)
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                filename
            )
            val manager =
                context!!.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            manager!!.enqueue(request)
            Toast.makeText(context, "Download sedang berlangsung", Toast.LENGTH_SHORT).show()
        }
    }

    override fun getItemCount(): Int {
        return mFilteredList?.size!!
    }

    class DataPengajuanViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        val link : TextView? = itemView?.tv_link
        val nama : TextView? = itemView?.tv_nama_link
    }
}