package com.flyconcept.cameraapp

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.core.content.ContextCompat
import dji.common.error.DJIError
import dji.common.error.DJISDKError
import dji.sdk.base.BaseComponent
import dji.sdk.base.BaseProduct
import dji.sdk.base.BaseProduct.ComponentKey
import dji.sdk.camera.Camera
import dji.sdk.products.Aircraft
import dji.sdk.products.HandHeld
import dji.sdk.sdkmanager.DJISDKInitEvent
import dji.sdk.sdkmanager.DJISDKManager
import dji.sdk.sdkmanager.DJISDKManager.SDKManagerCallback


class CameraAppApplication():Application() {

    val FLAG_CONNECTION_CHANGE = "fpv_tutorial_connection_change"

    private var mProduct: BaseProduct? = null
    var mHandler: Handler? = null

    private var instance: Application? = null

    fun setContext(application: Application?) {
        instance = application
    }

    override fun getApplicationContext(): Context? {
        return instance
    }

    fun CameraAppApplication() {}

    @Synchronized
    fun getProductInstance(): BaseProduct? {
        if (null == mProduct) {
            mProduct = DJISDKManager.getInstance().product
        }
        return mProduct
    }

    @Synchronized
    fun getCameraInstance(): Camera? {
        if (getProductInstance() == null) return null
        var camera: Camera? = null
        if (getProductInstance() is Aircraft) {
            camera = (getProductInstance() as Aircraft?)!!.camera
        } else if (getProductInstance() is HandHeld) {
            camera = (getProductInstance() as HandHeld?)!!.camera
        }
        return camera
    }

    override fun onCreate() {
        super.onCreate()
        mHandler = Handler(Looper.getMainLooper())
        /**
         * When starting SDK services, an instance of interface DJISDKManager.DJISDKManagerCallback will be used to listen to
         * the SDK Registration result and the product changing.
         */
        //Listens to the SDK registration result
        val mDJISDKManagerCallback: SDKManagerCallback = object : SDKManagerCallback {
            //Listens to the SDK registration result
            override fun onRegister(djiError: DJIError) {
                val handler = Handler(Looper.getMainLooper())
                if (djiError === DJISDKError.REGISTRATION_SUCCESS) {
                    handler.post {
                        Toast.makeText(
                            applicationContext,
                            "SDK register success",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    DJISDKManager.getInstance().startConnectionToProduct()
                } else {
                    handler.post {
                        Toast.makeText(
                            applicationContext,
                            "SDK register fail",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            override fun onProductDisconnect() {
                notifyStatusChange()
            }

            override fun onProductConnect(baseProduct: BaseProduct) {
                notifyStatusChange()
            }

            override fun onProductChanged(baseProduct: BaseProduct) {
                notifyStatusChange()
            }

            override fun onComponentChange(
                componentKey: ComponentKey,
                oldComponent: BaseComponent,
                newComponent: BaseComponent
            ) {
                if (newComponent != null) {
                    newComponent.setComponentListener { notifyStatusChange() }
                }
            }

            override fun onInitProcess(djisdkInitEvent: DJISDKInitEvent, i: Int) {}
            override fun onDatabaseDownloadProgress(l: Long, l1: Long) {}
        }

        //Check the permissions before registering the application for android system 6.0 above.
        val permissionCheck = ContextCompat.checkSelfPermission(
            applicationContext!!,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permissionCheck2 = ContextCompat.checkSelfPermission(
            applicationContext!!,
            Manifest.permission.READ_PHONE_STATE
        )
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || permissionCheck == 0 && permissionCheck2 == 0) {
            //This is used to start SDK services and initiate SDK.
            DJISDKManager.getInstance().registerApp(applicationContext, mDJISDKManagerCallback)
            Toast.makeText(applicationContext, "registering, pls wait...", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(
                applicationContext,
                "Please check if the permission is granted.",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun notifyStatusChange() {
        mHandler!!.removeCallbacks(updateRunnable)
        mHandler!!.postDelayed(updateRunnable, 500)
    }

    private val updateRunnable = Runnable {
        val intent = Intent(FLAG_CONNECTION_CHANGE)
        applicationContext!!.sendBroadcast(intent)
    }
}