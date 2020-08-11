package com.saku.approval_sju

import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


class Library {

    fun toRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun toRupiah(nilai: Double): String? {
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)

        return formatRupiah.format(nilai)
    }

    fun rotate180(): RotateAnimation {
        val rotate = RotateAnimation(
            0F,
            180F,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 5000
        rotate.interpolator = LinearInterpolator()
        return rotate
    }

    fun toRupiahClean(nilai: Double): String? {
        val localeID = Locale("in", "ID")
        val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
        return formatRupiah.format(nilai).substringBeforeLast(".")
    }

    fun dateTodayddmmyy(): String {
        return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    }

    fun dateTodaymmddyy(): String {
        return SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(Date())
    }

    fun dateTodayyymmdd(): String {
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    }
}