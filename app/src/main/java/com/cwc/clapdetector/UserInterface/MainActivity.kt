package com.cwc.clapdetector.UserInterface


import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.cwc.clapdetector.Adopter.SoundAdopter
import com.cwc.clapdetector.Ads.NativeAdMob
import com.cwc.clapdetector.R
import com.cwc.clapdetector.UserInterface.StartScreen.Companion.ads
import com.cwc.clapdetector.Utils.ClapDetectionService
import com.cwc.clapdetector.Utils.Sound_model
import com.cwc.clapdetector.Utils.Sounddata
import com.cwc.clapdetector.Utils.getcountrycode
import com.cwc.clapdetector.Utils.getflash
import com.cwc.clapdetector.Utils.getsoundsenstivity
import com.cwc.clapdetector.Utils.getvibrate
import com.cwc.clapdetector.Utils.isDarkModeEnabled
import com.cwc.clapdetector.Utils.savecountrycode
import com.cwc.clapdetector.Utils.savedarkmode
import com.cwc.clapdetector.Utils.saveflash
import com.cwc.clapdetector.Utils.savesoundsensitivity
import com.cwc.clapdetector.Utils.savevibrate
import com.cwc.clapdetector.Utils.setAppNightMode
import com.cwc.clapdetector.Utils.switchLocale
import com.cwc.clapdetector.databinding.ActivityMainBinding
import com.cwc.clapdetector.databinding.ExitDialogBinding
import com.example.spanishspeakandtranslate.data.local.shared_pref.TinyDB
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.warkiz.widget.IndicatorSeekBar
import com.warkiz.widget.OnSeekChangeListener
import com.warkiz.widget.SeekParams

class MainActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var binding:ActivityMainBinding
    private lateinit var soundAdapter: SoundAdopter
    private lateinit var soundList: ArrayList<Sound_model>
    var nativeAd:NativeAdMob?=null
    private lateinit var vibrator: Vibrator

    var mSlideState = false
    var count=0
    var isShowing = false
    private var WaitDialog: Dialog? = null
    var SplashInterstial:InterstitialAd?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackColorOn = ContextCompat.getColor(this, R.color.appcolor)
        val trackColorOff = ContextCompat.getColor(this, R.color.grey2)
        binding.apply {
            ads = true
            binding.ivmenu.setOnClickListener(this@MainActivity)
            binding.start.setOnClickListener(this@MainActivity)
            binding.stop.setOnClickListener(this@MainActivity)
            binding.settings.setOnClickListener(this@MainActivity)
            drawer()
            init()

            binding.menu.darkmodee.isChecked = isDarkModeEnabled()
            if(isDarkModeEnabled()){
                binding.menu.darkmodee.trackTintList = ColorStateList.valueOf(getColor(R.color.appcolor))
            }else{
                binding.menu.darkmodee.trackTintList = ColorStateList.valueOf(getColor(R.color.grey1))
            }
            binding.menu.flashlight.isChecked = getflash()
            binding.menu.vibratee.isChecked = getvibrate()
            val trackColor = if (getvibrate()) trackColorOn else trackColorOff
            binding.menu.vibratee.trackTintList = ColorStateList.valueOf(trackColor)
            val trackColor2 = if (getflash()) trackColorOn else trackColorOff
            binding.menu.flashlight.trackTintList = ColorStateList.valueOf(trackColor2)

            if (getflash()) {
                binding.menu.flashlight.trackTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.appcolor))
            } else {
                binding.menu.flashlight.trackTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.grey1))
            }
            if (getvibrate()) {
                binding.menu.vibratee.trackTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.appcolor))
            } else {
                binding.menu.vibratee.trackTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.grey1))
            }
            val isServiceRunning = isServiceRunning(this@MainActivity, ClapDetectionService::class.java)
            if (isServiceRunning) {
                binding.stop.visibility = View.VISIBLE
                binding.start.visibility = View.INVISIBLE
                binding.activetext.text = getString(R.string.tap_deactive)
            } else {
                binding.stop.visibility = View.INVISIBLE
                binding.start.visibility = View.VISIBLE
                binding.activetext.text = getString(R.string.tap_active)
            }
            soundList = Sounddata()
            binding.soundsensitivity.setProgress(getsoundsenstivity().toFloat())
            soundAdapter = SoundAdopter(this@MainActivity, soundList)
            binding.recyclerview.adapter = soundAdapter
            binding.recyclerview.layoutManager = GridLayoutManager(this@MainActivity, 3)
            binding.recyclerview.setItemViewCacheSize(10)

            binding.menu.flashlight.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    saveflash(isChecked)
                    binding.menu.flashlight.trackTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.appcolor))
                } else {
                    saveflash(isChecked)
                    binding.menu.flashlight.trackTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.grey1))
                }
                val trackColor = if (isChecked) trackColorOn else trackColorOff
                binding.menu.flashlight.trackTintList = ColorStateList.valueOf(trackColor)
            }
            binding.menu.vibratee.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    savevibrate(isChecked)
                    binding.menu.vibratee.trackTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.appcolor))
                } else {
                    savevibrate(isChecked)
                    binding.menu.vibratee.trackTintList =
                        ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.grey1))
                }
                val trackColor = if (isChecked) trackColorOn else trackColorOff
                binding.menu.vibratee.trackTintList = ColorStateList.valueOf(trackColor)
            }

            binding.menu.darkmodee.setOnCheckedChangeListener { _, isChecked ->
                setAppNightMode(isChecked)
                savedarkmode(isChecked)
                startActivity(Intent(this@MainActivity,StartScreen::class.java))
                finishAffinity()
            }

            soundAdapter.setOnItemClickListener(object : SoundAdopter.OnItemClickListener {
                override fun onItemClick(position: Int) {
                    val intent = Intent(this@MainActivity, SetAudio::class.java)
                    intent.putExtra("name", soundList[position].name)
                    intent.putExtra("image", soundList[position].image)
                    intent.putExtra("pos", position)
                    intent.putExtra("sound", soundList[position].sound)
                    startActivity(intent)
                }

            })

            vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
            binding.soundsensitivity.setOnSeekChangeListener(object : OnSeekChangeListener {
                override fun onSeeking(seekParams: SeekParams?) {
                    if (seekParams != null) {
                        savesoundsensitivity(seekParams.progressFloat.toInt())
                    }
                }

                override fun onStartTrackingTouch(seekBar: IndicatorSeekBar?) {

                }

                override fun onStopTrackingTouch(seekBar: IndicatorSeekBar?) {

                }

            })
            onBackPressedDispatcher.addCallback(this@MainActivity, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitDialog()
                }
            })
        }
    }
    fun init(){
        binding.menu.language.setOnClickListener(this@MainActivity)
        binding.menu.exit.setOnClickListener(this@MainActivity)
        binding.menu.feedback.setOnClickListener(this@MainActivity)
        binding.menu.rateus.setOnClickListener(this@MainActivity)
        binding.menu.privacy.setOnClickListener(this@MainActivity)
        binding.menu.moreapps.setOnClickListener(this@MainActivity)
    }
    private fun drawer() {
        binding.drawerlayout.setDrawerListener(object : ActionBarDrawerToggle(
            this@MainActivity,
            binding.drawerlayout,
            0,
            0) {
            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                mSlideState = false //is Closed
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                mSlideState = true //is Opened
            }
        })
    }

    @SuppressLint("RtlHardcoded")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.ivmenu -> {
                binding.drawerlayout.openDrawer(Gravity.LEFT)
            }

            R.id.language -> {
                startActivity(Intent(this@MainActivity,Language::class.java))
            }

            R.id.start -> {
                val startIntent = Intent(this@MainActivity, ClapDetectionService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Use startForegroundService on Android Oreo and later
                    startForegroundService(startIntent);
                } else {
                    // Use startService on Android versions prior to Oreo
                    startService(startIntent);
                }
                binding.stop.visibility = View.VISIBLE
                binding.start.visibility = View.INVISIBLE
                binding.activetext.text = getString(R.string.tap_deactive)
            }
            R.id.stop -> {
                val stopIntent = Intent(this@MainActivity, ClapDetectionService::class.java)
                stopService(stopIntent)
                binding.stop.visibility = View.INVISIBLE
                binding.start.visibility = View.VISIBLE
                binding.activetext.text = getString(R.string.tap_active)
                count++
                if(count==3){
                    showWaitDialog(this@MainActivity)
                    LoadInterstialAdMob()
                    count=0
                }
            }
            R.id.settings -> {
                startActivity(Intent(this@MainActivity,Settings::class.java))
            }
            R.id.feedback -> {
                sendEmail()
            }
            R.id.rateus -> {
                openGooglePlayForRating()
            }
            R.id.privacy -> {
                val uri = Uri.parse("https://sites.google.com/view/clap-phone-finder-privacy/home")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            R.id.moreapps -> {
                val uri = Uri.parse("https://play.google.com/store/search?q=pub:Codix Apps")
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
            R.id.exit -> {
                exitDialog()
            }
        }
    }
    fun openGooglePlayForRating() {
        val uri = Uri.parse("market://details?id=$packageName")

        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // Handle case where Google Play Store is not available
        }
    }

    fun sendEmail() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("islam24hoursstudio@gmail.com"))
        intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Clap Detector")

        if (intent.resolveActivity(this.packageManager) != null) {
            startActivity(intent)
        } else {
            // Handle the case where no email app is available
        }
    }

    fun isServiceRunning(context: Context, serviceClass: Class<*>): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val services = manager.getRunningServices(Integer.MAX_VALUE)
        for (service in services) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

    fun exitDialog(){
        val dialog = BottomSheetDialog(this@MainActivity)
        val binding = ExitDialogBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog.window!!.attributes)
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams.gravity = Gravity.CENTER
        dialog.window!!.attributes = layoutParams
        dialog.show()

        nativeAd = NativeAdMob()
        nativeAd!!.LoadNativeAd(this,R.string.native_id,R.layout.native_medium,binding.shimerlayoutparentview.shimerlayout,binding.adviewnative,binding.nativeAd)
        binding.exit.setOnClickListener {
            finishAffinity()
            dialog.dismiss()
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