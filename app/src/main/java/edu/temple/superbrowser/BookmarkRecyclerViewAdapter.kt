package edu.temple.superbrowser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
class BookmarkRecyclerViewAdapter(private val bookmarks: BookMarkList, private val goToCallback: (Int) ->Unit
    ,private val deleteCallback: (Int) ->Unit): RecyclerView.Adapter<BookmarkRecyclerViewAdapter.BookMarkViewHolder>() {

    inner class BookMarkViewHolder(view: View): RecyclerView.ViewHolder(view){
        val mTextView = view.findViewById<TextView>(R.id.bookMarkTextView)
        private val deleteButton = view.findViewById<ImageButton>(R.id.deleteBookmark)
        init{
            //when text is clicked we go to the listed bookmark
            mTextView.setOnClickListener{
                goToCallback(adapterPosition)
            }
            deleteButton.setOnClickListener{
                deleteCallback(adapterPosition)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BookMarkViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.bookmark_view, parent,false) as View
    )

    override fun getItemCount(): Int {
        return bookmarks.list.size
    }

    override fun onBindViewHolder(holder: BookMarkViewHolder, position: Int) {
        holder.mTextView.text = bookmarks.list.get(position).title
    }

}