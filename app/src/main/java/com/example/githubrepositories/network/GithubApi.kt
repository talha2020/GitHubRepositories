package com.example.githubrepositories.network

import com.example.githubrepositories.Constants
import com.example.githubrepositories.data.Contributor
import com.example.githubrepositories.data.Repository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    /* sortOrder should take an enum here so that we don't pass on any unsupported String,
    leaving it as is for now
    */
    @GET(Constants.BASE_URL + "orgs/{org}/repos")
    suspend fun getRepositories(
        @Path("org") org: String = "Vincit",
        @Query("sort") sortOrder: String = "updated",
        @Query("per_page") count: Int = 15,
        @Query("page") page: Int
    ): Response<List<Repository>>


    @GET(Constants.BASE_URL + "repos/{owner}/{repo}/contributors")
    suspend fun getRepositoryContributors(
        @Path("owner") owner: String = "Vincit",
        @Path("repo") repo: String
    ): Response<List<Contributor>>

}

