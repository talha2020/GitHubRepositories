package com.example.githubrepositories.network

import com.example.githubrepositories.Constants
import com.example.githubrepositories.data.Contributor
import com.example.githubrepositories.data.Repository
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET(Constants.BASE_URL + "orgs/{org}/repos")
    suspend fun getRepositories(
        @Path("org") org: String = "Vincit",
        @Query("sort") sortOrder: String = "updated",
        @Query("per_page") count: Int = 15,
        @Query("page") page: Int
    ): Response<List<Repository>>


    @GET(Constants.BASE_URL + "repos/{owner}/{repo}/contributors")
    suspend fun getRepositoryContributers(
        @Path("owner") owner: String = "Vincit",
        @Path("repo") repo: String
    ): Response<List<Contributor>>

    // Rate limits: https://docs.github.com/en/rest/overview/resources-in-the-rest-api#rate-limiting
}

//TODO: sort order should be an enum