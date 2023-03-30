package com.example.mygithubrepo.presenter.screen.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mygithubrepo.R
import com.example.mygithubrepo.domain.entity.RepoItems
import com.example.mygithubrepo.databinding.RepoListItemBinding

class RepoListAdapter(private val context: Context): PagingDataAdapter<RepoItems, RecyclerView.ViewHolder>(
    DiffCallback
){

    private lateinit var binding: RepoListItemBinding

    inner class ReposViewHolder :RecyclerView.ViewHolder(binding.root){

        fun bind(context: Context,user: RepoItems){
            with(binding){
                userName.text=user.userName
                repoLanguage.text = user.language
                repoName.text = user.repoName
                starCount.text = user.starCount
                Glide.with(context)
                    .load(user.avatarUrl)
                    .placeholder(R.drawable.ic_baseline_account_circle_24)
                    .apply(RequestOptions().circleCrop())
                    .into(repoUserImage)
            }

            binding.shareCode.setOnClickListener {
                val share = Intent(Intent.ACTION_SEND)
                share.putExtra(Intent.EXTRA_TEXT,"Hey friend! Check out this repository:)\n"+user.htmlUrl)
                share.type = "text/plain"
                context.startActivity(share)
            }

            binding.container.setOnClickListener {
                val go = Intent(Intent.ACTION_VIEW)
                go.data = Uri.parse(user.htmlUrl)
                context.startActivity(go)
            }

        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        binding= RepoListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ReposViewHolder()
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        getItem(position)?.let { (holder as ReposViewHolder).bind(context,it) }
    }

    /*
     * Allows the RecyclerView to determine which items have changed when the list
     * has been updated.
     */

    companion object DiffCallback : DiffUtil.ItemCallback<RepoItems>() {
        override fun areItemsTheSame(oldItem: RepoItems, newItem: RepoItems): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: RepoItems, newItem: RepoItems): Boolean {
             return oldItem == newItem  //Auto generated equality check from data classes
        }
    }


}