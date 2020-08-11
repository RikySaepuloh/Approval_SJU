package com.saku.approval_sju

import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import es.voghdev.pdfviewpager.library.RemotePDFViewPager
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter
import es.voghdev.pdfviewpager.library.remote.DownloadFile
import es.voghdev.pdfviewpager.library.util.FileUtil
import kotlinx.android.synthetic.main.activity_file_viewer.*


class FileViewerActivity : AppCompatActivity(), DownloadFile.Listener {
    lateinit var link :String
    lateinit var status :String
    lateinit var root: LinearLayout
    lateinit var remotePDFViewPager: RemotePDFViewPager
    lateinit var imageview: ImageView
    lateinit var adapter: PDFPagerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_viewer)
        root = findViewById(R.id.layout_root)
        if(intent.hasExtra("link")){
//            link = intent.getStringExtra("link").replace(" ","%20")
            link = intent.getStringExtra("link")
            status = intent.getStringExtra("status")
        }
        if(status=="pdf"){
//            remotePDFViewPager = RemotePDFViewPager(this, "https://devsai-s3.s3.ap-southeast-1.amazonaws.com/apv/5f0575d4d5a18_Preview.pdf", this)
            remotePDFViewPager = RemotePDFViewPager(this, link.replace(" ","%20"), this)
            remotePDFViewPager.id = R.id.pdfview
        }else{
            imageview = imageView
            imageview.id = R.id.imageView
            imageLayout()
        }

//        webview.settings.loadsImagesAutomatically = true
//        webview.settings.javaScriptEnabled = true
//        webview.settings.loadWithOverviewMode = true
//        webview.settings.useWideViewPort = true
//        webview.settings.supportZoom()
//        webview.webViewClient = WebViewClient()
////        webview.webViewClient = object : WebViewClient() {
////            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
////                view.loadUrl(url)
////                return true
////            }
////
////            override fun onPageFinished(view: WebView, url: String) {
////                webview.visibility = View.INVISIBLE
////            }
////        }
////        val linkhardcode="https://api.simkug.com/api/apv-mobile/storage/5f27f74a93e4c_Biaya Tenaga Magang Bang Karir SDM Mei 2020  Lampiran_signed KabagSDM.pdf"
//        val linkhardcode="https://devsai-s3.s3.ap-southeast-1.amazonaws.com/apv/5f0575d4d5a18_Preview.pdf"
////        webview.loadData(linkhardcode,"application/pdf","UTF-8")
//        webview.loadUrl(linkhardcode)
    }

    override fun onSuccess(url: String?, destinationPath: String?) {
        adapter = PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url))
        remotePDFViewPager.adapter = adapter
        updateLayout()
    }

    fun updateLayout(){
        root.removeAllViewsInLayout();
        root.addView(remotePDFViewPager,
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    fun imageLayout(){
        root.removeAllViewsInLayout();
        root.addView(imageview,
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        Glide.with(this).load(link).error(android.R.drawable.ic_dialog_alert).into(imageview)
    }


    override fun onFailure(e: Exception?) {
        e?.printStackTrace()
        Toast.makeText(this,"Berkas gagal ditampilkan.",Toast.LENGTH_LONG).show()
        if(::adapter.isInitialized){
            adapter.close()
        }
        super.onBackPressed()
    }

    override fun onProgressUpdate(progress: Int, total: Int) {
        if (progress==10){
            Toast.makeText(this,"Harap tunggu...",Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        if(::adapter.isInitialized){
            adapter.close()
        }
        super.onDestroy()
    }
}
