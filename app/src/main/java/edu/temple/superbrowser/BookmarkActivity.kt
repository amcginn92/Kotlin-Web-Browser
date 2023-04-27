package edu.temple.superbrowser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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
//                Toast.makeText(this,"position $it", Toast.LENGTH_SHORT).show()
                bmList.list.removeAt(it)
                rec.adapter?.notifyDataSetChanged()
                update()
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