package com.saku.approval_sju.adapter

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.saku.approval_sju.FileViewerActivity
import com.saku.approval_sju.R
import com.saku.approval_sju.models.ModelDokumen
import kotlinx.android.synthetic.main.layout_dokumen.view.*


class DokumenAdapter(rawData: ArrayList<ModelDokumen>) :
    RecyclerView.Adapter<DokumenAdapter.DataPengajuanViewHolder>() {
    lateinit var context : Context
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
        val rawLink = "https://newsju.simkug.com/server/media/"
//            mFilteredList?.get(position)?.host.toString().replace("\\","")
        val filename = mFilteredList?.get(position)?.no_gambar.toString()
        val filedesc = mFilteredList?.get(position)?.kode_jenis.toString()
        val typefile = mFilteredList?.get(position)?.no_gambar?.substringAfterLast(".")
        val file = rawLink+filename
        if(typefile == "pdf"){
            holder.filetype?.background = ContextCompat.getDrawable(context,R.drawable.file_type_box_red)
            holder.filetype?.text = "PDF"
            holder.link?.setOnClickListener {
                val intent = Intent(context, FileViewerActivity::class.java)
                    .apply {
                        putExtra("link",file)
                        putExtra("status","pdf")
                    }
                context.startActivity(intent)
            }
        }else if(typefile.equals("xls")||typefile.equals("xlsx")){
            holder.filetype?.background = ContextCompat.getDrawable(context,R.drawable.file_type_box_green)
            holder.filetype?.text = "XLS"
            holder.link?.text = "Download"
            holder.link?.setOnClickListener {
                downloadBerkas(rawLink, filename)
            }
        }else if(typefile.equals("png")||typefile.equals("jpg")||typefile.equals("jpeg")){
            holder.filetype?.background = ContextCompat.getDrawable(context,R.drawable.file_type_box_grey)
            holder.filetype?.text = "IMG"
            holder.link?.setOnClickListener {
                val intent = Intent(context, FileViewerActivity::class.java)
                    .apply {
                        putExtra("link",file)
                        putExtra("status","img")
                    }
                context.startActivity(intent)
            }
        }else if(typefile.equals("doc")||typefile.equals("docx")){
            holder.filetype?.background = ContextCompat.getDrawable(context,R.drawable.file_type_box_blue)
            holder.filetype?.text = "DOC"
            holder.link?.text = "Download"
            holder.link?.setOnClickListener {
                downloadBerkas(rawLink, filename)
            }
        }else{
//            holder.filetype?.background = ContextCompat.getDrawable(context,R.drawable.file_type_box_grey)
//            holder.filetype?.text = "IMG"
//            holder.link?.setOnClickListener {
//                val intent = Intent(context, FileViewerActivity::class.java)
//                    .apply {
//                        putExtra("link",file)
//                        putExtra("status","img")
//                    }
//                context.startActivity(intent)
//            }
            holder.filetype?.background = ContextCompat.getDrawable(context,R.drawable.file_type_box_green)
            holder.filetype?.text = "XLS"
            holder.link?.text = "Download"
            holder.link?.setOnClickListener {
                downloadBerkas(rawLink, filename)
            }
        }

        holder.nama?.setOnClickListener {
            downloadBerkas(rawLink,filename)
        }

//        holder.link?.setOnLongClickListener {
//            downloadBerkas(rawLink,filename)
//            return@setOnLongClickListener true
//        }

    }

    fun downloadBerkas(rawLink:String,filename:String){
        MaterialAlertDialogBuilder(context)
            .setTitle("Unduh Berkas")
            .setMessage(filename)
            .setNegativeButton("Tidak") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Ya") { dialog, which ->
                val request =
                    DownloadManager.Request(Uri.parse(rawLink + filename))
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                request.setDescription(filename)
                request.setAllowedOverRoaming(true)
                request.setTitle(filename)
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
                request.setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    filename
                )
                val manager =
                    context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
                manager!!.enqueue(request)
                Toast.makeText(
                    context,
                    "Download sedang berlangsung $rawLink$filename",
                    Toast.LENGTH_SHORT
                ).show()
            }
            .show()
    }

    override fun getItemCount(): Int {
        return mFilteredList?.size!!
    }

    class DataPengajuanViewHolder(itemView: View?) :
        RecyclerView.ViewHolder(itemView!!) {
        val link : TextView? = itemView?.tv_link
        val filetype : TextView? = itemView?.filetype
        val nama : TextView? = itemView?.tv_nama_link
    }
}