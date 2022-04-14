package com.flyconcept.cameraapp

import android.app.Application
import android.content.Context
import com.secneo.sdk.Helper

class MApplication():Application(){

    private var cameraAppApplication: CameraAppApplication? = null
    override fun attachBaseContext(paramContext: Context?) {
        super.attachBaseContext(paramContext)
        Helper.install(this@MApplication)
        if (cameraAppApplication == null) {
            cameraAppApplication = CameraAppApplication()
            cameraAppApplication!!.setContext(this)
        }
    }

    override fun onCreate() {
        super.onCreate()
        cameraAppApplication!!.onCreate()
    }

}


