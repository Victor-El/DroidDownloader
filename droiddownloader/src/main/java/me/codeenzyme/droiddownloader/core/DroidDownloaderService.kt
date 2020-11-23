package me.codeenzyme.droiddownloader.core

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DroidDownloaderService {

    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody

    @Streaming
    @GET
    suspend fun downloadFileWithBearerAuth(@Url url: String, @Header("Authorization") auth: String): ResponseBody
}