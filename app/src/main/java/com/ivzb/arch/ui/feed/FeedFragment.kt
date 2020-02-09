package com.ivzb.arch.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.updatePaddingRelative
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ivzb.arch.databinding.FragmentFeedBinding
import com.ivzb.arch.ui.main.MainNavigationFragment
import com.ivzb.arch.util.activityViewModelProvider
import com.ivzb.arch.util.doOnApplyWindowInsets
import com.ivzb.arch.util.viewModelProvider
import javax.inject.Inject

class FeedFragment : MainNavigationFragment() {

    companion object {
        private const val BUNDLE_KEY_SESSIONS_LAYOUT_MANAGER_STATE = "sessions_layout_manager"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var model: FeedViewModel
    private lateinit var binding: FragmentFeedBinding
    private var adapter: FeedAdapter? = null
    private lateinit var sessionsViewBinder: FeedSessionsViewBinder

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

    override fun onSaveInstanceState(outState: Bundle) {
        if (::sessionsViewBinder.isInitialized) {
            outState.putParcelable(
                BUNDLE_KEY_SESSIONS_LAYOUT_MANAGER_STATE,
                sessionsViewBinder.recyclerViewManagerState
            )
        }
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setupProfileMenuItem(activityViewModelProvider(viewModelFactory), this)

        binding.root.doOnApplyWindowInsets { _, insets, _ ->
            binding.statusBar.run {
                layoutParams.height = insets.systemWindowInsetTop
                isVisible = layoutParams.height > 0
                requestLayout()
            }
        }

        if (adapter == null) {
            // Initialising sessionsViewBinder here to handle config change.
            sessionsViewBinder =
                FeedSessionsViewBinder(
                    model, savedInstanceState?.getParcelable(
                        BUNDLE_KEY_SESSIONS_LAYOUT_MANAGER_STATE
                    )
                )
        }

        binding.recyclerView.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePaddingRelative(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }

        binding.snackbar.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePaddingRelative(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }

        setUpSnackbar(model.snackBarMessage, binding.snackbar, snackbarMessageManager)

        model.errorMessage.observe(this, Observer { message ->
            val errorMessage = message?.getContentIfNotHandled()
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(this.context, errorMessage, Toast.LENGTH_SHORT).show()
                Timber.e(errorMessage)
            }
        })

        model.feed.observe(this, Observer {
            showFeedItems(binding.recyclerView, it)
        })

        model.navigateToSessionAction.observe(this, EventObserver { sessionId ->
            openSessionDetail(sessionId)
        })

        model.navigateToScheduleAction.observe(this, EventObserver { withPinnedEvents ->
            openSchedule(withPinnedEvents)
        })

        model.openSignInDialogAction.observe(this, EventObserver { openSignInDialog() })

        model.openLiveStreamAction.observe(this, EventObserver { streamUrl ->
            openLiveStreamUrl(streamUrl)
        })

        model.navigateToMapAction.observe(this, EventObserver { moment ->
            openMap(moment)
        })
    }

    private fun openSessionDetail(id: SessionId) {
        findNavController().navigate(toSessionDetail(id))
    }

    private fun openSchedule(withPinnedSessions: Boolean) {
        if (withPinnedSessions) {
            findNavController().navigate(toSchedule(showPinnedEvents = true))
        } else {
            findNavController().navigate(toSchedule(showAllEvents = true))
        }
    }

    private fun showFeedItems(recyclerView: RecyclerView, list: List<Any>?) {
        if (adapter == null) {
            val sectionHeaderViewBinder = FeedSectionHeaderViewBinder()
            val countdownViewBinder = CountdownViewBinder()
            val momentViewBinder = MomentViewBinder(
                eventListener = model,
                userInfoLiveData = model.currentUserInfo,
                themeLiveData = model.theme
            )
            val sessionsViewBinder = FeedSessionsViewBinder(model)
            val announcementViewBinder = AnnouncementViewBinder(model.timeZoneId, this)
            val announcementsEmptyViewBinder = AnnouncementsEmptyViewBinder()
            val announcementsLoadingViewBinder = AnnouncementsLoadingViewBinder()
            @Suppress("UNCHECKED_CAST")
            val viewBinders = ImmutableMap.builder<FeedItemClass, FeedItemBinder>()
                .put(
                    announcementViewBinder.modelClass,
                    announcementViewBinder as FeedItemBinder
                )
                .put(
                    sectionHeaderViewBinder.modelClass,
                    sectionHeaderViewBinder as FeedItemBinder
                )
                .put(
                    countdownViewBinder.modelClass,
                    countdownViewBinder as FeedItemBinder
                )
                .put(
                    momentViewBinder.modelClass,
                    momentViewBinder as FeedItemBinder
                )
                .put(
                    sessionsViewBinder.modelClass,
                    sessionsViewBinder as FeedItemBinder
                )
                .put(
                    announcementsEmptyViewBinder.modelClass,
                    announcementsEmptyViewBinder as FeedItemBinder
                )
                .put(
                    announcementsLoadingViewBinder.modelClass,
                    announcementsLoadingViewBinder as FeedItemBinder
                )
                .build()

            adapter = FeedAdapter(viewBinders)
        }
        if (recyclerView.adapter == null) {
            recyclerView.adapter = adapter
        }
        (recyclerView.adapter as FeedAdapter).submitList(list ?: emptyList())
    }
}
