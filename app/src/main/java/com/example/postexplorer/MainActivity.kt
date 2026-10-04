package com.example.postexplorer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.example.postexplorer.network.RetrofitInstance
import com.example.postexplorer.repository.PostRepository
import com.example.postexplorer.ui.PostDetailScreen
import com.example.postexplorer.ui.PostListScreen
import com.example.postexplorer.viewmodel.PostViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val repository = PostRepository(RetrofitInstance.api)
        val viewModel = PostViewModel(repository)

        setContent {
            MaterialTheme {
                var selectedPostId by remember { mutableStateOf<Int?>(null) }

                val selectedPost = selectedPostId?.let { viewModel.findPostById(it) }

                if (selectedPostId == null) {
                    PostListScreen(
                        viewModel = viewModel,
                        onPostClick = { id -> selectedPostId = id }
                    )
                } else {
                    PostDetailScreen(
                        post = selectedPost,
                        onBack = { selectedPostId = null }
                    )
                }
            }
        }
    }
}