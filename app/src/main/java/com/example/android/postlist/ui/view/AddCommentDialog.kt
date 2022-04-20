package com.example.android.postlist.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.postlist.databinding.ActivityAddCommentDialogBinding
import com.example.android.postlist.model.Comment
import com.example.android.postlist.model.Post
import com.example.android.postlist.repository.Repository
import com.example.android.postlist.room.CachedCommentMapper
import com.example.android.postlist.room.CachedPostMapper
import com.example.android.postlist.room.LocalDataBase
import com.example.android.postlist.viewModel.MainViewModel
import com.example.android.postlist.viewModel.MainViewModelFactory

class AddCommentDialog(private val post: Post) : DialogFragment() {
    private var _binding: ActivityAddCommentDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var repository: Repository
    private lateinit var viewModel: MainViewModel
    private lateinit var commentMapper: CachedCommentMapper
    private lateinit var postMapper: CachedPostMapper
    private lateinit var viewModelFactory: MainViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = ActivityAddCommentDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        /*Initialise ViewModel*/
        val roomDatabase = LocalDataBase.getInstance(requireContext())
        commentMapper = CachedCommentMapper()
        postMapper = CachedPostMapper()
        repository = Repository(roomDatabase, commentMapper, postMapper)
        viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        binding.btComment.setOnClickListener {
            val name = binding.commenterName.text.toString()
            val email = binding.emailAddress.text.toString()
            val comments = binding.newComment.text.toString()
            val id = 0
            val postId = post.id


            when {
                name.isEmpty() -> {
                    binding.commenterName.error = "Name Can't Be Empty"
                    return@setOnClickListener
                }
                email.isEmpty() -> {
                    binding.emailAddress.error = "Email Can't Be Empty"
                    return@setOnClickListener
                }
                comments.isEmpty() -> {
                    binding.newComment.error = "Comment Can't Be Empty"
                    return@setOnClickListener
                }
                else -> {
                    val newComment = Comment(comments, email, id, name, postId)
                    viewModel.pushComment(newComment)
                    dismiss()
                }
            }
        }
    }
}