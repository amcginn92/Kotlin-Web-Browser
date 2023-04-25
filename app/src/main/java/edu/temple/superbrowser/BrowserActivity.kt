package edu.temple.superbrowser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class BrowserActivity : AppCompatActivity(), BrowserControlFragment.BrowserControlInterface, PageViewerFragment.PageViewerInterface, PageListFragment.PageListInterface {

    private val pager: ViewPager2 by lazy {
        findViewById(R.id.viewPager)
    }

    private val browserViewModel : BrowserViewModel by lazy {
        ViewModelProvider(this)[BrowserViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

}