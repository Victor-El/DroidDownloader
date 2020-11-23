package me.codeenzyme.droiddownloader.api

sealed class DownloadStatus {
    data class Success(val filePath: String): DownloadStatus()
    sealed class Failure(): DownloadStatus() {
        data class NetworkFailure(val message: String): DownloadStatus()
        data class DiskFailure(val message: String): DownloadStatus()
    }
}