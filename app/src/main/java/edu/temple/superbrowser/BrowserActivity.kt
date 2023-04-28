package edu.temple.superbrowser

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import java.io.*

open class BrowserActivity : AppCompatActivity(), BrowserControlFragment.BrowserControlInterface, PageViewerFragment.PageViewerInterface, PageListFragment.PageListInterface {

    lateinit var bmList: BookMarkList

    lateinit var bmIntent: Intent

    private val pager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    private val browserViewModel : BrowserViewModel by lazy {
        ViewModelProvider(this)[BrowserViewModel::class.java]
    }

    private lateinit var file: File
    private val internalFilename = "my_file"
    private lateinit var requestQueue: RequestQueue


    //=================================================Activity Result
    var getContent = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
    ){result : ActivityResult ->
        result.data?.run{
//                Toast.makeText(,"This",Toast.LENGTH_SHORT).show()
            bmList = this.getSerializableExtra("RETURN",BookMarkList::class.java) as BookMarkList
            val url = this.getStringExtra("CLICKED")
            if(url != null){
                updateTitle(browserViewModel.currentPageIndex,url.toString())
                supportFragmentManager.findFragmentByTag("f${browserViewModel.currentPageIndex}")?.run {
                    (this as PageViewerFragment).setPageUrl(url)
                }

            }
        }
//        Log.d("Test1", "${}")
    }

    //===========================================================onCreate
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestQueue = Volley.newRequestQueue(this)

//      create file in internal storage if not already made
        file = File(filesDir, internalFilename)

        bmList = BookMarkList()

        if(file != null) {
            val fis = FileInputStream(file)
            val ois = ObjectInputStream(fis)
            bmList = ois.readObject() as BookMarkList
            ois.close()
            Toast.makeText(this,"Size ${bmList.list.size}",Toast.LENGTH_SHORT).show()
            Log.d("Read", "${bmList.list.size}")

        }

        //Create bookmarklist instance

        //assign the pager adapter
        pager.adapter = BrowserFragmentStateAdapter(this)

        // Move to previous page index
        pager.setCurrentItem(browserViewModel.currentPageIndex, false)

        // Keep track of current page in ViewModel
        pager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                browserViewModel.currentPageIndex = position

                // Retrieve title from Fragment directly.
                // Exploits FragmentStateAdapter 'f-position' Tag scheme
                supportFragmentManager.findFragmentByTag("f$position")?.run {
                    (this as PageViewerFragment).getPageTitle()?.run {
                        updateTitle(position, this)
                    }
                }
            }
        })

        if (browserViewModel.getCurrentPageCount() == 0) {
            addPage()
        }

    }

    /**
     * Add a new page to the list by
     * - increasing page count
     * - notifying adapter
     * - switching ViewPager to current page
     * - clearing Activity title
     */
    override fun addPage() {
        browserViewModel.addNewPage()
        pager.run {
            adapter?.notifyItemInserted(browserViewModel.getCurrentPageCount())
            setCurrentItem(browserViewModel.getCurrentPageCount(), true)
        }

        updateTitle(browserViewModel.currentPageIndex,"")
    }

    override fun share(){
        lateinit var url:String
        lateinit var title:String
//        Toast.makeText(this,"Share", Toast.LENGTH_SHORT).show()
        supportFragmentManager.findFragmentByTag("f${browserViewModel.currentPageIndex}")?.run {
            url = (this as PageViewerFragment).getPageUrl().toString()
            title = (this as PageViewerFragment).getPageTitle().toString()
        }

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }
        startActivity(sendIntent)
    }

    override fun bookmark() {
        lateinit var url:String
        lateinit var title:String
        supportFragmentManager.findFragmentByTag("f${browserViewModel.currentPageIndex}")?.run {
            url = (this as PageViewerFragment).getPageUrl().toString()
            title = (this as PageViewerFragment).getPageTitle().toString()
        }
        val temp = Bookmark(url,title)
        if(bmList.list.contains(temp)){
            Toast.makeText(this,"$url: Title: ${title} already exists!!", Toast.LENGTH_SHORT).show()
        }else if(url == null || url == "" || title == null || title == ""){
            Toast.makeText(this,"$url: Title: ${title} is blank!!", Toast.LENGTH_SHORT).show()
        }else{
//            Toast.makeText(this,"'$url': Title: ${title} should not be blank!!", Toast.LENGTH_SHORT).show()
            bmList.add(url,title)
            Toast.makeText(this,"Size ${bmList.list.size}",Toast.LENGTH_SHORT).show()

        }

    }

    override fun bookmarks() {

        //define the intent to lauch bookmark Activity
        bmIntent = Intent(this,BookmarkActivity::class.java)
        //add the bookmarklist to it
        bmIntent.putExtra("BMLIST", bmList)

        getContent.launch(bmIntent)
    }

    override fun closePage() {
    }


    // Update or clear Activity title
    override fun updateTitle(pageId: Int, title: String) {
        if (pageId == browserViewModel.currentPageIndex) {
            if (title.isNotEmpty())
                supportActionBar?.title = "${getString(R.string.app_name)} - $title"
            else
                supportActionBar?.title = getString(R.string.app_name)
        }
    }

    inner class BrowserFragmentStateAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
        // retrieve page count from ViewModel
        override fun getItemCount() : Int {
            return browserViewModel.getCurrentPageCount()
        }

        override fun createFragment(position: Int) = PageViewerFragment.newInstance(position)

    }

    override fun pageSelected(pageIndex: Int) {
        pager.setCurrentItem(pageIndex, true)
    }

    override fun onStop() {
        super.onStop()
        val fos = FileOutputStream(file)
        val oos = ObjectOutputStream(fos)
        oos.writeObject(bmList)

        Log.d("Written", "${bmList.list.size}")

        oos.close()
    }

}