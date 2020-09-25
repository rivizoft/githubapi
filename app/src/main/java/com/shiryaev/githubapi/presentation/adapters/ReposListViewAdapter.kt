package com.shiryaev.githubapi.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shiryaev.githubapi.R
import com.shiryaev.githubapi.domain.RepositoryData
import kotlinx.android.synthetic.main.repo_item.view.*
import java.io.Serializable

class ReposListViewAdapter(private val clickOnRepoListener: ClickOnRepoListener)
    : RecyclerView.Adapter<ReposListViewAdapter.ReposListViewHolder>(), Serializable
{
    private var repoList: List<RepositoryData> = listOf()

    inner class ReposListViewHolder(inflater: LayoutInflater, parent: ViewGroup)
        : RecyclerView.ViewHolder(inflater.inflate(R.layout.repo_item, parent, false))
    {
        fun bind(repo: RepositoryData)
        {
            itemView.tv_title_repo.text = repo.title
            if (repo.starCount >= 1000)
                itemView.tv_count_stars.text = "${repo.starCount / 1000}k"
            else
                itemView.tv_count_stars.text = repo.starCount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposListViewHolder
    {
        val inflater = LayoutInflater.from(parent.context)
        return ReposListViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: ReposListViewHolder, position: Int)
    {
        val repo = repoList[position]
        holder.bind(repo)

        holder.itemView.setOnClickListener {
            clickOnRepoListener.onRepoItemClicked(repo, position)
        }
    }

    override fun getItemCount(): Int = repoList.size

    fun setData(repos: List<RepositoryData>)
    {
        repoList = repos
        notifyDataSetChanged()
    }
}

interface ClickOnRepoListener
{
    fun onRepoItemClicked(repo: RepositoryData, position: Int)
}