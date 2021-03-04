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
    private var isLoading = false
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
        if (!isLoading && !isLastPage && !networkJob.isActive) {
            networkJob = coroutineScope.launch {
                isLoading = true
                viewMvc.showProgressIndication()
                try {
                    when (val result = getRepositoriesUseCase.getRepositories(pageNumber)) {
                        is ApiResponse.Success -> {
                            ++pageNumber
                            viewMvc.bindRepositories(result.data)
                            if (result.data.isNullOrEmpty()) {
                                isLastPage = true
                            }
                        }
                        is ApiResponse.Failure -> onFetchFailed()
                    }
                } finally {
                    viewMvc.hideProgressIndication()
                    isLoading = false
                }

            }
        }
    }

    private fun onFetchFailed() {
        showMessage(getString(R.string.network_error))
    }

    override fun onRepositoryClicked(clickedItem: ReposListContent) {
        showMessage("${clickedItem.repository.name} clicked")
    }

    override fun loadMoreItems() {
        fetchRepositories()
    }

}