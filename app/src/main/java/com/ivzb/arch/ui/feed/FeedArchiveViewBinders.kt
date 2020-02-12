package com.ivzb.arch.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ivzb.arch.R
import com.ivzb.arch.databinding.ItemFeedLinkBinding
import com.ivzb.arch.model.Link

class LinksViewBinder(
    private val lifecycleOwner: LifecycleOwner
) : FeedItemViewBinder<Link, LinkViewHolder>(
    Link::class.java
) {

    override fun createViewHolder(parent: ViewGroup): LinkViewHolder =
        LinkViewHolder(
            ItemFeedLinkBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            lifecycleOwner
        )

    override fun bindViewHolder(model: Link, viewHolder: LinkViewHolder) {
        viewHolder.bind(model)
    }

    override fun getFeedItemType(): Int = R.layout.item_feed_link

    override fun areItemsTheSame(oldItem: Link, newItem: Link): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Link, newItem: Link) =
        oldItem == newItem
}

class LinkViewHolder(
    private val binding: ItemFeedLinkBinding,
    private val lifecycleOwner: LifecycleOwner
) : ViewHolder(binding.root) {

    fun bind(link: Link) {
        binding.link = link
        binding.lifecycleOwner = lifecycleOwner
        binding.executePendingBindings()
    }
}

// Shown while loading Links
object LoadingIndicator

class LoadingViewHolder(itemView: View) : ViewHolder(itemView)

class LinksLoadingViewBinder : FeedItemViewBinder<LoadingIndicator, LoadingViewHolder>(
    LoadingIndicator::class.java
) {

    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        return LoadingViewHolder(
            LayoutInflater.from(parent.context).inflate(getFeedItemType(), parent, false)
        )
    }

    override fun bindViewHolder(model: LoadingIndicator, viewHolder: LoadingViewHolder) {}

    override fun getFeedItemType() = R.layout.item_feed_links_loading

    override fun areItemsTheSame(oldItem: LoadingIndicator, newItem: LoadingIndicator) = true

    override fun areContentsTheSame(oldItem: LoadingIndicator, newItem: LoadingIndicator) = true
}

// Shown if there are no Links or fetching Links fails
object LinkEmpty

class EmptyViewHolder(itemView: View) : ViewHolder(itemView)

class LinksEmptyViewBinder : FeedItemViewBinder<LinkEmpty, EmptyViewHolder>(
    LinkEmpty::class.java
) {

    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        return EmptyViewHolder(
            LayoutInflater.from(parent.context).inflate(getFeedItemType(), parent, false)
        )
    }

    override fun bindViewHolder(model: LinkEmpty, viewHolder: EmptyViewHolder) {}

    override fun getFeedItemType() = R.layout.item_feed_links_empty

    override fun areItemsTheSame(oldItem: LinkEmpty, newItem: LinkEmpty) = true

    override fun areContentsTheSame(oldItem: LinkEmpty, newItem: LinkEmpty) = true
}