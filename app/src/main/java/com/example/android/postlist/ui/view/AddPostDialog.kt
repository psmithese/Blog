package com.example.android.postlist.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.postlist.databinding.FragmentAddPostDialogBinding
import com.example.android.postlist.model.Post
import com.example.android.postlist.repository.Repository
import com.example.android.postlist.room.CachedCommentMapper
import com.example.android.postlist.room.CachedPostMapper
import com.example.android.postlist.room.LocalDataBase
import com.example.android.postlist.viewModel.MainViewModel
import com.example.android.postlist.viewModel.MainViewModelFactory

class AddPostDialog : DialogFragment() {
    private var _binding: FragmentAddPostDialogBinding? = null
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddPostDialogBinding.inflate(inflater, container, false)
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
        viewModel =
            ViewModelProvider(requireActivity(), viewModelFactory).get(MainViewModel::class.java)

        binding.btPost.setOnClickListener {
            val postTitle = binding.newPostTitle.text.toString()
            val postBody = binding.newPostBody.text.toString()
            val userId = (1..10).random()
            val id = 0

            when {
                postTitle.isEmpty() -> {
                    binding.newPostTitle.error = "Title Can't Be Empty"
                    return@setOnClickListener
                }
                postBody.isEmpty() -> {
                    binding.newPostBody.error = "Post Can't Be Empty"
                    return@setOnClickListener
                }
                else -> {
                    val newPost = Post(userId, id, postTitle, postBody)
                    viewModel.addPost(newPost)
                    dismiss()
                }
            }
        }
    }
}