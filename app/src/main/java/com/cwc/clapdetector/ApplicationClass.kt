package com.cwc.clapdetector

import android.app.Application
import com.cwc.clapdetector.Ads.OpenappAdmob
import com.google.android.gms.ads.MobileAds

class ApplicationClass: Application() {

    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        OpenappAdmob(this,getString(R.string.app_open))
    }
}