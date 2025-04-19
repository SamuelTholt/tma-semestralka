package com.example.tma_semestralka.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tma_semestralka.databinding.PostItemBinding
import com.example.tma_semestralka.player.Player
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PostAdapter(private val listener: PostDetailsClickListener,
    private val isAdmin: Boolean)
    : ListAdapter<Post, PostAdapter.PostDetailsViewHolder>(DiffUtilCallback()) {

    inner class PostDetailsViewHolder(private val binding: PostItemBinding) :
         RecyclerView.ViewHolder(binding.root) {
        init {
            binding.editBtn.setOnClickListener {
                listener.onEditPostClick(getItem(adapterPosition))
            }

            binding.deleteBtn.setOnClickListener {
                listener.onDeletePostClick(getItem(adapterPosition))
            }
        }

        fun bind(post: Post) {
            binding.postHeaderTv.text = post.postHeader
            binding.postTextTv.text = post.postText

            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val formatedDate = dateFormat.format(Date(post.postDate))
            binding.postDateTv.text = formatedDate

            if(isAdmin) {
                binding.editBtn.visibility = ViewGroup.VISIBLE
                binding.deleteBtn.visibility = ViewGroup.VISIBLE
            } else {
                binding.editBtn.visibility = ViewGroup.GONE
                binding.deleteBtn.visibility = ViewGroup.GONE
            }

        }
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post) = oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) = oldItem == newItem
    }

    interface PostDetailsClickListener {
        fun onEditPostClick(post: Post)
        fun onDeletePostClick(post: Post)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostDetailsViewHolder {
        return PostDetailsViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: PostDetailsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

