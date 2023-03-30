package com.example.mygithubrepo.data.datastore.network.api


import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.mygithubrepo.BuildConfig
import com.example.mygithubrepo.data.dto.RemoteUserDetails
import com.example.mygithubrepo.data.dto.Repos
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query




interface GitHubAPIService {


    @GET("search/repositories")
    suspend fun getRepos(
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") itemsPerPage: Int
    ): Repos



    @GET("users/{user}")
    suspend fun getUserDetail(
        @Path("user") id: String
    ): RemoteUserDetails


}