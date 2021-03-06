package com.ivzb.arch.ui.feed

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.updatePaddingRelative
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ivzb.arch.R
import com.ivzb.arch.analytics.AnalyticsActions
import com.ivzb.arch.analytics.AnalyticsHelper
import com.ivzb.arch.analytics.AnalyticsScreens
import com.ivzb.arch.databinding.FragmentFeedBinding
import com.ivzb.arch.domain.EventObserver
import com.ivzb.arch.model.Link
import com.ivzb.arch.ui.feed.FeedFragmentDirections.Companion.toAdd
import com.ivzb.arch.ui.feed.FeedFragmentDirections.Companion.toDetails
import com.ivzb.arch.ui.feed.FeedFragmentDirections.Companion.toSearch
import com.ivzb.arch.ui.link.LinkOptionsDialogFragment
import com.ivzb.arch.ui.main.MainNavigationFragment
import com.ivzb.arch.ui.messages.SnackbarMessageManager
import com.ivzb.arch.util.*
import javax.inject.Inject

class FeedFragment : MainNavigationFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var snackbarMessageManager: SnackbarMessageManager

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

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

        analyticsHelper.sendScreenView(AnalyticsScreens.HOME, requireActivity())

        // Set up search menu item
        binding.toolbar.run {
            inflateMenu(R.menu.search_menu)

            model.searchVisible.observe(viewLifecycleOwner, Observer {
                menu.findItem(R.id.search).isVisible = it?.peekContent() ?: false
            })

            setOnMenuItemClickListener { item ->
                if (item.itemId == R.id.search) {
                    openSearch()
                    true
                } else {
                    false
                }
            }
        }

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
            openDetails(link.id)
        })

        model.performLinkLongClickEvent.observe(viewLifecycleOwner, EventObserver { link ->
            openLinkOptionsDialog(link)
        })

        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        binding.fabAddLink.setOnClickListener {
            openAdd()
        }

        binding.fabAddLink.setOnLongClickListener {
            if (!clipboard.hasLink()) {
                openAdd()
            } else {
                model.addLink(clipboard.getLink())
                analyticsHelper.logUiEvent(AnalyticsActions.ADD_LINK_CLIPBOARD)
            }

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
        analyticsHelper.logUiEvent(AnalyticsActions.HOME_TO_OPTIONS)
        LinkOptionsDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable(LinkOptionsDialogFragment.LINK, link)
            }

            show(this@FeedFragment.parentFragmentManager, DIALOG_LINK_OPTIONS)
        }
    }

    private fun openSearch() {
        analyticsHelper.logUiEvent(AnalyticsActions.HOME_TO_SEARCH)
        findNavController().navigate(toSearch())
    }

    private fun openAdd() {
        analyticsHelper.logUiEvent(AnalyticsActions.HOME_TO_ADD)
        findNavController().navigate(toAdd())
    }

    private fun openDetails(id: Int) {
        analyticsHelper.logUiEvent(AnalyticsActions.HOME_TO_DETAILS)
        findNavController().navigate(toDetails(id))
    }

    companion object {

        private const val DIALOG_LINK_OPTIONS = "dialog_link_options"
    }
}
