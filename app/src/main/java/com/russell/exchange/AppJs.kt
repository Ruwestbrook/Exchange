package com.russell.exchange

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.webkit.JavascriptInterface
import com.facebook.appevents.AppEventsLogger
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.gson.JsonObject
import io.branch.referral.util.BranchEvent

/**
@author russell
@description:
@date : 2020/11/29 0:47
 */
class AppJs(val mContext:Context) {



    private  val TAG = "Appjs"
    /**
     * 获取设备id
     * 必须保证有值
     * 获取不到的时候生成一个UUID
     */
    @SuppressLint("HardwareIds")
    @JavascriptInterface
    fun getDeviceId(): String {
        val tm=mContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return tm.deviceId

    }

    /**
     * 获取ANDROID_ID
     * public static final String ANDROID_ID
     */
    @JavascriptInterface
    fun getGoogleId(): String {
       return Settings.System.getString(mContext.contentResolver, Settings.System.ANDROID_ID);
    }

//    /**
//     * 获取pushId
//     * 个推id 现在只需要空实现传空串
//     */
//    @JavascriptInterface
//    fun takePushId(): String {
//        return ""
//    }
//
//    /**
//     * 获取fcm 令牌
//     * 看FCM推送的文档，有监听和获取令牌的方法
//     * 详情见第八点
//     */
//    @JavascriptInterface
//    fun takeFCMPushId(): String {
//        //fcm生成的注册令牌
//        //TODO
//    }

    /**
     * 集成branch包的时候已经带有Google Play Service核心jar包
     * 获取gpsadid 谷歌广告id
     * AdvertisingIdClient.getAdvertisingIdInfo() 异步方法
     */
    @JavascriptInterface
    fun getGaId(): String? {
        if(AdvertisingIdClient.getAdvertisingIdInfo(mContext)==null){
            return null
        }
        return AdvertisingIdClient.getAdvertisingIdInfo(mContext).toString()
    }

    /**
     * 获取应用渠道
     */
    @JavascriptInterface
    fun takeChannel(): String {
        return "google"
    }

    /**
     * H5调用原生谷歌登录
     * 后续流程看第七点
     *
     *  @param data {"sign":"","host":"https://bb.skr.today"}
     */
    @JavascriptInterface
    fun openGoogle(data: String) {
        //TODO
    }

    /**
     * 头像获取
     *  流程:H5调用方法 - 打开图片选择器 - 回调返回H5
     *  base64使用格式：Base64.NO_WRAP
     *
     * @param callbackMethod 回传图片时调用H5的方法名
     */
    @JavascriptInterface
    fun takePortraitPicture(callbackMethod: String,str: String) {
        // TODO
        // 参考实现：成员变量记录下js方法名，图片转成base64字符串后调用该js方法传递给H5
        // 下面一段代码仅供参考，能实现功能即可
        if (!TextUtils.isEmpty(callbackMethod)) {
            val builder = StringBuilder(callbackMethod).append("(")
            builder.append("'").append("data:image/png;base64,$str").append("'")
            builder.append(")")
            val methodName = builder.toString()
            val javaScript = "javascript:$methodName"
            val  webView=(mContext as WebActivity).getWebView()
            webView.evaluateJavascript(javaScript, null)
        }
    }

    /**
     * 控制webview是否显示 TitleBar
     * （点击返回键webview 后退）
     * @param visible
     */
    @JavascriptInterface
    fun showTitleBar(visible: Boolean) {
        //TODO
    }

    /**
     * 由h5控制是否禁用系统返回键
     * @param forbid     是否禁止返回键 1:禁止
     */
    @JavascriptInterface
    fun shouldForbidSysBackPress(forbid: Int) {
        //TODO
        if (mContext is WebActivity) {
            //WebActivity成员变量记录下是否禁止
            mContext.setShouldForbidBackPress(forbid)
            //WebActivity 重写onBackPressed方法 变量为1时禁止返回操作
        }
    }

    /**
     * 返回键调用h5控制
     *
     * @param forbid         是否禁止返回键 1:禁止
     * @param callbackMethod 反回时调用的h5方法 例如:detailBack() webview需要执行javascrept:detailBack()
     */
    @JavascriptInterface
    fun forbidBackForJS(forbid: Int, methodName: String) {
        //TODO
        if (mContext is WebActivity) {
            mContext.setShouldForbidBackPress(forbid)
            //同上
            mContext.setBackPressJSMethod(methodName)
            //WebActivity成员变量记录下js方法名 在禁止返回时调用js方法
        }
    }

    /**
     * 使用手机里面的浏览器打开 url
     *
     * @param url 打开 url
     */
    @JavascriptInterface
    fun openBrowser(url: String) {
        Log.e(TAG, "openBrowser url$url")
        if (mContext is WebActivity) {
            val uri = Uri.parse(url)
            val intent = Intent().apply {
                action = Intent.ACTION_VIEW
                data = uri
            }
            if (intent.resolveActivity(mContext.packageManager) != null) {
                mContext.startActivity(intent)
            }
        }
    }

