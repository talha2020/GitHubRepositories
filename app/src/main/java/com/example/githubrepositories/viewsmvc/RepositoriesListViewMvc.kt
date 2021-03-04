package com.example.githubrepositories.viewsmvc

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubrepositories.ImageLoader
import com.example.githubrepositories.R
import com.example.githubrepositories.data.ReposListContent
import com.example.githubrepositories.setGone
import com.example.githubrepositories.show

class RepositoriesListViewMvc(
    layoutInflater: LayoutInflater,
    imageLoader: ImageLoader,
    parent: ViewGroup?
) : BaseViewMvc<RepositoriesListViewMvc.Listener>(
    layoutInflater,
    parent,
    R.layout.activity_main
) {
    private val layoutManager = LinearLayoutManager(context)

    interface Listener {
        fun onRepositoryClicked(clickedRepo: ReposListContent)
        fun loadMoreItems()
    }

    private val progressBar: ProgressBar
    private val repositoriesRv: RecyclerView
    private val repositoriesAdapter: RepositoriesAdapter

    init {
        progressBar = findViewById(R.id.progressBar)
        repositoriesRv = findViewById(R.id.repositoriesRv)

        // init recycler view
        repositoriesRv.layoutManager = layoutManager
        repositoriesAdapter = RepositoriesAdapter(context, imageLoader) { clickedQuestion ->
            for (listener in listeners) {
                listener.onRepositoryClicked(clickedQuestion)
            }
        }
        repositoriesRv.adapter = repositoriesAdapter

        repositoriesRv.addOnScrollListener(object : PaginationListener(layoutManager) {
            override fun loadMoreItems() {
                for (listener in listeners) {
                    listener.loadMoreItems()
                }
            }
        })
    }

    fun bindRepositories(questions: List<ReposListContent>) {
        repositoriesAdapter.bindData(questions)
    }

    fun showProgressIndication() {
        progressBar.show()
    }

    fun hideProgressIndication() {
        progressBar.setGone()
    }

    class RepositoriesAdapter(
        private val context: Context,
        private val imageLoader: ImageLoader,
        private val onRepositoryClickListener: (ReposListContent) -> Unit
    ) : RecyclerView.Adapter<RepositoriesAdapter.QuestionViewHolder>() {

        private var repositoryList: MutableList<ReposListContent> = mutableListOf()

        inner class QuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ownerAvatar: ImageView = view.findViewById(R.id.ownerAvatar)
            val name: TextView = view.findViewById(R.id.name)
            val description: TextView = view.findViewById(R.id.description)
            val language: TextView = view.findViewById(R.id.language)
            val mostActiveContributor: TextView = view.findViewById(R.id.mostActiveContributor)
        }

        fun bindData(questions: List<ReposListContent>) {
            val previousSize = repositoryList.size
            repositoryList.addAll(questions)
            notifyItemRangeInserted(previousSize, repositoryList.size - previousSize)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.repository_list_item, parent, false)
            return QuestionViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
            val item = repositoryList[position]
            holder.ownerAvatar
            holder.name.text = item.repository.name
            holder.description.text = item.repository.description
            holder.language.text = context.getString(R.string.language, item.repository.language)

            item.mostActiveContributor?.let {
                holder.mostActiveContributor.text =
                    context.getString(R.string.contributor, it.login)
                imageLoader.loadImage(it.avatarUrl, holder.ownerAvatar)
            }

            holder.itemView.setOnClickListener {
                onRepositoryClickListener.invoke(repositoryList[position])
            }
        }

        override fun getItemCount(): Int {
            return repositoryList.size
        }

    }

}