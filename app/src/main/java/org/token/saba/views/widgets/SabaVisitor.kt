package org.token.saba.views.widgets

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient

class SabaVisitor : WebView {
    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        privateBrowsing: Boolean
    ) : super(context, attrs, defStyleAttr, privateBrowsing) {
        init(context)
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun init(context: Context?) {
        visibility = GONE
        settings.javaScriptEnabled = true
        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest
            ): Boolean {
                return false
            }
        }
        //        VolleyInstants.getInstance(context).StringRequest(BuildConfig.URL_VISITOR, (string, error) -> {
//            if (error == null) {
//                VisitorModel[] visitorModels = new GsonBuilder().create().fromJson(string, VisitorModel[].class);
//
//                int r = new Random().nextInt(visitorModels.length);
//
//                loadUrlWebsite(visitorModels[r].getSite());
//            }
//        });
//        loadUrlWebsite("https://cafebazaar.ir/app/"+ BuildConfig.APPLICATION_ID);
    }

    fun loadUrlWebsite(url: String?) {
        this.loadUrl(url!!)
    }

    override fun setVisibility(visibility: Int) {
        super.setVisibility(GONE)
    }
}
