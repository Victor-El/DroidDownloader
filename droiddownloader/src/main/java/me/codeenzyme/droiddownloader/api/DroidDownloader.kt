package me.codeenzyme.droiddownloader.api

import me.codeenzyme.droiddownloader.api.DownloadStatus

interface DroidDownloader {

    suspend fun downloadFile(url: String, dir: String, filename: String): DownloadStatus

    suspend fun downloadFileWithBearerAuth(url: String, dir: String, filename: String, auth: String): DownloadStatus

}