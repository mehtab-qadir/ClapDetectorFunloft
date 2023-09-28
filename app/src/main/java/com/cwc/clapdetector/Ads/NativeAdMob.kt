package com.cwc.clapdetector.Ads

import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.cwc.clapdetector.R
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoOptions
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.google.android.gms.ads.nativead.NativeAdView

class NativeAdMob:AppCompatActivity() {

    private var NativeAd: NativeAd?=null

    fun LoadNativeAd(context: Activity, id:Int,layout:Int, shimerview: ShimmerFrameLayout, parentview: CardView, adlayout: FrameLayout){
        val builder = AdLoader.Builder(context,context.getString(id))
        builder.forNativeAd {
            val adView = context.layoutInflater.inflate(layout,null,false) as NativeAdView
            PopulateNativeAd(it,adView)
            shimerview.visibility = View.GONE
            parentview.visibility = View.VISIBLE
            adlayout.removeAllViews()
            adlayout.addView(adView)
        }
        val videooptions = VideoOptions.Builder().setStartMuted(true).build()
        val adoptions = NativeAdOptions.Builder().setVideoOptions(videooptions).build()
        builder.withNativeAdOptions(adoptions)
        val adloader = builder.withAdListener(object : AdListener(){
            override fun onAdFailedToLoad(p0: LoadAdError) {
                super.onAdFailedToLoad(p0)

            }
        }).build()
        adloader.loadAd(AdRequest.Builder().build())
    }

    fun PopulateNativeAd(nativeAd: NativeAd,
                         adView: NativeAdView){
        NativeAd?.destroy()
        NativeAd = nativeAd
        // Set the media view.
        adView.mediaView = adView.findViewById(R.id.ad_media)
        // Set other ad assets.
        adView.headlineView = adView.findViewById(R.id.ad_headline)
        adView.bodyView = adView.findViewById(R.id.ad_body)
        adView.callToActionView = adView.findViewById(R.id.ad_call_to_action)
        adView.iconView = adView.findViewById(R.id.ad_app_icon)

        (adView.headlineView as TextView).text = nativeAd.headline
        adView.mediaView!!.setMediaContent(nativeAd.mediaContent)
        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.body == null) {
            adView.bodyView!!.visibility = View.GONE
        } else {
            adView.bodyView!!.visibility = View.VISIBLE
            (adView.bodyView as TextView).text = nativeAd.body
        }

        if (nativeAd.callToAction == null) {
            adView.callToActionView!!.visibility = View.INVISIBLE
        } else {
            adView.callToActionView!!.visibility = View.VISIBLE
            (adView.callToActionView as TextView).text = nativeAd.callToAction
        }
        if (nativeAd.icon == null) {
            adView.iconView!!.visibility = View.VISIBLE
        } else {
            (adView.iconView as ImageView).setImageDrawable(
                nativeAd.icon!!.drawable
            )
            adView.iconView!!.visibility = View.VISIBLE
        }

        adView.setNativeAd(nativeAd)
    }
}