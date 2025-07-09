package com.example.finelytics.data.remote


import okhttp3.ResponseBody
import retrofit2.http.Query
import retrofit2.http.GET

interface StockApi {
    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apiKey") apiKey:String = API_KEY
    ): ResponseBody

    companion object{
        const val API_KEY = "Q63Y9NX3TUF587NF"
        const val BASE_URL = "https://alphavantage.co"
    }
}