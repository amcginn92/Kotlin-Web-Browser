package edu.temple.superbrowser

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