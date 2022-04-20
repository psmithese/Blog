package com.example.android.postlist.viewModel

import androidx.lifecycle.*
import com.example.android.postlist.model.Comment
import com.example.android.postlist.model.Post
import com.example.android.postlist.repository.IRepository
import com.example.android.postlist.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel(private val repository: IRepository) : ViewModel() {

    private var _postList = MutableLiveData<Resource<List<Post>>>()
    val postList: LiveData<Resource<List<Post>>> get() = _postList

    private var _commentList = MutableLiveData<Resource<List<Comment>>>()
    val commentList: LiveData<Resource<List<Comment>>> get() = _commentList

    private var _entireCommentList = MutableLiveData<Resource<List<Comment>>>()
    val entireCommentList: LiveData<Resource<List<Comment>>> get() = _entireCommentList

    init {
        getPosts()
        getAllComments()
    }

    /*Function to get post*/
    fun getPosts() {
        viewModelScope.launch {
            _postList.value = Resource.Loading
            val response = repository.getPosts()
            response.collect {
                _postList.value = it
            }
        }
    }

    /*Function to get All Comments*/
    private fun getAllComments() {
        viewModelScope.launch {
            _entireCommentList.postValue(Resource.Loading)
            val response = repository.getAllComments()
            response.collect {
                _entireCommentList.value = it
            }
        }
    }

    /*Function to get Comments*/
    fun getComments(postId: Int) {
        viewModelScope.launch {
            _commentList.postValue(Resource.Loading)
            val response = repository.getComments(postId)
            response.collect {
                _commentList.value = it
            }
        }
    }

    /*Function to add Comments*/
    fun pushComment(comment: Comment) {
        viewModelScope.launch {
            Resource.Loading
            repository.pushComment(comment)
        }
    }

    /*Function to add Posts*/
    fun addPost(post: Post) {
        viewModelScope.launch {
            repository.addPost(post)
        }
    }

    /*Search Posts*/
    private var cachedPostList = MutableLiveData<Resource<List<Post>>>()
    private var isSearchStarting = true
    var isSearching = MutableStateFlow(false)

    fun searchPostList(query: String) {

        if (isSearchStarting) {
            cachedPostList.value = _postList.value
            isSearchStarting = false
        }

        val listToSearch = if (isSearchStarting) {
            postList.value
        } else {
            cachedPostList.value
        }

        viewModelScope.launch {
            if (query.isEmpty()) {
                _postList.value = cachedPostList.value
                isSearching.value = false
                isSearchStarting = true
                return@launch
            } else {
                val results = listToSearch?.data?.filter {
                    it.title.contains(query.trim(), ignoreCase = true) ||
                            it.id.toString().contains(query.trim())
                }
                results?.let {
                    _postList.value = Resource.Success(results)
                }
            }

            if (isSearchStarting) {
                cachedPostList.value = _postList.value
                isSearchStarting = false
            }

            isSearching.value = true
        }
    }

}