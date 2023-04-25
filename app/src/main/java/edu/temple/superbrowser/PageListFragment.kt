package edu.temple.superbrowser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PageListFragment : Fragment() {

    private val browserViewModel : BrowserViewModel by lazy {
        ViewModelProvider(requireActivity())[BrowserViewModel::class.java]
    }

    private val pageListRecyclerView : RecyclerView by lazy {
        view as RecyclerView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_page_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pageListRecyclerView?.layoutManager = LinearLayoutManager(requireActivity())
        pageListRecyclerView?.adapter = PageListRecyclerViewAdapter(browserViewModel.pageTitles){
            (requireActivity() as PageListInterface).pageSelected(it)
        }


        // Using observable proxy
        browserViewModel.getPageTitleObservable().observe(requireActivity()) {
            pageListRecyclerView?.adapter?.run{
                (this as PageListRecyclerViewAdapter).notifyItemChanged(it)
            }
        }
    }

    interface PageListInterface {
        fun pageSelected (pageIndex: Int)
    }

}