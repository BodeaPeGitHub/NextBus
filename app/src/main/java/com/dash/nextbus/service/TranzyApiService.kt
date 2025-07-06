package com.dash.nextbus.service

import android.util.Log
import com.dash.nextbus.model.Agency
import com.dash.nextbus.model.Stop
import com.dash.nextbus.model.StopTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface TranzyApiService {
    @GET("opendata/agency")
    suspend fun getAgencies(): List<Agency>

    @GET("opendata/stops")
    suspend fun getStops(@Header("X-Agency-Id") agencyId: Int): List<Stop>

    @GET("opendata/stop_times")
    suspend fun getStopTimes(@Header("X-Agency-Id") agencyId: String): List<StopTime>
}

object RetrofitClient {

    private const val BASE_URL = "https://api.tranzy.ai/v1/"
    private const val API_KEY = "implXunwGcd6uzb1BTmYKJAMZ7pyuOmdDiy1A2iA"

    private val headerInterceptor = Interceptor { chain ->
        val original: Request = chain.request()
        val requestWithHeaders = original.newBuilder()
            .header("Accept", "application/json")
            .header("X-API-KEY", API_KEY)
            .build()
        chain.proceed(requestWithHeaders)
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: TranzyApiService = retrofit.create(TranzyApiService::class.java)

}