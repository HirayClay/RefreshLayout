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
class ItemsAdapter : RecyclerView.Adapter<ItemsAdapter.VH> {

    var ITEMS: ArrayList<String>? = null
    lateinit var inflater: LayoutInflater

    var IMG = arrayListOf(R.mipmap.china, R.mipmap.iceland, R.mipmap.poland, R.mipmap.denmark,
            R.mipmap.german, R.mipmap.russia, R.mipmap.french, R.mipmap.finland,
            R.mipmap.czech, R.mipmap.norway, R.mipmap.swedish, R.mipmap.indian, R.mipmap.japan, R.mipmap.vietnam)
    var NAM = arrayListOf("China", "IceLand", "Poland", "Denmark", "German", "Russia", "France", "Finland", "Czech", "Norway", "Swedish"
            , "Indian", "Japan", "Vietnam")


    constructor(ITEMS: ArrayList<String>?, OnItemClickListener: OnItemClickListener?) : super() {
        this.ITEMS = ITEMS
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
        var rootView = inflater.inflate(R.layout.item_text, parent, false)
        return VH(rootView)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.textview.text = NAM[position % NAM.size]
        holder.logo.setImageResource(IMG[position % NAM.size])
        holder.itemView.setOnClickListener {
            Companion.OnItemClickListener!!.handle(position)
        }
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textview: AppCompatTextView = itemView.findViewById(R.id.name) as AppCompatTextView
        var logo: AppCompatImageView = itemView.findViewById(R.id.logo) as AppCompatImageView

    }


    interface OnItemClickListener {
        fun handle(position: Int)
    }

    companion object {
        var OnItemClickListener: OnItemClickListener? = null
    }
}


