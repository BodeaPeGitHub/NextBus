package com.dash.nextbus.service

import com.dash.nextbus.model.Agency
import com.dash.nextbus.model.Stop
import com.dash.nextbus.model.StopTime
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
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
        suspend fun getStopTimes(@Header("X-Agency-Id") agencyId: Int): List<StopTime>
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

    private val gson = GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val api: TranzyApiService = retrofit.create(TranzyApiService::class.java)

}