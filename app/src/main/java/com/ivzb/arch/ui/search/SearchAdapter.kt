package com.ivzb.arch.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ivzb.arch.databinding.ItemSearchResultBinding

class SearchAdapter(
    private val searchViewModel: SearchViewModel
) : ListAdapter<SearchResult, SearchViewHolder>(SearchDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            searchViewModel
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class SearchViewHolder(
    private val binding: ItemSearchResultBinding,
    private val searchViewModel: SearchViewModel
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(searchResult: SearchResult) {
        binding.eventListener = searchViewModel
        binding.searchResult = searchResult
        binding.executePendingBindings()
    }
}

object SearchDiff : DiffUtil.ItemCallback<SearchResult>() {

    override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult) =
        oldItem == newItem
}
