package com.example.githubrepositories

import android.os.Bundle
import com.example.githubrepositories.coroutines.CoroutinesDispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : BaseActivity() {

    @Inject
    lateinit var getRepositoriesUseCase: GetRepositoriesUseCase

    @Inject
    lateinit var dispatcherProvider: CoroutinesDispatcherProvider

    private lateinit var coroutineScope: CoroutineScope

    override fun onCreate(savedInstanceState: Bundle?) {
        injector.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        coroutineScope = CoroutineScope(dispatcherProvider.main)

        coroutineScope.launch {
            getRepositoriesUseCase.getRepositories()
        }
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.coroutineContext.cancelChildren()
    }
}