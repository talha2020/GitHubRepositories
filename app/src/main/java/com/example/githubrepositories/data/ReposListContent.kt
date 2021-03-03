package com.example.githubrepositories.data

data class ReposListContent(
    val repository: Repository,
    val mostActiveContributor: Contributor?
)