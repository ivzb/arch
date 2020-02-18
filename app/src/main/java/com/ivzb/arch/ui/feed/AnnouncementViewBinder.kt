package com.ivzb.arch.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ivzb.arch.R
import com.ivzb.arch.databinding.ItemFeedAnnouncementBinding
import com.ivzb.arch.model.Announcement

class AnnouncementViewBinder(
    private val lifecycleOwner: LifecycleOwner
) : FeedItemViewBinder<Announcement, AnnouncementViewHolder>(
    Announcement::class.java
) {

    override fun createViewHolder(parent: ViewGroup): AnnouncementViewHolder =
        AnnouncementViewHolder(
            ItemFeedAnnouncementBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            lifecycleOwner
        )

    override fun bindViewHolder(model: Announcement, viewHolder: AnnouncementViewHolder) {
        viewHolder.bind(model)
    }

    override fun getFeedItemType(): Int = R.layout.item_feed_announcement

    override fun areItemsTheSame(oldItem: Announcement, newItem: Announcement): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Announcement, newItem: Announcement) =
        oldItem == newItem
}

class AnnouncementViewHolder(
    private val binding: ItemFeedAnnouncementBinding,
    private val lifecycleOwner: LifecycleOwner
) : ViewHolder(binding.root) {

    fun bind(announcement: Announcement) {
        binding.announcement = announcement
        binding.lifecycleOwner = lifecycleOwner
        binding.executePendingBindings()
    }
}
