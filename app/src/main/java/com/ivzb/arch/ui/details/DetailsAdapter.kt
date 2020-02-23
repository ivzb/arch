package com.ivzb.arch.ui.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ivzb.arch.R
import com.ivzb.arch.databinding.ItemLinkDetailBinding
import com.ivzb.arch.model.Link
import com.ivzb.arch.util.executeAfter

/**
 * [RecyclerView.Adapter] for presenting a link details, composed of information about the
 * link, any speakers plus any related events.
 */
class DetailsAdapter(
    private val lifecycleOwner: LifecycleOwner,
    private val detailsViewModel: DetailsViewModel
) : RecyclerView.Adapter<LinkDetailViewHolder>() {

    private val differ = AsyncListDiffer<Any>(this, DiffCallback)

    var link: List<Link> = emptyList()
        set(value) {
            field = value
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_link_detail -> LinkDetailViewHolder.LinkViewHolder(
                ItemLinkDetailBinding.inflate(inflater, parent, false)
            )
            else -> throw IllegalStateException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: LinkDetailViewHolder, position: Int) {
        when (holder) {
            is LinkDetailViewHolder.LinkViewHolder -> holder.binding.executeAfter {
                viewModel = detailsViewModel
                lifecycleOwner = this@DetailsAdapter.lifecycleOwner
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (differ.currentList[position]) {
            is Link -> R.layout.item_link_detail
            else -> throw IllegalStateException("Unknown view type at position $position")
        }
    }

    override fun getItemCount() = differ.currentList.size
}

/**
 * Diff items presented by this adapter.
 */
object DiffCallback : DiffUtil.ItemCallback<Any>() {

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is Link && newItem is Link -> oldItem == newItem
            else -> false
        }
    }

    @SuppressLint("DiffUtilEquals")
    // Workaround of https://issuetracker.google.com/issues/122928037
    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is Link && newItem is Link -> oldItem == newItem
            else -> true
        }
    }
}

/**
 * [RecyclerView.ViewHolder] types used by this adapter.
 */
sealed class LinkDetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class LinkViewHolder(
        val binding: ItemLinkDetailBinding
    ) : LinkDetailViewHolder(binding.root)
}
