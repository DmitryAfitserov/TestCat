package com.app.testcat.helper

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.ContextCompat.getSystemService
import java.util.*


object DownloadHelper {

    fun download(url: String, context: Context){

        var fileName = url.substring(url.lastIndexOf('/') + 1)
        fileName = fileName.substring(0, 1).uppercase(Locale.getDefault()) + fileName.substring(1)

        val request = DownloadManager.Request(Uri.parse(url))
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(fileName)
            .setDescription("Downloading")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName)
            .setRequiresCharging(false)
            .setAllowedOverMetered(true)

        val downloadManager =  getSystemService(context ,DownloadManager::class.java)
        downloadManager?.enqueue(request)
    }

}