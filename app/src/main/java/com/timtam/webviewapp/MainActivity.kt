package com.timtam.webviewapp

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var browser: WebView

    @SuppressLint("SetJavaScriptEnabled", "JavascriptInterface")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        browser = findViewById(R.id.browser)
        browser.settings.javaScriptEnabled = true
        browser.webViewClient = WebViewClient()
        registerForContextMenu(browser)
        browser.loadUrl(HTTP_URL)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val webViewHitTestResult: WebView.HitTestResult = browser.hitTestResult

        if (webViewHitTestResult.type == WebView.HitTestResult.IMAGE_TYPE ||
                webViewHitTestResult.type == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
            menu.setHeaderTitle("Download Image From Below")
            menu.add(0, 1, 0, "Save Image").setOnMenuItemClickListener {
                val imageURL: String? = webViewHitTestResult.extra

                if (URLUtil.isValidUrl(imageURL)) {
                    val request: DownloadManager.Request = DownloadManager.Request(Uri.parse(imageURL))
                    request.allowScanningByMediaScanner()
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    val downloadManager: DownloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    downloadManager.enqueue(request)

                    Toast.makeText(this@MainActivity, "Success from: $imageURL", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "Failed", Toast.LENGTH_LONG).show()
                }

                false
            }
        }
    }

    companion object {
        const val HTTP_URL = "https://www.instagram.com/timur_hm"
    }

}
