package com.example.githubrepositories

import com.example.githubrepositories.coroutines.CoroutinesDispatcherProvider
import com.example.githubrepositories.data.Contributor
import com.example.githubrepositories.data.ReposListContent
import com.example.githubrepositories.data.Repository
import com.example.githubrepositories.network.ApiResponse
import com.example.githubrepositories.network.GithubApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Most of the application business logic should be encapsulated in
 * compact UseCases/Interactors.
 */
class GetRepositoriesUseCase @Inject constructor(
    private val githubApi: GithubApi,
    private val dispatcherProvider: CoroutinesDispatcherProvider
) {
    suspend fun getRepositories(pageNumber: Int): ApiResponse<List<ReposListContent>> {
        return withContext(dispatcherProvider.io) {
            try {
                val response = githubApi.getRepositories(page = pageNumber)
                if (response.isSuccessful && response.body() != null) {
                    val contributorsList = getContributors(response.body()!!)
                    val listContent = response.body()!!.mapIndexed { index, repository ->
                        ReposListContent(
                            repository,
                            contributorsList[index].maxByOrNull { it.contributions }
                        )
                    }
                    return@withContext ApiResponse.Success(listContent)
                } else {
                    // Not doing full error handling here and just returning the Failure object
                    // In production app I'll consider all the scenarios and make sure all of them are handled properly.
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

    private suspend fun getContributors(repositories: List<Repository>): List<List<Contributor>> {
        return withContext(dispatcherProvider.io) {

            val contributorsResponse = repositories.map {
                async { githubApi.getRepositoryContributors(repo = it.name) }
            }.awaitAll()

            return@withContext contributorsResponse.map { response ->
                if (response.isSuccessful && response.body() != null) {
                    response.body()!!
                } else {
                    emptyList()
                }
            }
        }
    }
}