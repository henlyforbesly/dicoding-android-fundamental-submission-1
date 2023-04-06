package com.example.submission.ui.main

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.submission.databinding.ItemRowUserBinding
import com.example.submission.shared.Constants.Companion.GLIDE_HEIGHT
import com.example.submission.shared.Constants.Companion.GLIDE_WIDTH
import com.example.submission.data.responses.User
import com.example.submission.ui.detail.DetailActivity

class ListUserAdapter(private val users: List<User>) :
    RecyclerView.Adapter<ListUserAdapter.ViewHolder>() {
    class ViewHolder(var binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                Glide.with(this.root.context)
                    .load(user.avatarUrl)
                    .override(GLIDE_WIDTH, GLIDE_HEIGHT)
                    .circleCrop()
                    .into(imageViewAvatar)
                textViewUsername.text = user.login
            }

            itemView.setOnClickListener {
                val intentToDetail = Intent(itemView.context, DetailActivity::class.java)
                intentToDetail.putExtra(DetailActivity.EXTRA_USERNAME, user.login)
                itemView.context.startActivity(intentToDetail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(users[position])
}