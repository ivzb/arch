package com.ivzb.arch.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ivzb.arch.R

// Shown while loading feed
object LoadingIndicator

class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

class LoadingViewBinder : FeedItemViewBinder<LoadingIndicator, LoadingViewHolder>(
    LoadingIndicator::class.java
) {

    override fun createViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return LoadingViewHolder(
            LayoutInflater.from(parent.context).inflate(getFeedItemType(), parent, false)
        )
    }

    override fun bindViewHolder(model: LoadingIndicator, viewHolder: LoadingViewHolder) {}

    override fun getFeedItemType() = R.layout.item_feed_loading

    override fun areItemsTheSame(oldItem: LoadingIndicator, newItem: LoadingIndicator) = true

    override fun areContentsTheSame(oldItem: LoadingIndicator, newItem: LoadingIndicator) = true
}
