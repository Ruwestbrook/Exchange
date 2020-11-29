package com.russell.exchange

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView

/**
@author russell
@description:
@date : 2020/11/29 0:48
 */
class WebActivity : Activity() {

    private lateinit var webView:WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView=WebView(this)

        webView.apply {
            WebView.setWebContentsDebuggingEnabled(false)
            //  clearHistory()
            isDrawingCacheEnabled = true
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            addJavascriptInterface(AppJs(this@WebActivity), "AppJs")
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            setOnLongClickListener {
                val result = webView.hitTestResult
                if (result != null) {
                    val type = result.type
                    if (type == WebView.HitTestResult.IMAGE_TYPE) {
                        showSaveImageDialog(result)
                    }
                }
                false
            }
            setDownloadListener { url, _, _, _, _ ->
                val uri = Uri.parse(url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                if (intent.resolveActivity(packageManager) != null) {
                    startActivity(intent)
                }
            }
            settings.apply {
                var userAgentString = this.userAgentString
                userAgentString = getString(R.string.android_web_agent, userAgentString)
                this.userAgentString = userAgentString

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
                setRenderPriority(WebSettings.RenderPriority.NORMAL)

                javaScriptEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
                cacheMode = WebSettings.LOAD_DEFAULT
                databaseEnabled = true
                setAppCacheEnabled(true)
                setAppCachePath(externalCacheDir?.path)
                allowFileAccess = true
                domStorageEnabled = true
                useWideViewPort = true
                loadWithOverviewMode = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                }
            }
        }
    }

    private fun showSaveImageDialog(result: WebView.HitTestResult) {


    }

    fun setShouldForbidBackPress(i:Int){

    }

    fun getWebView():WebView{
        return webView
    }

    fun setBackPressJSMethod(method:String){

    }

}