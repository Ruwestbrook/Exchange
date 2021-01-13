package com.russell.exchange

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.webkit.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.russell.exchange.fragment.PermissionX
import com.russell.exchange.service.FileUtils
import org.json.JSONObject
import qiu.niorgai.StatusBarCompat
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLDecoder


/**
@author russell
@description:
@date : 2020/11/29 0:48
 */
class WebActivity : AppCompatActivity() {

    private lateinit var webView:WebView
    val RC_SIGN_IN=1
    val FILECHOOSER_RESULTCODE=9
    private lateinit var webTitle:TextView
    private lateinit var constraintLayout: ConstraintLayout
    private var titleText:String? = null
    var signData:String? = null
    private var showTitle=false
    private var rewriteTitle=true
    //true:web回退(点击返回键webview可以回退就回退，无法回退的时候关闭该页面)|false(点击返回键关闭该页面) 直接关闭页面
    private var webBack=true
    private var mUploadCallBackAboveL:ValueCallback<Array<Uri>>?=null

    @SuppressLint("SetJavaScriptEnabled", "QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        PermissionX.request(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            callback = null
        )
        StatusBarCompat.setStatusBarColor(this, Color.BLACK)
        webView=findViewById(R.id.web_view)
        webTitle=findViewById(R.id.web_title)
        constraintLayout=findViewById(R.id.title_view)
        Log.d(TAG, "onCreate: 222")
        webView.apply {
            WebView.setWebContentsDebuggingEnabled(false)
            //  clearHistory()
            isDrawingCacheEnabled = true
            scrollBarStyle = View.SCROLLBARS_INSIDE_OVERLAY
            addJavascriptInterface(AppJs(this@WebActivity), "AppJs")
            setLayerType(View.LAYER_TYPE_HARDWARE, null)

            webChromeClient=object :WebChromeClient(){
                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    mUploadCallBackAboveL = filePathCallback
                    showFileChooser()
                    return true
                }


            }
            webViewClient=object :WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                   // loadResult(true)
                    if(rewriteTitle)
                        titleText=view?.title
                    showTitle()
                }

                override fun onReceivedError(
                    view: WebView?,
                    request: WebResourceRequest?,
                    error: WebResourceError?
                ) {
                    super.onReceivedError(view, request, error)
                   // loadResult(false)
                }
            }


            setOnLongClickListener {
                val result = webView.hitTestResult
                val type = result.type
                if (type == WebView.HitTestResult.IMAGE_TYPE) {
                    showSaveImageDialog(result)
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
        webView.addJavascriptInterface(AppJs(this), "android")
        init()
     // webView.loadUrl("file:///android_asset/test.html")


    }




    /**
     * 打开一个基本配置的webview （不修改UA、可以缓存）
     * 打开新页面
     * 加载webview的情况分类(判断依据：url、postData、html)
     *    |-------1、只有url：webView.loadUrl()
     *    |-------2、有url和postData：webView.postUrl()
     *    |-------3、有html webView.loadDataWithBaseURL()
     *
     * @param json 打开web传参 选填
     * {"title":"", 打开时显示的标题
     *  "url":"", 加载的地址
     *  "hasTitleBar":false, 是否显示标题栏
     *  "rewriteTitle":true, 是否通过加载的Web重写标题
     *  "stateBarTextColor":"black", 状态栏字体颜色 black|white
     *  "titleTextColor":"#FFFFFF", 标题字体颜色
     *  "titleColor":"#FFFFFF", 标题背景色
     *  "postData":"", webView post方法时需要传参
     *  "html":"", 加载htmlCode,
     *  "webBack":true, true:web回退(点击返回键webview可以回退就回退，无法回退的时候关闭该页面)|false(点击返回键关闭该页面) 直接关闭页面
     * }
     */
    private fun init() {
        Log.d(TAG, "init: ")
        val data=intent.getStringExtra("page")
        if(data==null || data.isEmpty()){
            return
        }
        Log.d(TAG, "init: isEmptyisEmpty")
        val jsonObject=JSONObject(data)
        if(jsonObject.has("title"))
            titleText=jsonObject.getString("title")
        if(jsonObject.has("hasTitleBar"))
         showTitle=jsonObject.getBoolean("hasTitleBar")
        if(jsonObject.has("rewriteTitle"))
        rewriteTitle=jsonObject.getBoolean("rewriteTitle")
        if(jsonObject.has("stateBarTextColor")){
            val stateBarTextColor=jsonObject.getString("stateBarTextColor")
            if(stateBarTextColor == "white"){
                StatusBarCompat.setStatusBarColor(this, Color.WHITE)
            }else{
                StatusBarCompat.setStatusBarColor(this, Color.BLACK)
            }
        }
        if(jsonObject.has("titleTextColor")){
            val titleTextColor=jsonObject.getString("titleTextColor")
            webTitle.setTextColor(Color.parseColor(titleTextColor))
        }
        var postData=""
        if(jsonObject.has("postData")){
            postData=jsonObject.getString("postData")
        }
        var url=""

        if(jsonObject.has("url")){
            url=jsonObject.getString("url")
        }
        Log.d(TAG, "init: url=$url")
        var html=""
        if(jsonObject.has("html")){
            html=jsonObject.getString("html")
        }
        if(jsonObject.has("webBack")){
            webBack=jsonObject.getBoolean("webBack")
        }
        if(url.isNotEmpty()){
            if(postData.isNotEmpty()){
                webView.postUrl(url, postData.toByteArray())
            }else{
                webView.loadUrl(url)
            }
        }else{
            if(html.isNotEmpty()){
                webView.loadDataWithBaseURL(html, "", null, null, "")
            }
        }
    }


    private fun showTitle() {
        if(showTitle){
            constraintLayout.visibility=View.VISIBLE
            webTitle.text=titleText
        }else{
            constraintLayout.visibility=View.GONE
        }

    }

    private var mCameraFilePath:String?=null
    /**
     * 打开选择文件/相机
     */
    private fun showFileChooser() {

        val i = Intent(Intent.ACTION_GET_CONTENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.type = "*/*" //文件上传
        startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE)

    }

    private fun showSaveImageDialog(result: WebView.HitTestResult) {





    }

    var forbid:Int=0
    fun setShouldForbidBackPress(i: Int){
        forbid=i
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
    private  val CHOOSE_PHOTO = 188
    var callbackMethod:String?=null
    fun takePicture(name: String){
        callbackMethod=name
        val intent = Intent()
        intent.action = Intent.ACTION_PICK
        intent.type = "image/*"
        startActivityForResult(intent, CHOOSE_PHOTO)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d(TAG, "onActivityResult: resultCode=$resultCode,requestCode=$requestCode")
            when (requestCode) {
                CHOOSE_PHOTO -> {
                    runOnUiThread {
                        val uri: Uri = data?.data!! //获取图片uri
                        val pfd = contentResolver.openFileDescriptor(uri, "r")
                        val bitmap = BitmapFactory.decodeFileDescriptor(pfd?.fileDescriptor)
                        var str = bitmapToBase64(bitmap)
                        str = str?.replace("\\s*", "")
                        val builder = StringBuilder(callbackMethod)
                        builder.append("('data:image/png;base64,$str')")
                        val methodName = builder.toString()
                        val javaScript = "javascript:$methodName"
                        Log.d(TAG, "takePicture: $javaScript")
                        webView.evaluateJavascript(javaScript, null)
                    }


                }

                RC_SIGN_IN -> {


                    try {
                        val task: Task<GoogleSignInAccount> =
                            GoogleSignIn.getSignedInAccountFromIntent(
                                data
                            )
                        val account: GoogleSignInAccount?
                        account = task.getResult(ApiException::class.java)
                        login(account)
                        Log.d(TAG, "onActivityResult: ${account?.email}")
                        Log.d(TAG, "onActivityResult: ${account?.displayName}")
                    } catch (e: ApiException) {
                        Log.d(TAG, "onActivityResult: ${e.toString()}")
                    }

                }
            }
      //  }


        if (requestCode == FILECHOOSER_RESULTCODE) {
            var result: Uri? = if (resultCode != RESULT_OK) null else data?.data
            Log.d(TAG, "onActivityResult: $result")
            if (result != null) {
                val path: String? = FileUtils.getPath(this, result)
                if (path?.isNotEmpty()==true) {
                    val f = File(path)
                    if (f.exists() && f.isFile) {
                        val newUri = Uri.fromFile(f)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (mUploadCallBackAboveL != null) {
                                if (newUri != null) {
                                    mUploadCallBackAboveL!!.onReceiveValue(arrayOf(newUri))
                                    mUploadCallBackAboveL = null
                                    return
                                }
                            }
                        }
                    }
                }
            }
            clearUploadMessage()
            return
        }

    }

    private fun clearUploadMessage() {
        if (mUploadCallBackAboveL != null) {
            mUploadCallBackAboveL!!.onReceiveValue(null)
            mUploadCallBackAboveL = null
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

    //   "webBack":true, true:web回退(点击返回键webview可以回退就回退，无法回退的时候关闭该页面)|
    //   false(点击返回键关闭该页面) 直接关闭页面
    fun back(view: View?) {
        if(webBack){
            if(webView.canGoBack()){
                if(webView.copyBackForwardList().size <2){
                    finish()
                }else{
                    webView.goBack()
                }
            }else{
                finish()
            }
        }else{
            finish()
        }
    }


    //是否禁止返回键 1:禁止
    override fun onBackPressed() {
        if(forbid==1){
            return
        }
        back(null)
    }


    private fun login(account: GoogleSignInAccount?){

        try {

            Thread {
                Log.d(TAG, "login: $signData")
                val sign = JSONObject(signData)
                val host = sign.getString("host")
                var connection: HttpURLConnection? = null
                val response = StringBuilder()
                val url = URL(
                    host +
                            "/user/google/doLogin2.do?" +
                            "id=" + account?.id +
                            "&name=" + URLDecoder.decode(account?.displayName) +
                            "&type=0" +
                            "&email=" + account?.email +
                            "&sign=" + sign.getString("sign")
                )
                Log.d(TAG, "login: $url")
                connection=url.openConnection() as HttpURLConnection
                connection.connectTimeout =60000
                connection.readTimeout =60000
                val input=connection.inputStream
                val reader=BufferedReader(InputStreamReader(input))
                reader.use {
                    reader.forEachLine {
                        response.append(it)
                    }
                }
            val jsonObject=JSONObject(response.toString())

            if(jsonObject.getInt("code")==200){

                val jsonData=jsonObject.getJSONObject("data")

                if(jsonData.has("token1")){
                    syncCookie(host, jsonData.getString("token1"), "token1")
                    syncCookie(jsonData.getString("url"), jsonData.getString("token1"), "token1")
                }
                if(jsonData.has("token2")){
                    syncCookie(host, jsonData.getString("token2"), "token2")
                    syncCookie(jsonData.getString("url"), jsonData.getString("token2"), "token2")
                }
                val advIntent=Intent(this@WebActivity, WebActivity::class.java)
                var url=jsonData.getString("url")
                if(url.contains("\\u002F")){
                    url=Uri.decode(url)
                }

                runOnUiThread {
                    webView.loadUrl(url)
                }
            }
            }.start()

        }catch (e: Exception){
            Log.d(TAG, "login Exception: $e")
        }

    }



    /**
     * 给WebView同步Cookie
     *
     * @param context 上下文
     * @param url     可以使用[domain][host]
     */
    private fun syncCookie(host: String, token: String, name: String) {
        val cookieManager = CookieManager.getInstance()
        cookieManager.setCookie(host, "$name=$token;expires=1; path=/")
        CookieSyncManager.getInstance().sync()

    }

    fun showTitleBar(visible: Boolean) {
        Log.d(TAG, "showTitleBar: $visible")
        showTitle=visible
        runOnUiThread {
            if(showTitle){
                constraintLayout.visibility=View.VISIBLE
                webTitle.text=titleText
            }else{
                constraintLayout.visibility=View.GONE
            }
        }
    }

}