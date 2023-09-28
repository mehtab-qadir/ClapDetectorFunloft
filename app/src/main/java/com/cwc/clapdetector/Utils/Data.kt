package com.cwc.clapdetector.Utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatDelegate
import com.cwc.clapdetector.R
import java.util.Locale

data class Languages_Model(val name:String,val flag:Int,val country_code:String)

data class Sound_model(val name:String,val image:Int,val sound:String)

fun Context.Sounddata(): ArrayList<Sound_model> {
    val list = arrayListOf(
        Sound_model(getString(R.string.cat_meowing), R.drawable.cat,"cat_mewoing.wav"),
        Sound_model(getString(R.string.dog_barking), R.drawable.dog,"dog_barking.wav"),
        Sound_model(getString(R.string.cavalry), R.drawable.trumpet,"cavalry_sound.mp3"),
        Sound_model(getString(R.string.whistle), R.drawable.whistle,"human_whistle.wav"),
        Sound_model(getString(R.string.hello), R.drawable.hello,"hello.mp3"),
        Sound_model(getString(R.string.car_honk), R.drawable.car_rental,"car_honk.mp3"),
        Sound_model(getString(R.string.door_bell), R.drawable.bell,"doorbellsound.mp3"),
        Sound_model(getString(R.string.party_horn), R.drawable.party_horn,"party_horn.mp3"),
        Sound_model(getString(R.string.police_whistle), R.drawable.police_wristle,"police_wristle.mp3")
    )

    return list
}

fun getLanguages():ArrayList<Languages_Model> {
    val list = arrayListOf(
    Languages_Model("English (Default)", R.drawable.english,"en"),
    Languages_Model("Chinese", R.drawable.china,"zh"),
    Languages_Model("Afrikaans", R.drawable.africian,"af"),
    Languages_Model("French", R.drawable.french,"fr"),
    Languages_Model("German", R.drawable.german,"de"),
    Languages_Model("Hindi", R.drawable.india,"hi"),
    Languages_Model("Indonesian",R.drawable.indonesia,"in"))

    return list
}

fun switchLocale(activity: Activity?, code: String) {
    if (activity != null) {
        val myLocale = Locale(code)
        val res: Resources = activity.resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.setLocale(myLocale)
        res.updateConfiguration(conf, dm)

    }
}

fun Context.savesoundposition(value:Int){
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt("intValueKey", value)
    editor.apply()
}

fun Context.getsoundposition():Int{
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val intValue = sharedPreferences.getInt("intValueKey", 0)
    return intValue
}

fun Context.savesoundtimeposition(value:Int){
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt("soundtime", value)
    editor.apply()
}

fun Context.getsoundtimeposition():Int{
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val intValue = sharedPreferences.getInt("soundtime", 15)
    return intValue
}

fun Context.savesoundactive(value:Boolean){
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("soundactive", value)
    editor.apply()
}

fun Context.getsoundactive():Boolean{
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val intValue = sharedPreferences.getBoolean("soundactive", true)
    return intValue
}

fun Context.savesoundsensitivity(value:Int){
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt("sound", value)
    editor.apply()
}

fun Context.getsoundsenstivity():Int{
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val intValue = sharedPreferences.getInt("sound", 200)
    return intValue
}

fun Context.savesound(value:String){
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("sound1", value)
    editor.apply()
}

fun Context.getsound():String?{
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val intValue = sharedPreferences.getString("sound1", "cat_mewoing.wav")
    return intValue
}

fun Context.savelanguage(name: String, pos: Int) {
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("name", name)
    editor.putInt("pos", pos)
    editor.apply()
}

fun Context.getlanguage(): Pair<String?, Int> {
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val nameValue = sharedPreferences.getString("name", null)
    val posValue = sharedPreferences.getInt("pos", 0)
    return Pair(nameValue, posValue)
}

fun Context.saveflash(value:Boolean){
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("flashlight", value)
    editor.apply()
}

fun Context.getflash():Boolean{
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val intValue = sharedPreferences.getBoolean("flashlight", true)
    return intValue
}

fun Context.savevibrate(value:Boolean){
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("vibreate", value)
    editor.apply()
}

fun Context.getvibrate():Boolean{
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val intValue = sharedPreferences.getBoolean("vibreate", true)
    return intValue
}

fun Context.saveSOSPattern(pattern: LongArray) {
    val sharedPreferences =  getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("sos", pattern.joinToString(","))
    editor.apply()
}

fun Context.loadSOSPattern(): LongArray {
    val sharedPreferences =  getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val savedPattern = sharedPreferences.getString("sos", "") ?: ""
    return if (savedPattern.isNotEmpty()) {
        savedPattern.split(",").map { it.toLong() }.toLongArray()
    } else {
        longArrayOf(1000) // Return an empty array if no pattern is saved
    }
}

fun Context.savevibratePattern(pattern: LongArray) {
    val sharedPreferences =  getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("vsos", pattern.joinToString(","))
    editor.apply()
}

fun Context.loadvibratePattern(): LongArray {
    val sharedPreferences =  getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val savedPattern = sharedPreferences.getString("vsos", "") ?: ""
    return if (savedPattern.isNotEmpty()) {
        savedPattern.split(",").map { it.toLong() }.toLongArray()
    } else {
        longArrayOf(200) // Return an empty array if no pattern is saved
    }
}

fun Context.savecountrycode(value:String){
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("country", value)
    editor.apply()
}

fun Context.getcountrycode():String?{
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val intValue = sharedPreferences.getString("country", "en")
    return intValue
}

fun Context.savedarkmode(value:Boolean){
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putBoolean("vibreate", value)
    editor.apply()
}

fun Context.getdarkmode():Boolean{
    val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    val intValue = sharedPreferences.getBoolean("vibreate", false)
    return intValue
}

 fun Context.isDarkModeEnabled(): Boolean {
    val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
}

 fun Context.setAppNightMode(enabled: Boolean) {
    if (enabled) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    } else {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
