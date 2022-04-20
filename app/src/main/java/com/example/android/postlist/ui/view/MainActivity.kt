package com.example.android.postlist.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.postlist.R
import com.example.android.postlist.databinding.ActivityMainBinding
import com.example.android.postlist.model.Post
import com.example.android.postlist.repository.Repository
import com.example.android.postlist.room.CachedCommentMapper
import com.example.android.postlist.room.CachedPostMapper
import com.example.android.postlist.room.LocalDataBase
import com.example.android.postlist.ui.adapter.PostRvAdapter
import com.example.android.postlist.util.LocalListUtil.getPostList
import com.example.android.postlist.util.Resource
import com.example.android.postlist.viewModel.MainViewModel
import com.example.android.postlist.viewModel.MainViewModelFactory
import java.util.*

class MainActivity : AppCompatActivity(), PostRvAdapter.Interaction {
    private lateinit var binding: ActivityMainBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var repository: Repository
    private lateinit var commentMapper: CachedCommentMapper
    private lateinit var postMapper: CachedPostMapper
    private lateinit var viewModel: MainViewModel
    private lateinit var viewModelFactory: MainViewModelFactory
    private lateinit var postRvAdapter: PostRvAdapter

    private var localPostList = getPostList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*Set Status bar Color*/
        window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window?.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window?.statusBarColor = resources?.getColor(R.color.backgroundSecond)!!

        /*Initialise ViewModel*/
        val roomDatabase = LocalDataBase.getInstance(this)
        commentMapper = CachedCommentMapper()
        postMapper = CachedPostMapper()
        repository = Repository(roomDatabase, commentMapper, postMapper)
        viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        progressBar = binding.progressBar

        /*Initialise RecyclerView*/
        setupRecyclerView()
        loadPage()

        /*Set-up Search functionality*/
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.searchPostList(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { viewModel.searchPostList(it) }
                return false
            }
        })

        /*Add New Comment*/
        binding.fabPost.setOnClickListener {
            AddPostDialog().show(supportFragmentManager, "D")
        }

        /*Set-up Rv Swipe to Refresh*/
        binding.swipeRefresh.setOnRefreshListener {
            loadPage()
            binding.swipeRefresh.isRefreshing = false
        }

    }

    /*Initialise RecyclerView*/
    private fun setupRecyclerView() {
        binding.rvPost.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            postRvAdapter = PostRvAdapter(this@MainActivity)
            adapter = postRvAdapter
        }

        /*Scroll to Position of New Post*/
        postRvAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver(){
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.rvPost.scrollToPosition(positionStart)
            }
        })
    }

    private fun loadPage() {
        viewModel.postList.observe(this, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    response.data?.let {
                        postRvAdapter.submitList(it)
                        localPostList = it as MutableList<Post>
                    }
                    hideProgressBar()
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
                is Resource.Error -> {
                    hideProgressBar()
                    Toast.makeText(this, "Error: Occurred", Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun onItemSelected(position: Int, item: Post) {
        val currentPost = localPostList[position]

        val intent = Intent(this, CommentActivity::class.java).apply {
            putExtra(POST, currentPost)
        }
        startActivity(intent)
    }

    companion object {
        const val POST = "post"
    }
}