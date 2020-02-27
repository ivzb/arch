package com.ivzb.arch.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.updatePadding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.ivzb.arch.R
import com.ivzb.arch.analytics.AnalyticsActions
import com.ivzb.arch.analytics.AnalyticsHelper
import com.ivzb.arch.analytics.AnalyticsScreens
import com.ivzb.arch.databinding.FragmentDetailsBinding
import com.ivzb.arch.domain.EventObserver
import com.ivzb.arch.ui.main.MainNavigationFragment
import androidx.navigation.fragment.findNavController
import com.ivzb.arch.ui.details.DetailsFragmentDirections.Companion.toEdit
import com.ivzb.arch.ui.messages.SnackbarMessageManager
import com.ivzb.arch.util.*
import javax.inject.Inject

class DetailsFragment : MainNavigationFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var snackbarMessageManager: SnackbarMessageManager

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private lateinit var detailsViewModel: DetailsViewModel

    private lateinit var binding: FragmentDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailsViewModel = viewModelProvider(viewModelFactory)

        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.link_shared_enter)
        // Delay the enter transition until image has loaded.
        postponeEnterTransition(500L)

        binding = FragmentDetailsBinding.inflate(inflater, container, false).apply {
            viewModel = detailsViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        binding.detailsBottomAppBar.run {
            inflateMenu(R.menu.details_menu)

            setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_item_copy -> {
                        detailsViewModel.link.value?.apply {
                            copy(requireActivity(), title ?: "Link you copied", url)
                            analyticsHelper.logUiEvent(AnalyticsActions.LINK_COPY)
                        }
                    }

                    R.id.menu_item_share -> {
                        detailsViewModel.link.value?.apply {
                            share(requireActivity(), url)
                            analyticsHelper.logUiEvent(AnalyticsActions.LINK_SHARE)
                        }
                    }

                    R.id.menu_item_delete -> {
                        detailsViewModel.link.value?.apply {
                            detailsViewModel.deleteLink(this)
                            analyticsHelper.logUiEvent(AnalyticsActions.LINK_DELETE)
                        }

                        requireActivity().onBackPressed()
                    }
                }

                true
            }
        }

        val detailsAdapter = DetailsAdapter(
            viewLifecycleOwner,
            detailsViewModel
        )

        binding.detailsRecyclerView.run {
            adapter = detailsAdapter
            itemAnimator?.run {
                addDuration = 120L
                moveDuration = 120L
                changeDuration = 120L
                removeDuration = 100L
            }
            doOnApplyWindowInsets { view, insets, state ->
                view.updatePadding(bottom = state.bottom + insets.systemWindowInsetBottom)
                // CollapsingToolbarLayout's defualt scrim visible trigger height is a bit large.
                // Choose something smaller so that the content stays visible longer.
                binding.collapsingToolbar.scrimVisibleHeightTrigger =
                    insets.systemWindowInsetTop * 2
            }
        }

        detailsViewModel.link.observe(this, Observer {
            detailsAdapter.link = listOf(it)
        })

        setUpSnackbar(detailsViewModel.snackBarMessage, binding.snackbar, snackbarMessageManager)

        detailsViewModel.errorMessage.observe(this, EventObserver { errorMsg ->
            // TODO: Change once there's a way to show errors to the user
            Toast.makeText(this.context, errorMsg, Toast.LENGTH_LONG).show()
        })

        binding.fabEditLink.setOnClickListener {
            detailsViewModel.link.value?.apply {
                openEdit(id, url)
            }
        }

        requireArguments().apply {
            val linkId = DetailsFragmentArgs.fromBundle(this).linkId
            detailsViewModel.observeLink(linkId)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyticsHelper.sendScreenView(AnalyticsScreens.DETAILS, requireActivity())
    }

    private fun openEdit(id: Int, url: String) {
        analyticsHelper.logUiEvent(AnalyticsActions.ADD_TO_EDIT)
        findNavController().navigate(toEdit(id, url))
    }
}
