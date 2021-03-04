package com.example.githubrepositories

import android.os.Bundle
import com.example.githubrepositories.coroutines.CoroutinesDispatcherProvider
import com.example.githubrepositories.data.ReposListContent
import com.example.githubrepositories.network.ApiResponse
import com.example.githubrepositories.viewsmvc.RepositoriesListViewMvc
import com.example.githubrepositories.viewsmvc.ViewMvcFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Decided to go with the classic MVC pattern. Anything related to the view is abstracted away and
 * is created through [ViewMvcFactory]. Activity and Fragment are considered controllers in this scenario
 * that takes the actions from the view and then contact the business logic layer through UseCases to perform actions
 * and also call certain view functions with data received from the business logic layer.
 *
 * This makes the testing of controller logic difficult. Ideally it should contain just a plumbing logic
 * between business layer and views and most of the logic should live in UseCases but even then we may
 * want to test that plumbing logic. In that case, controller logic can be abstracted out of the Activity
 * class as well which will make it easily testable. Decided to keep it straight forward here.
 */

class MainActivity : BaseActivity(), RepositoriesListViewMvc.Listener {

    @Inject
    lateinit var getRepositoriesUseCase: GetRepositoriesUseCase

    @Inject
    lateinit var dispatcherProvider: CoroutinesDispatcherProvider

    @Inject
    lateinit var viewMvcFactory: ViewMvcFactory

    private lateinit var viewMvc: RepositoriesListViewMvc

    private lateinit var coroutineScope: CoroutineScope
    private var networkJob: Job = Job().apply { cancel() }

    private var pageNumber = 1
    private var isLastPage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
        viewMvc = viewMvcFactory.newRepositoriesListViewMvc(null)
        setContentView(viewMvc.rootView)

        coroutineScope = CoroutineScope(dispatcherProvider.main)
    }

    override fun onStart() {
        super.onStart()
        viewMvc.registerListener(this)
        fetchRepositories()
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.coroutineContext.cancelChildren()
        viewMvc.unregisterListener(this)
    }

    private fun fetchRepositories() {
        if (!isLastPage && !networkJob.isActive) {
            networkJob = coroutineScope.launch {
                viewMvc.showProgressIndication()
                try {
                    when (val result = getRepositoriesUseCase.getRepositories(pageNumber)) {
                        is ApiResponse.Success -> {
                            ++pageNumber
                            viewMvc.bindRepositories(result.data)
                            // This logic can be moved inside usecase so it informs you if
                            // no more data is available, that will make controller logic simpler.
                            if (result.data.isNullOrEmpty()) {
                                isLastPage = true
                            }
                        }
                        is ApiResponse.Failure -> onFetchFailed()
                    }
                } finally {
                    viewMvc.hideProgressIndication()
                }
            }
        }
    }

    private fun onFetchFailed() {
        showMessage(getString(R.string.network_error))
    }

    override fun onRepositoryClicked(clickedRepo: ReposListContent) {
        showMessage("${clickedRepo.repository.name} clicked")
    }

    override fun loadMoreItems() {
        fetchRepositories()
    }

}