package com.russell.exchange

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.branch.referral.Branch
import io.branch.referral.BranchError
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL


class SplashActivity : AppCompatActivity() {


    var imageView:ImageView?=null
    var h5Url:String?=null
    var textView:TextView?=null
    private val handler= @SuppressLint("HandlerLeak")
    object:Handler(){

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
           val jsonObject = JSONObject()
            val intent=Intent(this@SplashActivity, WebActivity::class.java)
            jsonObject.put("url",h5Url)
            intent.putExtra("page", jsonObject.toString())
            Log.d(TAG, "handleMessage:startActivity webView ")
            startActivity(intent)
            if(msg.what!=1){
                val advUrl=msg.obj as String
                val advIntent=Intent(this@SplashActivity, WebActivity::class.java)
                jsonObject.put("url",advUrl)
                intent.putExtra("page", jsonObject.toString())
                startActivity(advIntent)
            }
            finish()
        }

    }

    override fun finish() {
        super.finish()
        handler.removeCallbacksAndMessages(null)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        imageView=findViewById(R.id.image)
        textView=findViewById(R.id.jump)


        Thread{
            getMessage()
        }.start()

    }
    private  val TAG = "SplashActivity"

    private fun getMessage(){

        try {

            var connection:HttpURLConnection?=null
            val response=StringBuilder()
            val url= URL(
                "http://api2.fpiopv.com" +
                        "/admin/client/vestSign.do?" +
                        "vestCode=CCFVZ3BD" +
                        "&version=" +getVersionCodeByUser()+
                        "&channelCode=google" +
                        "&deviceId=" + AppJs(this).getDeviceId() +
                        "&timestamp=" + System.currentTimeMillis()
            )
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
            Log.d(TAG, "getMessage: $response")
            val responseMessage=JSONObject(response.toString())
            val jsonObject=responseMessage.getJSONObject("data")
            h5Url= jsonObject.getString("h5Url")

            if((jsonObject.get("status") as Int)==0 ){
              if(jsonObject.getInt("advOn")==1 && jsonObject.has("advImg")){
                  val bitmap=returnBitMap(jsonObject.getString("advImg"))
                  if(bitmap!=null){
                      imageView?.setImageBitmap(bitmap)
                      imageView?.setOnClickListener {
                          handler.removeMessages(1)
                          val message=Message()
                          message.obj=jsonObject.getString("advImg")
                          message.what=2
                          handler.sendMessage(message)
                      }
                      textView?.visibility=View.VISIBLE
                      textView?.setOnClickListener {
                          handler.removeMessages(1)
                          handler.sendEmptyMessage(1)
                      }
                  }else{
                      handler.sendEmptyMessageDelayed(1, 5000)
                  }
              }else{
                  handler.sendEmptyMessage(1)
              }
            }else{
                handler.sendEmptyMessage(1)
            }
        }catch (e: Exception){
            Log.d(TAG, "getMessage: $e")
            startActivity(Intent(this, MainActivity::class.java))
        }

    }

    override fun onStart() {
        super.onStart()
        // Branch init
        Branch.sessionBuilder(this).withCallback(branchListener).withData(this.intent?.data).init()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        this.intent = intent
        // Branch reinit (in case Activity is already in foreground when Branch link is clicked)
        Branch.sessionBuilder(this).withCallback(branchListener).reInit()
    }

    object branchListener : Branch.BranchReferralInitListener {
        override fun onInitFinished(referringParams: JSONObject?, error: BranchError?) {
            if (error == null) {
                Log.i("BRANCH SDK", referringParams.toString())
                // Retrieve deeplink keys from 'referringParams' and evaluate the values to determine where to route the user
                // Check '+clicked_branch_link' before deciding whether to use your Branch routing logic
            } else {
                Log.e("BRANCH SDK", error.message)
            }
        }
    }

    private fun returnBitMap(url: String?): Bitmap? {
        val myFileUrl: URL
        var bitmap: Bitmap? = null
        try {
            myFileUrl = URL(url)
            val conn: HttpURLConnection = myFileUrl.openConnection() as HttpURLConnection
            conn.doInput = true
            conn.connect()
            val `is`: InputStream = conn.inputStream
            bitmap = BitmapFactory.decodeStream(`is`)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bitmap
    }

    /**
     * [获取应用程序版本名称信息]
     * @param context
     * @return 当前应用的版本名称
     */
    @Synchronized
    fun getVersionCodeByUser(): Int {
        try {
            val packageManager: PackageManager = packageManager
            val packageInfo: PackageInfo = packageManager.getPackageInfo(
                packageName, 0
            )
            return packageInfo.versionCode
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return 0
    }

}