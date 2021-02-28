package com.example.githubrepositories.data

import com.google.gson.annotations.SerializedName

data class GetRepositoriesResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("node_id") val nodeId: String
)