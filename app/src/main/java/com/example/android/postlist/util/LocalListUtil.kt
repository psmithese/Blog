package com.example.android.postlist.util

import com.example.android.postlist.model.Comment
import com.example.android.postlist.model.Post

object LocalListUtil {
    private var localPostList: MutableList<Post> = ArrayList()
    private var localCommentList: MutableList<Comment> = ArrayList()

    fun addComment(item: Comment) {
        localCommentList.add(item)
    }

    fun getPostList(): List<Post> {
        return localPostList
    }

    fun getCommentList(): List<Comment> {
        return localCommentList
    }

    fun getCommentListCount(): Int {
        return  localCommentList.size
    }
}