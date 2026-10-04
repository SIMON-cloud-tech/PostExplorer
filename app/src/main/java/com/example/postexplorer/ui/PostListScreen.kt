package com.example.postexplorer.ui

import androidx.compose.foundation.clickable
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
fun PostListScreen(
    viewModel: PostViewModel,
    onPostClick: (Int) -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    val query by viewModel.searchQuery.collectAsState()
    val filtered by viewModel.filteredPosts.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.setSearchQuery(it) },
            label = { Text("Search posts") },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        )

        when (val current = state) {
            is PostsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            is PostsUiState.Error -> {
                Text("Error: ${current.message}", modifier = Modifier.padding(12.dp))
            }
            is PostsUiState.Success -> {
                if (filtered.isEmpty()) {
                    Text("No posts match your search.", modifier = Modifier.padding(12.dp))
                } else {
                    LazyColumn {
                        items(filtered) { post ->
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onPostClick(post.id) }
                                    .padding(12.dp)
                            ) {
                                Text(post.title, style = MaterialTheme.typography.titleMedium)
                                Text(
                                    post.body.take(80) + if (post.body.length > 80) "..." else "",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}