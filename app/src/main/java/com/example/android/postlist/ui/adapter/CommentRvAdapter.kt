package com.example.android.postlist.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import com.example.android.postlist.R
import com.example.android.postlist.model.Comment

class CommentRvAdapter() :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Comment>() {

        override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.comment_list_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CommentViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Comment>) {
        differ.submitList(list)
    }

    class CommentViewHolder
    constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Comment) = with(itemView) {
            val commentName = itemView.findViewById<TextView>(R.id.commentName)
            val commentEmail = itemView.findViewById<TextView>(R.id.commentEmail)
            val commentBody= itemView.findViewById<TextView>(R.id.commentBody)
            val commentId= itemView.findViewById<TextView>(R.id.commentId)

            commentName.text = item.name
            commentEmail.text = item.email
            commentBody.text = item.body
//            commentId.text = item.id.toString()
        }
    }

}