package com.roman.lab4.database

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CountryApiService {
    @GET("country?region=AFR&format=json")
    fun getCountries(
        @Query("page") page: Int = 1
    ): Call<ArrayList<Any>>
}