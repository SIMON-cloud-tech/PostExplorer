package com.example.postexplorer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postexplorer.model.Post
import com.example.postexplorer.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.launch

sealed class PostsUiState {
    object Loading : PostsUiState()
    data class Success(val posts: List<Post>) : PostsUiState()
    data class Error(val message: String) : PostsUiState()
}

class PostViewModel(private val repository: PostRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<PostsUiState>(PostsUiState.Loading)
    val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val filteredPosts: StateFlow<List<Post>> = _uiState
        .map { state ->
            val all = (state as? PostsUiState.Success)?.posts ?: emptyList()
            val q = _searchQuery.value.trim().lowercase()
            if (q.isEmpty()) all
            else all.filter {
                it.title.lowercase().contains(q) || it.body.lowercase().contains(q)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init { loadPosts() }

    fun loadPosts() {
        viewModelScope.launch {
            _uiState.value = PostsUiState.Loading
            try {
                _uiState.value = PostsUiState.Success(repository.fetchPosts())
            } catch (e: Exception) {
                _uiState.value = PostsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun setSearchQuery(q: String) { _searchQuery.value = q }

    fun findPostById(id: Int): Post? =
        (_uiState.value as? PostsUiState.Success)?.posts?.firstOrNull { it.id == id }
}