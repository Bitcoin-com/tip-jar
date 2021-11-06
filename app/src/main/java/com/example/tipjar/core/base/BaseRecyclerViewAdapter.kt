package com.example.tipjar.core.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T>(
    open val items: MutableList<T> = mutableListOf()
) : RecyclerView.Adapter<BaseRecyclerViewAdapter<T>.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBindViewHolder(holder.itemView, items[position], position)
    }

    abstract fun onBindViewHolder(view: View, item: T, position: Int)

    override fun getItemCount() = items.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}