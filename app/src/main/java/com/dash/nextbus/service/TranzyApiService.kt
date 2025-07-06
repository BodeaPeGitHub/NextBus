package com.dash.nextbus.service

import com.dash.nextbus.model.Agency
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface TranzyApiService {
    @GET("opendata/agency")
    suspend fun getAgencies(): List<Agency>
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

    suspend fun canReachGoogle(): Boolean {
        return try {
            val testClient = OkHttpClient()
            val request = Request.Builder()
                .url("https://clients3.google.com/generate_204")
                .build()

            val response: Response = testClient.newCall(request).execute()
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}