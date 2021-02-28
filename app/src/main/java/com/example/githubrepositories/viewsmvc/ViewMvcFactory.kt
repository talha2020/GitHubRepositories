package com.example.githubrepositories.viewsmvc

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.githubrepositories.ImageLoader
import com.example.githubrepositories.RepositoriesListViewMvc
import javax.inject.Inject
import javax.inject.Provider

class ViewMvcFactory @Inject constructor(
    private val layoutInflaterProvider: Provider<LayoutInflater>,
    private val imageLoaderProvider: Provider<ImageLoader>
) {

    fun newRepositoriesListViewMvc(parent: ViewGroup?): RepositoriesListViewMvc {
        return RepositoriesListViewMvc(
            layoutInflaterProvider.get(),
            imageLoaderProvider.get(),
            parent
        )
    }

}