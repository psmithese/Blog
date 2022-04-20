package com.example.android.postlist.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class  Comment(
    val body: String,
    val email: String,
    var id: Int,
    val name: String,
    var postId: Int
) : Parcelable