    /**
     *  AppJs是否存在交互方法 告诉H5是否存在传入的对应方法
     *
     * @param name 方法名
     */
    @JavascriptInterface
    fun isContainsName(callbackMethod: String, name: String) {
        var has = false
        Log.v(TAG, "isContainsName:${callbackMethod};${name}")
        val classMethods = MethodUtil.getClassMethods(AppJs::class.java)
        classMethods?.let {
            for (method in it) {
                if (method != null) {
                    has = method.name == name
                    if (has) {
                        return@let
                    }
                }
            }
        }
        if (mContext is WebActivity) {
            mContext.runOnUiThread {
                val webView = mContext.getWebView()
                val javaScript = "javascript:$callbackMethod('$has')"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    webView.evaluateJavascript(javaScript, null)
                } else {
                    webView.loadUrl(javaScript)
                }
            }
        }
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
    @JavascriptInterface
    fun openPureBrowser(json: String) {
        Log.v(TAG, "openPureBrowser json:$json")
    }

    /**
     * branch事件统计
     * @param eventName 统计事件名称
     */
    @JavascriptInterface
    fun branchEvent(eventName: String) {
        Log.v(TAG, "branchEvent:\neventName:${eventName}")
        BranchEvent(eventName)
            .logEvent(mContext)
    }

    /**
     * branch事件统计
     * @param eventName 统计时间名称
     * @param parameters 自定义统计参数
     */
    @JavascriptInterface
    fun branchEvent(eventName: String, parameters: String) {
        Log.v(TAG, "branchEvent:\neventName:${eventName}\nparameters:$parameters")
        val branchEvent = BranchEvent(eventName)
        val obj = JsonObject().getAsJsonObject(parameters)
        val bundle = Bundle()
        for (entry in obj.entrySet()) {
            val value = entry.value
            bundle.putString(entry.key, value.asString)
            branchEvent.addCustomDataProperty(
                entry.key,
                value.asString
            )
        }
        branchEvent
            .logEvent(mContext)
    }

    /**
     * branch事件统计
     * @param eventName 统计事件名称
     * @param parameters 自定义统计参数
     * @param alias 事件别名
     */
    @JavascriptInterface
    fun branchEvent(eventName: String, parameters: String, alias: String) {
        Log.v(TAG, "branchEvent:\neventName:${eventName}\nparameters:$parameters\nalias$alias")
        val branchEvent = BranchEvent(eventName)
        val obj = JsonObject().getAsJsonObject(parameters)
        val bundle = Bundle()
        for (entry in obj.entrySet()) {
            val value = entry.value
            bundle.putString(entry.key, value.asString)
            branchEvent.addCustomDataProperty(
                entry.key,
                value.asString
            )
        }
        branchEvent
            .setCustomerEventAlias(alias)
            .logEvent(mContext)
    }

    /**
     * facebook事件统计
     * @param eventName 事件名称
     * @param valueToSum 计数数值
     * @param parameters 自定义统计参数json{}需要全是String类型
     */
    @JavascriptInterface
    fun facebookEvent(eventName: String, valueToSum: Double, parameters: String) {
        Log.v(
            TAG,
            "facebookEvent:\neventName:${eventName}\nvalueToSum:$valueToSum\nparameters:$parameters"
        )
        val logger = AppEventsLogger.newLogger(mContext)
        val obj = JsonObject().getAsJsonObject(parameters)
        val bundle = Bundle()
        for (entry in obj.entrySet()) {
            val value = entry.value
            bundle.putString(entry.key, value.asString)
        }
        logger.logEvent(eventName, valueToSum, bundle)
    }

    /**
     * facebook事件统计
     * @param eventName 事件名称
     * @param parameters 自定义统计参数json{}需要全是String类型
     */
    @JavascriptInterface
    fun facebookEvent(eventName: String, parameters: String) {
        Log.v(TAG, "facebookEvent:\neventName:${eventName}\nparameters:$parameters")
        val logger = AppEventsLogger.newLogger(mContext)
        val obj = JsonObject().getAsJsonObject(parameters)
        val bundle = Bundle()
        for (entry in obj.entrySet()) {
            val value = entry.value
            bundle.putString(entry.key, value.asString)
        }
        logger.logEvent(eventName, bundle)
    }

    /**
     * facebook计数统计
     * @param eventName 事件名称
     * @param valueToSum 计数数值
     */
    @JavascriptInterface
    fun facebookEvent(eventName: String, valueToSum: Double) {
        Log.v(TAG, "facebookEvent:\neventName:${eventName}\nvalueToSum:$valueToSum")
        val logger = AppEventsLogger.newLogger(mContext)
        logger.logEvent(eventName, valueToSum)
    }

    /**
     * facebook 计数事件统计
     * @param eventName 事件名称
     */
    @JavascriptInterface
    fun facebookEvent(eventName: String) {
        Log.v(TAG, "facebookEvent:\neventName:${eventName}")
        val logger = AppEventsLogger.newLogger(mContext)
        logger.logEvent(eventName)
    }

    /**
     * firebase事件统计
     */
    @JavascriptInterface
    fun firebaseEvent(category: String, parameters: String) {
        Log.v(TAG, "firebaseEvent:\ncategory:${category}\nparameters:$parameters")
        val obj = JsonObject().getAsJsonObject(parameters)
        val bundle = Bundle()
        for (entry in obj.entrySet()) {
            val value = entry.value
            bundle.putString(entry.key, value.asString)
        }
        FirebaseAnalytics.getInstance(mContext).logEvent(category, bundle)
    }

}