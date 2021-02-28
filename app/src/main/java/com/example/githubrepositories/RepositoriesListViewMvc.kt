package com.example.githubrepositories

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubrepositories.data.Repository
import com.example.githubrepositories.viewsmvc.BaseViewMvc

class RepositoriesListViewMvc(
    layoutInflater: LayoutInflater,
    private val imageLoader: ImageLoader,
    parent: ViewGroup?
) : BaseViewMvc<RepositoriesListViewMvc.Listener>(
    layoutInflater,
    parent,
    R.layout.activity_main
) {

    interface Listener {
        fun onRepositoryClicked(clickedRepo: Repository)
    }

    private val progressBar: ProgressBar
    private val repositoriesRv: RecyclerView
    private val repositoriesAdapter: RepositoriesAdapter

    init {
        progressBar = findViewById(R.id.progressBar)
        repositoriesRv = findViewById(R.id.repositoriesRv)

        // init recycler view
        repositoriesRv.layoutManager = LinearLayoutManager(context)
        repositoriesAdapter = RepositoriesAdapter(imageLoader) { clickedQuestion ->
            for (listener in listeners) {
                listener.onRepositoryClicked(clickedQuestion)
            }
        }
        repositoriesRv.adapter = repositoriesAdapter

    }

    fun bindRepositories(questions: List<Repository>) {
        repositoriesAdapter.bindData(questions)
    }

    fun showProgressIndication() {
        progressBar.show()
    }

    fun hideProgressIndication() {
        progressBar.setGone()
    }

    class RepositoriesAdapter(
        private val imageLoader: ImageLoader,
        private val onRepositoryClickListener: (Repository) -> Unit
    ) : RecyclerView.Adapter<RepositoriesAdapter.QuestionViewHolder>() {

        private var repositoryList: List<Repository> = ArrayList(0)

        inner class QuestionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val ownerAvatar: ImageView = view.findViewById(R.id.ownerAvatar)
            val name: TextView = view.findViewById(R.id.name)
            val description: TextView = view.findViewById(R.id.description)
            val language: TextView = view.findViewById(R.id.language)
        }

        fun bindData(questions: List<Repository>) {
            repositoryList = ArrayList(questions)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.repository_list_item, parent, false)
            return QuestionViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: QuestionViewHolder, position: Int) {
            val item = repositoryList[position]
            holder.ownerAvatar
            holder.name.text = item.name
            holder.description.text = item.description
            holder.language.text = item.language

            imageLoader.loadImage(item.owner.avatarUrl, holder.ownerAvatar)

            holder.itemView.setOnClickListener {
                onRepositoryClickListener.invoke(repositoryList[position])
            }
        }

        override fun getItemCount(): Int {
            return repositoryList.size
        }

    }
}