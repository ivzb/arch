package com.ivzb.arch.ui.feed

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

        binding.root.doOnApplyWindowInsets { _, insets, _ ->
            binding.statusBar.run {
                layoutParams.height = insets.systemWindowInsetTop
                isVisible = layoutParams.height > 0
                requestLayout()
            }
        }

        binding.recyclerView.doOnApplyWindowInsets { v, insets, padding ->
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
            showFeedItems(binding.recyclerView, it)
        })
    }

    private fun showFeedItems(recyclerView: RecyclerView, list: List<Any>?) {
        if (adapter == null) {
            val sectionHeaderViewBinder = FeedSectionHeaderViewBinder()
            val linksViewBinder = LinksViewBinder(this)
            val linksEmptyViewBinder = LinksEmptyViewBinder()
            val linksLoadingViewBinder = LinksLoadingViewBinder()

            val viewBinders = HashMap<FeedItemClass, FeedItemBinder>()
            viewBinders.put(sectionHeaderViewBinder.modelClass, sectionHeaderViewBinder as FeedItemBinder)
            viewBinders.put(linksViewBinder.modelClass, linksViewBinder as FeedItemBinder)
            viewBinders.put(linksEmptyViewBinder.modelClass, linksEmptyViewBinder as FeedItemBinder)
            viewBinders.put(linksLoadingViewBinder.modelClass, linksLoadingViewBinder as FeedItemBinder)

            adapter = FeedAdapter(viewBinders)
        }
        if (recyclerView.adapter == null) {
            recyclerView.adapter = adapter
        }
        (recyclerView.adapter as FeedAdapter).submitList(list ?: emptyList())
    }
}
