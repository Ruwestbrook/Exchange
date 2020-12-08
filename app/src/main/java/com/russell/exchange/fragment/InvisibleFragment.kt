package com.russell.exchange.fragment

import android.app.Activity
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
author: russell
time: 2020/4/25:23:32
describe：用于申请权限的fragment
 */
typealias PermissionCallback = (Boolean,List<String>) -> Unit

class InvisibleFragment :Fragment() {

    private var callback:PermissionCallback? = null



    fun requestNow(cb:PermissionCallback?,vararg permissions:String){

        callback=cb

        requestPermissions(permissions,1)

    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode==1){

            val deniedList=ArrayList<String>()

            for ((index,result) in grantResults.withIndex()){
                if(result!=PackageManager.PERMISSION_GRANTED){
                    deniedList.add(permissions[index])
                }
            }

            val allGranted=deniedList.isEmpty()
            callback?.let { it(allGranted,deniedList) }

        }
    }

}


object PermissionX{
    private const val TAG="InvisibleFragment"

    @JvmStatic
    fun request(activity: FragmentActivity,vararg permissions:String,callback:PermissionCallback?){

        val fragmentManager=activity.supportFragmentManager

        val existedFragment=fragmentManager.findFragmentByTag(TAG)

        val fragment=if(existedFragment!=null){
            existedFragment as InvisibleFragment
        } else{
            val invisibleFragment=InvisibleFragment()
            fragmentManager.beginTransaction().add(invisibleFragment, TAG).commitNow()
            invisibleFragment
        }

        fragment.requestNow(callback,*permissions)
    }
}