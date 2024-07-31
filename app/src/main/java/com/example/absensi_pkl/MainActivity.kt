package com.example.absensi_pkl

import android.app.Instrumentation.ActivityResult
import android.content.pm.PackageManager
import android.health.connect.datatypes.DataOrigin
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.GeolocationPermissions
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var webviw: WebView


    private val LOCATION_PERMISSION_REQUEST_CODE = 1002

    private val cameraPermissionLaunc = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        isGrant : Boolean -> if (isGrant){

    }else{

    }

    }
    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var fineLocationGranted =
                permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
            var coarseLocationGranted =
                permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

            if (fineLocationGranted || coarseLocationGranted) {
                // Izin lokasi diberikan
                // Lakukan tindakan terkait lokasi di sini jika diperlukan
            } else {
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                    fineLocationGranted =
                        permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: false
                    coarseLocationGranted =
                        permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), PERMISSION_REQUEST_CODE)
        }

        webviw = findViewById(R.id.wbviw)

        webviw.webViewClient = WebViewClient()

        val webSettings: WebSettings = webviw.settings
        webviw.settings.javaScriptEnabled = true
        webviw.settings.domStorageEnabled = true

// Untuk WebView di Android 5.0 (Lollipop) dan lebih tinggi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webviw.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webSettings.javaScriptEnabled = true
        webviw.settings.javaScriptCanOpenWindowsAutomatically = true
        webviw.settings.pluginState = WebSettings.PluginState.ON

        webviw.settings.mediaPlaybackRequiresUserGesture = false
        webviw.clearCache(true)

        webviw.loadUrl("https://pkl.smkn1panyingkiran.sch.id/apps/")


        webviw.webChromeClient = object : WebChromeClient(){
            override fun onPermissionRequest(request: PermissionRequest) {
                runOnUiThread {
                    request.grant(request.resources)
                }
            }
            override fun onShowCustomView(view: View, callback: CustomViewCallback) {
                // Handle custom view (e.g., fullscreen video) if needed
                super.onShowCustomView(view, callback)
            }

            override fun onHideCustomView() {
                // Handle hiding custom view if needed
                super.onHideCustomView()
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: WebChromeClient.FileChooserParams?
            ): Boolean {
                // Handle file chooser if needed
                return true
            }


            override fun onGeolocationPermissionsShowPrompt(
                origin: String,
                callback: GeolocationPermissions.Callback
            ) {
                if (ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    locationPermissionLauncher.launch(arrayOf(
                        android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ))
                } else {
                    callback.invoke(origin, true, false)
                }
            }
        }

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
            }
        }
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1001
    }
}