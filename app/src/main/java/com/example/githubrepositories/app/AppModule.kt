package com.example.githubrepositories.app

import android.app.Application
import com.example.githubrepositories.Constants
import com.example.githubrepositories.network.GithubApi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
class AppModule(private val application: Application) {

    @Provides
    @AppScope
    fun retrofit(): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun application() = application

    @Provides
    @AppScope
    fun githubApi(retrofit: Retrofit) = retrofit.create(GithubApi::class.java)

}