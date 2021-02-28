package com.example.githubrepositories.network

import com.example.githubrepositories.Constants
import com.example.githubrepositories.data.Repository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApi {

    @GET(Constants.BASE_URL + "orgs/{org}/repos")
    suspend fun getRepositories(
        @Path("org") org: String = "Vincit",
    ): Response<List<Repository>>

}