package com.ivzb.arch.ui.feed

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.ivzb.arch.R
import com.ivzb.arch.databinding.ItemGenericSectionHeaderBinding
import com.ivzb.arch.ui.SectionHeader

class FeedSectionHeaderViewBinder :
    FeedItemViewBinder<SectionHeader, SectionHeaderViewHolder>(SectionHeader::class.java) {

    override fun createViewHolder(parent: ViewGroup): SectionHeaderViewHolder =
        SectionHeaderViewHolder(
            ItemGenericSectionHeaderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun bindViewHolder(model: SectionHeader, viewHolder: SectionHeaderViewHolder) {
        viewHolder.bind(model)
    }

    override fun getFeedItemType(): Int = R.layout.item_generic_section_header

    override fun areItemsTheSame(oldItem: SectionHeader, newItem: SectionHeader): Boolean =
        oldItem.titleId == newItem.titleId

    // This is called if [areItemsTheSame] is true, in which case we know the contents match.
    override fun areContentsTheSame(oldItem: SectionHeader, newItem: SectionHeader) = true
}

class SectionHeaderViewHolder(
    private val binding: ItemGenericSectionHeaderBinding
) : ViewHolder(binding.root) {

    fun bind(model: SectionHeader) {
        binding.sectionHeader = model
    }
}
