package com.gokul.sample.data.api

import com.gokul.sample.data.model.ListModel
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiPlaceHolder {

    @GET("users")
    suspend fun getUserList(/*@HeaderMap headers: Map<String, String>, */@Query("page") page :String?): Response<ListModel>

    @POST("users")
    suspend fun createUser(@Body data: RequestBody): Response<String>
}