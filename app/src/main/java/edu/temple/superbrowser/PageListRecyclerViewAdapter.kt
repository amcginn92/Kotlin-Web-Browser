package edu.temple.superbrowser

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PageListRecyclerViewAdapter(private val pages: HashMap<Int, String>, private val callback: (Int)->Unit) : RecyclerView.Adapter<PageListRecyclerViewAdapter.PageListViewHolder>() {

    inner class PageListViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView) {
        init {
            textView.setOnClickListener { callback(adapterPosition) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PageListViewHolder(
        LayoutInflater.from(parent.context).inflate(android.R.layout.simple_selectable_list_item, parent, false) as TextView
    )

    override fun onBindViewHolder(holder: PageListViewHolder, position: Int) {
        holder.textView.text = pages[position]
    }

    override fun getItemCount() = pages.size
}