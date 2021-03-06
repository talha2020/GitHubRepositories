package com.example.githubrepositories.network

import com.example.githubrepositories.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class AuthInterceptor : Interceptor {

    /*
    Due to rate limiting, added this authorization header. More info here:
    https://docs.github.com/en/rest/overview/resources-in-the-rest-api#rate-limiting
     */
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authRequest = request.newBuilder()
            .header("Authorization", "token ${BuildConfig.GITHUB_PERSONAL_TOKEN}").build()
        return chain.proceed(authRequest)
    }

}