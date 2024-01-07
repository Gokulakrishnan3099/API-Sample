package com.gokul.sample.data.repository

import com.gokul.sample.data.api.ApiPlaceHolder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class MainRepository {

    private val baseUrl = "https://reqres.in/api/"

    private val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val okHttpClient: OkHttpClient = OkHttpClient
        .Builder()
        .connectTimeout(30000, TimeUnit.MINUTES)
        .readTimeout(30000, TimeUnit.MINUTES)
        .writeTimeout(30000, TimeUnit.MINUTES)
        .addInterceptor(interceptor)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    public val apiPlaceHolder: ApiPlaceHolder = retrofit.create(ApiPlaceHolder::class.java)
}