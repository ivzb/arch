package com.ivzb.arch.ui.feed

import android.content.ClipDescription.MIMETYPE_TEXT_PLAIN
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.updatePaddingRelative
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.ivzb.arch.databinding.FragmentFeedBinding
import com.ivzb.arch.domain.EventObserver
import com.ivzb.arch.model.Link
import com.ivzb.arch.ui.link.LinkOptionsDialogFragment
import com.ivzb.arch.ui.main.MainNavigationFragment
import com.ivzb.arch.ui.messages.SnackbarMessageManager
import com.ivzb.arch.util.doOnApplyWindowInsets
import com.ivzb.arch.util.setUpSnackbar
import com.ivzb.arch.util.viewModelProvider
import javax.inject.Inject

class FeedFragment : MainNavigationFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var snackbarMessageManager: SnackbarMessageManager

    private lateinit var model: FeedViewModel
    private lateinit var binding: FragmentFeedBinding
    private var adapter: FeedAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = viewModelProvider(viewModelFactory)

        binding = FragmentFeedBinding.inflate(
            inflater, container, false
        ).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = model
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvFeed.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePaddingRelative(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }

        binding.snackbar.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePaddingRelative(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }

        setUpSnackbar(model.snackBarMessage, binding.snackbar, snackbarMessageManager)

        model.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            val errorMessage = message?.getContentIfNotHandled()
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this.context, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

        model.feed.observe(viewLifecycleOwner, Observer {
            showFeedItems(binding.rvFeed, it)
        })

        model.performLinkClickEvent.observe(viewLifecycleOwner, EventObserver { link ->
            openLinkOptionsDialog(link)
        })

        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        binding.fabAddLink.setOnClickListener {
            if (!hasClipboardText(clipboard)) {
                Toast.makeText(requireContext(), "Clipboard is empty.", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            val link = clipboard.primaryClip?.getItemAt(0)?.text.toString()

            model.addLink(link)

            Toast.makeText(requireContext(), "Inserted $link.", Toast.LENGTH_LONG).show()
        }
    }

    private fun hasClipboardText(clipboard: ClipboardManager) = when {
        !clipboard.hasPrimaryClip() -> {
            // disable add link button, since the clipboard is empty
            false
        }

        !(clipboard.primaryClipDescription?.hasMimeType(MIMETYPE_TEXT_PLAIN) ?: false) -> {
            // disable add link button, since the clipboard has data but it is not plain text
            false
        }

        (clipboard.primaryClip?.itemCount ?: 0 > 0) && (clipboard.primaryClip?.getItemAt(0)?.text?.trim()?.isEmpty()
            ?: false) -> {
            // disable add link button, since the clipboard is empty
            false
        }

        else -> {
            // enable add link button, since the clipboard contains plain text
            true
        }
    }

    private fun showFeedItems(recyclerView: RecyclerView, list: List<Any>?) {
        if (adapter == null) {
            val announcementViewBinder = AnnouncementViewBinder(this)
            val sectionHeaderViewBinder = FeedSectionHeaderViewBinder()
            val linksViewBinder = LinksViewBinder(this, model)
            val linksEmptyViewBinder = LinksEmptyViewBinder()
            val loadingViewBinder = LoadingViewBinder()

            val viewBinders = HashMap<FeedItemClass, FeedItemBinder>().apply {
                put(
                    announcementViewBinder.modelClass,
                    announcementViewBinder as FeedItemBinder
                )

                put(
                    sectionHeaderViewBinder.modelClass,
                    sectionHeaderViewBinder as FeedItemBinder
                )

                put(
                    linksViewBinder.modelClass,
                    linksViewBinder as FeedItemBinder
                )

                put(
                    linksEmptyViewBinder.modelClass,
                    linksEmptyViewBinder as FeedItemBinder
                )

                put(
                    loadingViewBinder.modelClass,
                    loadingViewBinder as FeedItemBinder
                )
            }

            adapter = FeedAdapter(viewBinders)
        }

        if (recyclerView.adapter == null) {
            recyclerView.adapter = adapter
        }

        (recyclerView.adapter as FeedAdapter).submitList(list ?: emptyList())
    }

    private fun openLinkOptionsDialog(link: Link) {
        LinkOptionsDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable(LinkOptionsDialogFragment.LINK, link)
            }

            show(this@FeedFragment.parentFragmentManager, DIALOG_LINK_OPTIONS)
        }
    }

    companion object {

        private const val DIALOG_LINK_OPTIONS = "dialog_link_options"
    }
}
