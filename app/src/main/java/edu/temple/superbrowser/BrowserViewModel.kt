package edu.temple.superbrowser

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BrowserViewModel : ViewModel() {

    private val titleObservable: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    private var numberOfPages : Int = 0
    var currentPageIndex = 0
    val pageTitles = HashMap<Int, String>()
    val bmList = BookMarkList()




    fun addBookmark(url:String, title: String){
        val bm:Bookmark = Bookmark(url,title)
        bmList.list.add(bm)
    }

    fun getBookmark():BookMarkList{
        return bmList
    }

    fun getCurrentPageCount (): Int {
        return numberOfPages
    }

    fun addNewPage() {
        numberOfPages++
    }

    fun closePage() {
        if (numberOfPages > 0)
            numberOfPages--
    }

    fun updatePageTitle (pageId: Int, title: String) {
        pageTitles[pageId] = title
        titleObservable.value = pageId
    }

    fun getPageTitleObservable (): LiveData<Int> {
        return titleObservable
    }

}