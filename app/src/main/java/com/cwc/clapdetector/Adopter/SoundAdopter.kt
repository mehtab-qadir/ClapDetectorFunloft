package com.cwc.clapdetector.Adopter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cwc.clapdetector.R
import com.cwc.clapdetector.UserInterface.SetAudio
import com.cwc.clapdetector.Utils.Sound_model
import com.cwc.clapdetector.Utils.getsoundposition
import com.cwc.clapdetector.Utils.savesoundposition
import com.cwc.clapdetector.databinding.SoundItemBinding

class SoundAdopter(val context: Context,val soundlist:ArrayList<Sound_model>)
    :RecyclerView.Adapter<SoundAdopter.ViewHolder>() {

    var selectedPosition = context.getsoundposition()

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = SoundItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(itemView)
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            imageView2.setImageResource(soundlist[position].image)
            text.text = soundlist[position].name

            root.setOnClickListener {
                onItemClickListener?.onItemClick(holder.adapterPosition)
            }

            if (position == selectedPosition){
                constraintLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.lightblue))
            }else{
                constraintLayout.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.lightgrey))
            }

        }
    }

        override fun getItemCount(): Int {
         return soundlist.size
    }

    class ViewHolder(var binding: SoundItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }
}