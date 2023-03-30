package com.example.mygithubrepo.data.di



import android.app.Application
import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.mygithubrepo.data.datastore.network.api.GitHubAPIService
import com.example.mygithubrepo.data.datastore.network.cons.MyCons
import com.example.mygithubrepo.data.repository.RepoRepositoryImpl
import com.example.mygithubrepo.domain.datasource.RepoDataSource
import com.example.mygithubrepo.domain.datasource.ReposRemoteDatasource
import com.example.mygithubrepo.domain.repository.RepoRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
 class DataModule {


    @Provides
    fun provideHttpLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    @Provides
    @Singleton
    fun provideOkhttpClient(
        context: Context
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(
            ChuckerInterceptor.Builder(context)
                .collector(ChuckerCollector(context))
                .maxContentLength(250000L)
                .redactHeaders(emptySet())
                .alwaysReadResponseBody(false)
                .build()
        )
        return client.build()
    }


    @Singleton
    @Provides
    fun provideUserRemoteDataSource(
        gitHubAPIService: GitHubAPIService
    ): RepoDataSource {
        return ReposRemoteDatasource(gitHubAPIService)
    }

    @Singleton
    @Provides
    fun provideIoDispatcher() = Dispatchers.IO

    @Singleton
    @Provides
    fun provideUserRepository(
        remoteDataSource: RepoDataSource,
        ioDispatcher: CoroutineDispatcher
    ): RepoRepository {
        return RepoRepositoryImpl(remoteDataSource, ioDispatcher)
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(MyCons.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    @Singleton
    fun provideApiService(
        retrofit: Retrofit
    ): GitHubAPIService {
        return retrofit.create(GitHubAPIService::class.java)
    }

}

