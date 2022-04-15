package com.flyconcept.cameraapp

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.flyconcept.cameraapp.databinding.ActivityConnectionBinding
import java.util.concurrent.atomic.AtomicBoolean

class ConnectionActivity : AppCompatActivity(), View.OnClickListener{
    var activityConnectionBinding: ActivityConnectionBinding? = null
    private val REQUIRED_PERMISSION_LIST = arrayOf<String>(
        Manifest.permission.VIBRATE,
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_WIFI_STATE,
        Manifest.permission.WAKE_LOCK,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.CHANGE_WIFI_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE
    )
    private val missingPermission: List<String> = ArrayList()
    private val isRegistrationInProgress: AtomicBoolean = AtomicBoolean(false)
    private val REQUEST_PERMISSION_CODE = 12345
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityConnectionBinding = ActivityConnectionBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_connection)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}