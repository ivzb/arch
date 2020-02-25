package com.ivzb.arch.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.updatePadding
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ivzb.arch.analytics.AnalyticsActions
import com.ivzb.arch.analytics.AnalyticsHelper
import com.ivzb.arch.analytics.AnalyticsScreens
import com.ivzb.arch.databinding.FragmentSearchBinding
import com.ivzb.arch.domain.EventObserver
import com.ivzb.arch.model.Link
import com.ivzb.arch.ui.feed.FeedFragmentDirections
import com.ivzb.arch.ui.link.LinkOptionsDialogFragment
import com.ivzb.arch.ui.main.MainNavigationFragment
import com.ivzb.arch.util.dismissKeyboard
import com.ivzb.arch.util.doOnApplyWindowInsets
import com.ivzb.arch.util.showKeyboard
import com.ivzb.arch.util.viewModelProvider
import kotlinx.android.synthetic.main.fragment_search.view.*
import javax.inject.Inject

class SearchFragment : MainNavigationFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private lateinit var binding: FragmentSearchBinding
    private lateinit var model: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        model = viewModelProvider(viewModelFactory)

        binding = FragmentSearchBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = model
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyticsHelper.sendScreenView(AnalyticsScreens.SEARCH, requireActivity())

        binding.toolbar.searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    this@apply.dismissKeyboard()
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    model.onSearchQueryChanged(newText)
                    return true
                }
            })

            // Set focus on the SearchView and open the keyboard
            setOnQueryTextFocusChangeListener { view, hasFocus ->
                if (hasFocus) {
                    view.findFocus()?.showKeyboard()
                }
            }

            requestFocus()
        }

        binding.recyclerView.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePadding(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }

        model.performLinkClickEvent.observe(viewLifecycleOwner, EventObserver { link ->
            openDetails(link.id)
        })

        model.performLinkLongClickEvent.observe(viewLifecycleOwner, EventObserver { link ->
            openLinkOptionsDialog(link)
        })
    }

    override fun onPause() {
        binding.toolbar.searchView.dismissKeyboard()
        super.onPause()
    }

    private fun openDetails(id: Int) {
        analyticsHelper.logUiEvent(AnalyticsActions.HOME_TO_DETAILS)
        findNavController().navigate(FeedFragmentDirections.toDetails(id))
    }

    private fun openLinkOptionsDialog(link: Link) {
        analyticsHelper.logUiEvent(AnalyticsActions.HOME_TO_OPTIONS)
        LinkOptionsDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable(LinkOptionsDialogFragment.LINK, link)
            }

            show(this@SearchFragment.parentFragmentManager, DIALOG_LINK_OPTIONS)
        }
    }

    companion object {

        private const val DIALOG_LINK_OPTIONS = "dialog_link_options"
    }
}
