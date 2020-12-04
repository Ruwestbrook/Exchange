package com.russell.exchange

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import java.io.ByteArrayOutputStream
import java.io.IOException


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

        webView=WebView(this)

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

        webView.loadUrl("file:///android_asset/test.html")
        webView.addJavascriptInterface(AppJs(this), "android")
        setContentView(webView)
    }

    private fun showSaveImageDialog(result: WebView.HitTestResult) {


    }

    var forbid:Int=0
    fun setShouldForbidBackPress(i: Int){
        forbid=i
    }

    override fun onBackPressed() {
        if(forbid==1){
            return
        }
        super.onBackPressed()
    }

    fun getWebView():WebView{
        return webView
    }

    fun setBackPressJSMethod(method: String){
        runOnUiThread {
            val javaScript = if(method.endsWith(")")) "javascript:$method" else "javascript:$method()"
            webView.evaluateJavascript(javaScript, null)
        }

    }


    private  val TAG = "WebActivity"
    var callbackMethod:String?=null
    fun takePicture(name: String){
        callbackMethod=name


        PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .forResult(PictureConfig.CHOOSE_REQUEST)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                PictureConfig.CHOOSE_REQUEST -> {
                    // 图片选择结果回调
                    val selectList = PictureSelector.obtainMultipleResult(data)
                    // 例如 LocalMedia 里面返回三种 path
                    // 1.media.getPath(); 为原图 path
                    // 2.media.getCutPath();为裁剪后 path，需判断 media.isCut();是否为 true
                    // 3.media.getCompressPath();为压缩后 path，需判断 media.isCompressed();是否为 true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    if (selectList.size > 0) {
                        runOnUiThread {
                        var str = bitmapToBase64(BitmapFactory.decodeFile(selectList[0].path))
                        str = str?.replace("\\s*", "")
                        val builder = StringBuilder(callbackMethod)
                        builder.append("('data:image/png;base64,$str')")
                        val methodName = builder.toString()
                        val javaScript = "javascript:$methodName"
                        Log.d(TAG, "takePicture: $javaScript")
                        webView.evaluateJavascript(javaScript, null)
                    }

                    }
                }
            }
        }
    }


    /**
     * bitmap转为base64
     * @param bitmap
     * @return
     */
    fun bitmapToBase64(bitmap: Bitmap?): String? {
        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            if (bitmap != null) {
                baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                baos.flush()
                baos.close()
                val bitmapBytes: ByteArray = baos.toByteArray()
                result = Base64.encodeToString(bitmapBytes, Base64.NO_WRAP)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return result
    }




}