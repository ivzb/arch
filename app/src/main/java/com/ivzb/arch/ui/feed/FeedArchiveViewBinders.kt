package com.ivzb.arch.ui.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ivzb.arch.R
import com.ivzb.arch.databinding.ItemFeedArchiveBinding
import com.ivzb.arch.model.Archive

class ArchiveViewBinder(
    private val lifecycleOwner: LifecycleOwner
) : FeedItemViewBinder<Archive, ArchiveViewHolder>(
    Archive::class.java
) {

    override fun createViewHolder(parent: ViewGroup): ArchiveViewHolder =
        ArchiveViewHolder(
            ItemFeedArchiveBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            lifecycleOwner
        )

    override fun bindViewHolder(model: Archive, viewHolder: ArchiveViewHolder) {
        viewHolder.bind(model)
    }

    override fun getFeedItemType(): Int = R.layout.item_feed_archive

    override fun areItemsTheSame(oldItem: Archive, newItem: Archive): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Archive, newItem: Archive) =
        oldItem == newItem
}

class ArchiveViewHolder(
    private val binding: ItemFeedArchiveBinding,
    private val lifecycleOwner: LifecycleOwner
) : ViewHolder(binding.root) {

    fun bind(archive: Archive) {
        binding.archive = archive
        binding.lifecycleOwner = lifecycleOwner
        binding.executePendingBindings()
    }
}

// Shown while loading Archives
object LoadingIndicator

class LoadingViewHolder(itemView: View) : ViewHolder(itemView)

class ArchiveLoadingViewBinder : FeedItemViewBinder<LoadingIndicator, LoadingViewHolder>(
    LoadingIndicator::class.java
) {

    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        return LoadingViewHolder(
            LayoutInflater.from(parent.context).inflate(getFeedItemType(), parent, false)
        )
    }

    override fun bindViewHolder(model: LoadingIndicator, viewHolder: LoadingViewHolder) {}

    override fun getFeedItemType() = R.layout.item_feed_archive_loading

    override fun areItemsTheSame(oldItem: LoadingIndicator, newItem: LoadingIndicator) = true

    override fun areContentsTheSame(oldItem: LoadingIndicator, newItem: LoadingIndicator) = true
}

// Shown if there are no Archives or fetching Announcments fails
object ArchiveEmpty

class EmptyViewHolder(itemView: View) : ViewHolder(itemView)

class ArchiveEmptyViewBinder : FeedItemViewBinder<ArchiveEmpty, EmptyViewHolder>(
    ArchiveEmpty::class.java
) {

    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        return EmptyViewHolder(
            LayoutInflater.from(parent.context).inflate(getFeedItemType(), parent, false)
        )
    }

    override fun bindViewHolder(model: ArchiveEmpty, viewHolder: EmptyViewHolder) {}

    override fun getFeedItemType() = R.layout.item_feed_archive_empty

    override fun areItemsTheSame(oldItem: ArchiveEmpty, newItem: ArchiveEmpty) = true

    override fun areContentsTheSame(oldItem: ArchiveEmpty, newItem: ArchiveEmpty) = true
}
