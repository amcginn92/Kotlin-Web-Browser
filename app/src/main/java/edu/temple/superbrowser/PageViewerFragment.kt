package edu.temple.superbrowser

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.EditText
import android.widget.ImageButton
import androidx.lifecycle.ViewModelProvider

const val PAGE_ID = "page_id"

class PageViewerFragment : Fragment() {

    val browserViewModel: BrowserViewModel by lazy {
        ViewModelProvider(requireActivity())[BrowserViewModel::class.java]
    }

    private lateinit var webView: WebView
    private lateinit var goButton: ImageButton
    private lateinit var bkButton: ImageButton
    private lateinit var fwdButton: ImageButton
    private lateinit var urlEditText: EditText

    private var pageId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getInt(PAGE_ID)?.run {
            pageId = this
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_page_viewer, container, false).apply {
            webView = findViewById(R.id.webView)
            goButton = findViewById(R.id.goButton)
            bkButton = findViewById(R.id.bkButton)
            fwdButton = findViewById(R.id.fwdButton)
            urlEditText = findViewById(R.id.urlEditText)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Enable Javascript for proper functioning websites
        webView.settings.javaScriptEnabled = true

        if (browserViewModel.pageTitles[pageId].isNullOrBlank())
            browserViewModel.updatePageTitle(pageId, "_blank")

        // Provision WebViewClient implementation that retrieves current page URL
        webView.webViewClient = object: WebViewClient () {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                url?.run {
                    urlEditText.setText(this)

                    // Display either title or URL
                    browserViewModel.updatePageTitle(pageId, view?.title ?: url)

                    view?.title?.run {
                        activity?.let{
                            (it as PageViewerInterface).updateTitle(pageId, this)
                        }
                    }
                }
            }
        }

        // Restore previous web state if present
        savedInstanceState?.run {
            webView.restoreState(this)
        }

        goButton.setOnClickListener {
            urlEditText.setText(cleanUrl(urlEditText.text.toString()))
            webView.loadUrl(urlEditText.text.toString())
        }

        bkButton.setOnClickListener {
            webView.goBack()
        }

        fwdButton.setOnClickListener {
            webView.goForward()
        }
    }

    // Fetch page title from
    fun getPageTitle(): String? {
        return webView.title
    }
    fun getPageUrl():String?{
        return webView.url
    }
    fun setPageUrl(myUrl:String){
        val cleanUrl = cleanUrl(myUrl)
        webView.loadUrl(cleanUrl.toString())
    }

    // Helper function to format malformed URLs
    private fun cleanUrl(url: String) : String {
        return if (url.startsWith("http"))
            url
        else
            "https://$url"
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        // Store current web state
        webView.saveState(outState)

    }

    interface PageViewerInterface {
        fun updateTitle (pageId: Int, title: String)

    }

    companion object {
        fun newInstance(id: Int) = PageViewerFragment().apply {
            arguments = Bundle().apply {
                putInt(PAGE_ID, id)
            }
        }
    }

}