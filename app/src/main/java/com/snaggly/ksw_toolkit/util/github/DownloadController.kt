package com.snaggly.ksw_toolkit.util.github

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import com.snaggly.ksw_toolkit.BuildConfig
import java.io.File

//Taken from example in https://androidwave.com/download-and-install-apk-programmatically/
class DownloadController(private val context: Context, private val url: String) {
    companion object {
        private const val FILE_NAME = "KSW-ToolKit_Service.apk"
        private const val FILE_BASE_PATH = "file://"
        private const val MIME_TYPE = "application/vnd.android.package-archive"
        private const val PROVIDER_PATH = ".provider"
        private const val APP_INSTALL_PATH = "\"application/vnd.android.package-archive\""
    }
    fun enqueueDownload() {
        var destination = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
        destination += FILE_NAME
        val uri = Uri.parse("$FILE_BASE_PATH$destination")
        val file = File(destination)
        if (file.exists()) file.delete()
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri)
        request.setMimeType(MIME_TYPE)
        request.setTitle("KSW-ToolKit-Service APK")
        request.setDescription("Downloading...")
        request.setDestinationUri(uri)
        showInstallOption(destination, uri)
        downloadManager.enqueue(request)
    }
    private fun showInstallOption(destination: String, uri: Uri) {
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val contentUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + PROVIDER_PATH, File(destination))
                val install = Intent(Intent.ACTION_VIEW)
                install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
                install.data = contentUri
                context.startActivity(install)
                context.unregisterReceiver(this)
            }
        }
        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }
}