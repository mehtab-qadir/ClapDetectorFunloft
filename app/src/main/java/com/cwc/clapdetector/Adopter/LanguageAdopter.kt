package com.cwc.clapdetector.Adopter

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cwc.clapdetector.R
import com.cwc.clapdetector.Utils.Languages_Model
import com.cwc.clapdetector.Utils.getlanguage
import com.cwc.clapdetector.Utils.savecountrycode
import com.cwc.clapdetector.Utils.savelanguage
import com.cwc.clapdetector.databinding.LanguagesItemBinding

class LanguageAdapter(
    var context: Context,
    var languageitem: ArrayList<Languages_Model>,
) :
    RecyclerView.Adapter<LanguageAdapter.ViewHolder>() {

    var selectedItem: Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageAdapter.ViewHolder {
        val itemView = LanguagesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding)
        {
            val arr = languageitem[position]

            languagename.text = arr.name
            flagimage.setImageResource(arr.flag)
            selectedItem = context.getlanguage().second

            if (selectedItem == position){
                cardview.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.appcolor))
                languagename.setTextColor(ContextCompat.getColor(context, R.color.white1))
            } else{
                cardview.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.white1))
                languagename.setTextColor(ContextCompat.getColor(context, R.color.black1))
            }

            holder.itemView.setOnClickListener {
                context.savelanguage(arr.name,position)
                context.savecountrycode(arr.country_code)
                notifyItemChanged(selectedItem)
                selectedItem = position
                notifyItemChanged(selectedItem)

            }

        }

    }

    override fun getItemCount(): Int {
        return languageitem.size
    }

    inner class ViewHolder(var binding: LanguagesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}