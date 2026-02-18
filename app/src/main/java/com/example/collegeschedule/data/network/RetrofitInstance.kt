package com.example.collegeschedule.data.network

import com.example.collegeschedule.data.api.ScheduleApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(AcceptJsonInterceptor())
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5170/")
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val api: ScheduleApi = retrofit.create(ScheduleApi::class.java)

    private class AcceptJsonInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request().newBuilder()
                .addHeader("Accept", "application/json")
                .build()
            return chain.proceed(request)
        }
    }
}