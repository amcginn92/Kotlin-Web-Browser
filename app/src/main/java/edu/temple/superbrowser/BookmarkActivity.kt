package edu.temple.superbrowser

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class BookmarkActivity : AppCompatActivity() {

    lateinit var bmList: BookMarkList
    lateinit var url:String

    private val browserViewModel : BrowserViewModel by lazy {
        ViewModelProvider(this)[BrowserViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bookmark)


        val rec = findViewById<RecyclerView>(R.id.bookmarkRecyclerView)

         intent.getSerializableExtra("BMLIST",BookMarkList::class.java)?.run {
            bmList = this
        }

        //takes bookmarklist and two lambdas. One when a bookmark is clicked, and one when the delete button is clicked
        rec.adapter = BookmarkRecyclerViewAdapter(bmList!!,
            //===========================================================bookmark clicked Lambda: it = position (temp)
            {
                url = bmList.list.get(it).url
                clickedLink()
//                Toast.makeText(this,"Bm Position: $it," +
//                    " Current Index: ${browserViewModel.currentPageIndex} " +
//                    "Bookmark Title: ${browserViewModel.getBookmark().value?.get(it)}", Toast.LENGTH_SHORT).show()
            },
            //==============================================================================delete Lambda
            {

                val alertDialogBuilder = AlertDialog.Builder(this)
                var confirm = -1
                alertDialogBuilder.setMessage("Are you sure you want to delete this bookmark??")
                    .setTitle("Delete Bookmark")
                    .setPositiveButton("yes"){dialog, which ->
                        bmList.list.removeAt(it)
                        rec.adapter?.notifyDataSetChanged()
                        update()
                    }.setNegativeButton("no"){dialog, which ->
                        confirm = 0
                    }

                alertDialogBuilder.show()
            }
        )

        rec.layoutManager = LinearLayoutManager(this)

    }
    fun clickedLink(){
        val intent = Intent()
        intent.putExtra("RETURN",bmList)
        intent.putExtra("CLICKED", url)
        setResult(RESULT_OK, intent)
        finish()
    }
    fun update(){
        val intent = Intent()
        intent.putExtra("RETURN",bmList)
        setResult(RESULT_OK, intent)
    }
    override fun onStop(){   //if user just clicks away
        super.onStop()
        val intent = Intent()
        intent.putExtra("RETURN",bmList)
        setResult(RESULT_OK, intent)
        finish()
    }


}