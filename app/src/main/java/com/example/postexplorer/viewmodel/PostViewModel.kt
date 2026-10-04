package com.example.postexplorer.viewmodel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.postexplorer.model.Post
import com.example.postexplorer.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
sealed class PostsUiState {
    object Loading : PostsUiState()
    data class Success(val posts: List<Post>) : PostsUiState()
    data class Error(val message: String) : PostsUiState()
}
class PostViewModel(private val repository: PostRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<PostsUiState>(PostsUiState.Loading)
    val uiState: StateFlow<PostsUiState> = _uiState.asStateFlow()
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
}