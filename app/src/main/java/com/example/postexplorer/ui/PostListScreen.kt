package com.example.postexplorer.ui
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.postexplorer.viewmodel.PostsUiState
import com.example.postexplorer.viewmodel.PostViewModel
@Composable
fun PostListScreen(viewModel: PostViewModel) {
    val state by viewModel.uiState.collectAsState()
    when (val current = state) {
        is PostsUiState.Loading -> CircularProgressIndicator()
        is PostsUiState.Error -> Text("Error: ${current.message}")
        is PostsUiState.Success -> {
            LazyColumn {
                items(current.posts) { post ->
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(post.title, style = MaterialTheme.typography.titleMedium)
                        Text(post.body)
                    }
                }
            }
        }
    }
}