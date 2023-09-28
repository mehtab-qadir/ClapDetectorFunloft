package com.cwc.clapdetector.Ads

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.cwc.clapdetector.ApplicationClass
import com.cwc.clapdetector.UserInterface.StartScreen
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd

class OpenappAdmob(val Application: ApplicationClass,val Ad:String): Application.ActivityLifecycleCallbacks,
    LifecycleObserver {

    private var appOpenAd: AppOpenAd? = null
    private var loadCallback: AppOpenAd.AppOpenAdLoadCallback? = null
    private var currentActivity: Activity? = null
    private var isShowingAd = false
    private val adRequest: AdRequest

        get() = AdRequest.Builder().build()
    val isAdAvailable: Boolean
        get() = appOpenAd != null

    init {
        Application.registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
    fun fetchAd(){
        if(isAdAvailable){
            return
        }
        loadCallback = object : AppOpenAd.AppOpenAdLoadCallback(){

            override fun onAdLoaded(p0: AppOpenAd) {
                super.onAdLoaded(p0)
                appOpenAd=p0
            }

            override fun onAdFailedToLoad(p0: LoadAdError) {
                Log.d("tag", "onAppOpenAdFailedToLoad: ")
            }
        }
        val request = adRequest
        AppOpenAd.load(Application,
            Ad, request,
            AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback as AppOpenAd.AppOpenAdLoadCallback
        )
    }
    fun showAdIfAvailable(){
        if(!isShowingAd && isAdAvailable && StartScreen.ads) {
            Log.d("tag", "will show ad ")
            val fullScreenContentCallback: FullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        appOpenAd = null
                        isShowingAd = false
                        fetchAd()
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {

                    }

                    override fun onAdShowedFullScreenContent() {
                        isShowingAd = true
                    }
                }
            appOpenAd!!.setFullScreenContentCallback(fullScreenContentCallback)
            appOpenAd!!.show(currentActivity!!)

        }else{
            Log.d("tag", "can't show ad ")
            fetchAd()
        }
    }
    override fun onActivityCreated(p0: Activity, p1: Bundle?) {

    }

    override fun onActivityStarted(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivityResumed(p0: Activity) {
        currentActivity = p0
    }

    override fun onActivityPaused(p0: Activity) {

    }

    override fun onActivityStopped(p0: Activity) {

    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {

    }

    override fun onActivityDestroyed(p0: Activity) {
        currentActivity = null
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart(){
        showAdIfAvailable()
        Log.d("tag", "onStart: ")
    }
}