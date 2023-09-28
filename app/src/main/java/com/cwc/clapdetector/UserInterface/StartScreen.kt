package com.cwc.clapdetector.UserInterface

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cwc.clapdetector.Ads.NativeAdMob
import com.cwc.clapdetector.R
import com.cwc.clapdetector.Utils.getcountrycode
import com.cwc.clapdetector.Utils.getdarkmode
import com.cwc.clapdetector.Utils.setAppNightMode
import com.cwc.clapdetector.Utils.switchLocale
import com.cwc.clapdetector.databinding.ActivityStartScreenBinding
import com.example.spanishspeakandtranslate.data.local.shared_pref.TinyDB
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class StartScreen : AppCompatActivity(),View.OnClickListener {
    private lateinit var binding:ActivityStartScreenBinding

    var isShowing = false
    private var WaitDialog: Dialog? = null
    var SplashInterstial:InterstitialAd?=null
    var nativeAd :NativeAdMob?=null

    companion object{
        var ads:Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAppNightMode(getdarkmode())
        binding.apply {
                this@StartScreen.nativeAd = NativeAdMob()
                this@StartScreen.nativeAd!!.LoadNativeAd(this@StartScreen,R.string.native_id,R.layout.native_small,this.shimerlayoutparentview.smalladView,this.adviewnative,this.nativeAd)
            TinyDB(this@StartScreen).putOutputCode("outputCodeKey", getcountrycode())
            switchLocale(this@StartScreen, getcountrycode()!!)
            binding.start.setOnClickListener(this@StartScreen)
            ads = false
            binding.viewpager.adapter = ImagePagerAdapter()
            binding.viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    // Not needed for this implementation
                }

                override fun onPageSelected(position: Int) {
                    // Update the transparency of the step views based on the current position
                    when (position) {
                        0 -> {
                            binding.circle1.setImageResource(R.drawable.circleblue)
                            binding.circle2.setImageResource(R.drawable.circle)
                            binding.circle3.setImageResource(R.drawable.circle)
                            binding.start.visibility = View.INVISIBLE
                        }

                        1 -> {
                            binding.circle1.setImageResource(R.drawable.circle)
                            binding.circle2.setImageResource(R.drawable.circleblue)
                            binding.circle3.setImageResource(R.drawable.circle)
                            binding.start.visibility = View.INVISIBLE
                        }

                        2 -> {
                            binding.circle1.setImageResource(R.drawable.circle)
                            binding.circle2.setImageResource(R.drawable.circle)
                            binding.circle3.setImageResource(R.drawable.circleblue)
                            binding.start.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {

                }
            })

        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.start -> {
                    showWaitDialog(this@StartScreen)
                    LoadInterstialAdMob()
            }
        }
    }


    private inner class ImagePagerAdapter() : PagerAdapter() {
        private val images = intArrayOf(R.drawable.step1, R.drawable.step2, R.drawable.step3)
        private val step = arrayListOf(R.string.clap_to_find_your_phone,R.string.flashlight_to_on_in_dark,R.string.what_are_you_waiting_for)
        private val text = arrayListOf(R.string.to_find_the_lost_phone_easily,R.string.to_find_the_lost_phone_easily,R.string.Go_app_to_experience_it)

        override fun getCount(): Int {
            return images.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        @SuppressLint("MissingInflatedId")
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val inflater = LayoutInflater.from(this@StartScreen)
            val itemView = inflater.inflate(R.layout.item_pager, container, false)

            val imageView = itemView.findViewById<ImageView>(R.id.image)
            val steps = itemView.findViewById<TextView>(R.id.steps)
            val title = itemView.findViewById<TextView>(R.id.title)

            imageView.setImageResource(images[position])
            title.setText(step[position])
            steps.setText(text[position])

            // Check if the view already has a parent before adding it
            if (itemView.parent != null) {
                (itemView.parent as ViewGroup).removeView(itemView)
            }

            container.addView(itemView)
            return itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    @SuppressLint("SetTextI18n")
    fun showWaitDialog(activity: Activity) {
        WaitDialog = Dialog(activity).apply {
            setContentView(R.layout.dialog_interstial)
            setCancelable(false)
            val textView = findViewById<TextView>(R.id.loading_text)
            textView.text = "Loading Ad..."
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val lp = WindowManager.LayoutParams().apply {
                copyFrom(window?.attributes)
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.MATCH_PARENT
            }
            window?.attributes = lp
        }

        if (!activity.isFinishing && !activity.isDestroyed) {
            try {
                WaitDialog?.show()
                isShowing = true
            } catch (e: Exception) {
                Log.e("TAG", "showWaitDialog" + e.message)
            }
        }
    }
    fun dismissWaitDialog() {
        if (isShowing) {
            try {
                isShowing = false
                WaitDialog?.dismiss()
            } catch (e: Exception) {
                Log.i("TAG", "hideLoadingDialog: unable to dismiss WaitDialog. -> ", e)
            }
        }
    }
    private fun LoadInterstialAdMob() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, getString(R.string.inters_id), adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                super.onAdLoaded(interstitialAd)
                Log.e("TAG", "Admob interstitial Ad is loaded")
                SplashInterstial = interstitialAd

                dismissWaitDialog()
                ShowInterstialAdMob()
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                Log.e("TAG", "Admob Interstitial Ad Load Failed: " + loadAdError.message)
                moveActivity()
                dismissWaitDialog()
            }
        })
    }
    fun ShowInterstialAdMob() {
        SplashInterstial?.show(this)
        SplashInterstial?.fullScreenContentCallback = (object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                super.onAdFailedToShowFullScreenContent(adError)
                Log.e("TAG", "Admob Interstitial Ad failed to show." + adError.message)
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                Log.e("TAG", "Admob Ad showed fullscreen content.")
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                Log.e("TAG", "Ad was dismissed.")
                moveActivity()
            }
        })
    }
    fun moveActivity(){
        startActivity(Intent(this, Permissions::class.java))
        finish()
    }
}
