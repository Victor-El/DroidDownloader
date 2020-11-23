package me.codeenzyme.droiddownloader.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.codeenzyme.droiddownloader.core.DroidDownloaderService
import me.codeenzyme.droiddownloader.utils.Utils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit


class DefaultDroidDownloader private constructor(
    private val connectTimeOutMillis: Long,
    private val writeTimeOutMillis: Long,
    private val readTimeOutMillis: Long
) : DroidDownloader {
    private val okHttpClient: OkHttpClient = if (readTimeOutMillis != 0L) OkHttpClient.Builder()
        .writeTimeout(writeTimeOutMillis, TimeUnit.MILLISECONDS)
        .readTimeout(readTimeOutMillis, TimeUnit.MILLISECONDS)
        .connectTimeout(connectTimeOutMillis, TimeUnit.MILLISECONDS)
        .build() else OkHttpClient.Builder()
        .writeTimeout(writeTimeOutMillis, TimeUnit.MILLISECONDS)
        .connectTimeout(connectTimeOutMillis, TimeUnit.MILLISECONDS)
        .build()


    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        // Retrofit2 requires base URL; setting dummy url
        .baseUrl("http://localhost:8080/")
        .build()

    private val droidDownloaderService = retrofit.create(DroidDownloaderService::class.java)

    override suspend fun downloadFile(url: String, dir: String, filename: String): DownloadStatus =
        withContext(Dispatchers.IO) {
            val resBody = try {
                droidDownloaderService.downloadFile(url)
            } catch (e: Exception) {
                return@withContext DownloadStatus.Failure.NetworkFailure(e.localizedMessage!!)
            }
            val success = Utils.writeResponseBodyToDisk(resBody, dir, filename)
            if (!success) {
                return@withContext DownloadStatus.Failure.DiskFailure("Disk Failure")
            }
            return@withContext DownloadStatus.Success(dir + File.separator + filename)
        }

    override suspend fun downloadFileWithBearerAuth(url: String, dir: String, filename: String, auth: String): DownloadStatus =
        withContext(Dispatchers.IO) {
            val resBody = try {
                droidDownloaderService.downloadFileWithBearerAuth(url, auth)
            } catch (e: Exception) {
                return@withContext DownloadStatus.Failure.NetworkFailure(e.localizedMessage!!)
            }
            val success = Utils.writeResponseBodyToDisk(resBody, dir, filename)
            if (!success) {
                return@withContext DownloadStatus.Failure.DiskFailure("Disk Failure")
            }
            return@withContext DownloadStatus.Success(dir + File.separator + filename)
        }


    class Builder {
        private var readTimeOutMillis: Long = 0
        private var writeTimeOutMillis = 30_000L
        private var connectTimeOutMillis = 30_000L

        fun addReadTimeOutMillis(readTimeOut: Long): Builder {
            readTimeOutMillis = readTimeOut
            return this
        }

        fun addWriteTimeOutMillis(writeTimeOut: Long): Builder {
            writeTimeOutMillis = writeTimeOut
            return this
        }

        fun addConnectTimeOutMillis(connectTimeOut: Long): Builder {
            connectTimeOutMillis = connectTimeOut
            return this
        }

        fun build(): DefaultDroidDownloader {
            return DefaultDroidDownloader(
                connectTimeOutMillis,
                writeTimeOutMillis,
                readTimeOutMillis
            )
        }
    }
}