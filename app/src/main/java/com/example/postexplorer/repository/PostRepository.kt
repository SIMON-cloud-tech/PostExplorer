package com.example.postexplorer.repository
import com.example.postexplorer.model.Post
import com.example.postexplorer.network.ApiService
class PostRepository(private val api: ApiService) {
    suspend fun fetchPosts(): List<Post> = api.getPosts()
}