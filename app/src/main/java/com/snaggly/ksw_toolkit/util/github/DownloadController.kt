package com.snaggly.ksw_toolkit.util.github

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.google.gson.Gson
import com.snaggly.ksw_toolkit.BuildConfig
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.LinkedList
import java.util.Queue

//Taken from example in https://androidwave.com/download-and-install-apk-programmatically/
object DownloadController {
    data class DownloadQueue(val request: DownloadManager.Request, val destination: String)
    data class DownloadableContent(val url: String, val fileName: String)

    private const val FILE_NAME = "KSW-ToolKit_Service.apk"
    private const val FILE_BASE_PATH = "file://"
    private const val MIME_TYPE = "application/vnd.android.package-archive"
    private const val PROVIDER_PATH = ".provider"
    private const val APP_INSTALL_PATH = "\"application/vnd.android.package-archive\""

    private val downloadQueue : Queue<DownloadQueue> = LinkedList()

    fun getServiceGitHubRelease(): GitHubRelease {
        return getRelease(URL("https://api.github.com/repos/Snaggly/KSW-ToolKit-Service/releases/latest"))
    }

    fun getClientGitHubRelease(): GitHubRelease {
        return getRelease(URL("https://api.github.com/repos/Snaggly/KSW-ToolKit/releases/latest"))
    }

    fun draftDownload(latestGitHubRelease: GitHubRelease?): DownloadableContent? {
        var assetIndex = -1
        for (i in latestGitHubRelease!!.assets.indices) {
            if (latestGitHubRelease.assets[i].content_type == "application/vnd.android.package-archive") {
                assetIndex = i
                break
            }
        }
        return if (assetIndex >= 0)
            DownloadableContent(latestGitHubRelease.assets[assetIndex].browser_download_url, latestGitHubRelease.assets[assetIndex].name)
        else
            null
    }

    private fun getRelease(url: URL): GitHubRelease {
        val connection = url.openConnection() as HttpURLConnection
        connection.connectTimeout = 5000
        return Gson().fromJson(
            BufferedReader(InputStreamReader(connection.inputStream)),
            GitHubRelease::class.java
        )
    }

    fun enqueueDownload(context: Context, dc: DownloadableContent, br: BroadcastReceiver) {
        var destination =
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
        destination += dc.fileName
        val uri = Uri.parse("$FILE_BASE_PATH$destination")
        val file = File(destination)
        if (file.exists()) file.delete()
        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(dc.url)
        val request = DownloadManager.Request(downloadUri)
        request.setMimeType(MIME_TYPE)
        request.setTitle("KSW-ToolKit-Service APK")
        request.setDescription("Downloading...")
        request.setDestinationUri(uri)

        if (downloadQueue.isEmpty()) {
            downloadQueue.add(DownloadQueue(request, destination))
            context.registerReceiver(br, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
            downloadManager.enqueue(request)
        } else {
            downloadQueue.add(DownloadQueue(request, destination))
        }
    }

    fun onReceive(context: Context?, br: BroadcastReceiver) {
        val currentHead = downloadQueue.peek()
        if (context == null || currentHead == null)
            return

        val contentUri = FileProvider.getUriForFile(
            context,
            BuildConfig.APPLICATION_ID + PROVIDER_PATH,
            File(currentHead.destination)
        )
        val install = Intent(Intent.ACTION_VIEW)
        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
        install.data = contentUri
        context.startActivity(install)

        val nextOnQueue = downloadQueue.poll()
        if (nextOnQueue == null) {
            context.unregisterReceiver(br)
        }
        else {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(nextOnQueue.request)
        }
    }
}