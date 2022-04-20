package com.example.android.postlist.repository

import com.example.android.postlist.model.Comment
import com.example.android.postlist.model.Post
import com.example.android.postlist.util.Resource
import kotlinx.coroutines.flow.Flow

interface IRepository {

    suspend fun getPosts(): Flow<Resource<List<Post>>>

    suspend fun getComments(postId: Int): Flow<Resource<List<Comment>>>

    suspend fun getAllComments(): Flow<Resource<List<Comment>>>

    suspend fun pushComment(comment: Comment)

    suspend fun addPost(post: Post)
}