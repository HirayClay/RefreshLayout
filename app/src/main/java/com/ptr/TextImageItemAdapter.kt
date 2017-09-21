package com.ptr

import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by CJJ on 2017/9/6.
 *@author CJJ
 */
class TextImageItemAdapter : RecyclerView.Adapter<TextImageItemAdapter.VH> {

    lateinit var inflater: LayoutInflater

    var IMG: ArrayList<Int>
    var NAM: ArrayList<String>
    var itemLayout: Int

    constructor(imgs: ArrayList<Int>, names: ArrayList<String>, layout: Int, OnItemClickListener: OnItemClickListener?) : super() {
        this.IMG = imgs
        this.NAM = names
        this.itemLayout = layout
        Companion.OnItemClickListener = OnItemClickListener
    }

    fun addHeader(img: Int, name: String) {
        IMG.add(0, img)
        NAM.add(0, name)
        notifyItemInserted(0)
    }

    fun addFooter(img: Int, name: String) {
        IMG.add(img)
        NAM.add(name)
        notifyItemInserted(NAM.size-1)
    }

    override fun getItemCount(): Int {
        return IMG.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        inflater = LayoutInflater.from(parent.context)
        val rootView = inflater.inflate(itemLayout, parent, false)
        return VH(rootView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.textview.text = NAM[position % NAM.size]
        holder.logo.setImageResource(IMG[position % NAM.size])
        holder.itemView.setOnClickListener {
            Companion.OnItemClickListener!!.onItemClick(position)
        }
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textview: AppCompatTextView = itemView.findViewById(R.id.name) as AppCompatTextView
        var logo: AppCompatImageView = itemView.findViewById(R.id.logo) as AppCompatImageView

    }


    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    companion object {
        var OnItemClickListener: OnItemClickListener? = null
    }
}


