package com.saku.approval_sju.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.saku.approval_sju.fragments.*


class PageAdapter internal constructor(fm: FragmentManager?, private val numOfTabs: Int) :
    FragmentPagerAdapter(fm!!) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> InfoPengajuanFragment()
            1 -> BankTransferFragment()
//            2 -> BudgetJurnalFragment()
            2 -> DokumenFragment()
            3 -> FlowDokumenFragment()
            else -> InfoPengajuanFragment.newInstance()
        }
    }
    //    override fun getItem(position: Int): Fragment {
//        return when (position) {
//            0 -> InfoPengajuanFragment()
//            1 -> BankTransferFragment()
//            2 -> BudgetJurnalFragment()
//            else -> InfoPengajuanFragment
//        }
//    }

    override fun getCount(): Int {
        return numOfTabs
    }

}