package com.example.postexplorer.network
import com.example.postexplorer.model.Post
import retrofit2.http.GET
interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}