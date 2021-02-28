package com.example.githubrepositories

import com.example.githubrepositories.coroutines.CoroutinesDispatcherProvider
import com.example.githubrepositories.data.GetRepositoriesResponse
import com.example.githubrepositories.network.ApiResponse
import com.example.githubrepositories.network.GithubApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetRepositoriesUseCase @Inject constructor(
    private val githubApi: GithubApi,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) {
    suspend fun getRepositories(): ApiResponse<List<GetRepositoriesResponse>> {
        return withContext(dispatcherProvider.io) {
            try {
                val response = githubApi.getRepositories()
                if (response.isSuccessful && response.body() != null) {
                    return@withContext ApiResponse.Success(response.body()!!)
                } else {
                    return@withContext ApiResponse.Failure
                }
            } catch (t: Throwable) {
                if (t !is CancellationException) {
                    return@withContext ApiResponse.Failure
                } else {
                    throw t
                }
            }
        }
    }
}