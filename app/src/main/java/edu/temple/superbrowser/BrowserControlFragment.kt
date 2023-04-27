package edu.temple.superbrowser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso


class BrowserControlFragment : Fragment() {

    private lateinit var share : ImageButton
    private lateinit var bookmark : ImageButton
    private lateinit var bookmarks : ImageButton

    private val browserViewModel : BrowserViewModel by lazy {
        ViewModelProvider(this)[BrowserViewModel::class.java]
    }
    lateinit var url:String
    lateinit var title:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_browser_control, container, false).apply {
            bookmarks = findViewById<ImageButton>(R.id.bookmarksButton)
            share = findViewById<ImageButton>(R.id.shareButton)
            bookmark = findViewById<ImageButton>(R.id.bookmarkButton)

            Picasso.get()
                .load(R.drawable.share)
                .resize(100,100)
                .into(share)

            Picasso.get()
                .load(R.drawable.bookmark)
                .resize(100,100)
                .into(bookmark)

            Picasso.get()
                .load(R.drawable.bookmarks)
                .resize(100,100)
                .into(bookmarks)


//            parentFragmentManager.findFragmentByTag("f${browserViewModel.currentPageIndex}")?.run{
//                url = (this as PageViewerFragment).getPageUrl().toString()
//                title = (this as PageViewerFragment).getPageTitle().toString()
//                Toast.makeText(requireActivity(),"${url}, ${title}",Toast.LENGTH_SHORT).show()
//            }

            findViewById<ImageButton>(R.id.addPageButton).setOnClickListener{(requireActivity() as BrowserControlInterface).addPage()}
            findViewById<ImageButton>(R.id.closePageButton).setOnClickListener{(requireActivity() as BrowserControlInterface).closePage()}
            findViewById<ImageButton>(R.id.shareButton).setOnClickListener{(requireActivity() as BrowserControlInterface).share()}
            findViewById<ImageButton>(R.id.bookmarkButton).setOnClickListener{(requireActivity() as BrowserControlInterface).bookmark()}
            findViewById<ImageButton>(R.id.bookmarksButton).setOnClickListener{(requireActivity() as BrowserControlInterface).bookmarks()}
        }
    }

    interface BrowserControlInterface {
        fun addPage()
        fun closePage()
        fun share()
        fun bookmark()
        fun bookmarks()
    }

}