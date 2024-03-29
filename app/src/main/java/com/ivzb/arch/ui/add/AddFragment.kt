package com.ivzb.arch.ui.add

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.ivzb.arch.R
import com.ivzb.arch.analytics.AnalyticsActions
import com.ivzb.arch.analytics.AnalyticsHelper
import com.ivzb.arch.analytics.AnalyticsScreens
import com.ivzb.arch.databinding.FragmentAddBinding
import com.ivzb.arch.ui.main.MainNavigationFragment
import com.ivzb.arch.util.*
import javax.inject.Inject

class AddFragment : MainNavigationFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private lateinit var addViewModel: AddViewModel

    private lateinit var binding: FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addViewModel = viewModelProvider(viewModelFactory)

        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.link_shared_enter)

        binding = FragmentAddBinding.inflate(inflater, container, false).apply {
            viewModel = addViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        binding.tilUrl.setEndIconOnClickListener {
            if (clipboard.hasLink()) {
                binding.etUrl.setText(clipboard.getLink())
                analyticsHelper.logUiEvent(AnalyticsActions.ADD_LINK_CLIPBOARD)
            } else {
                Toast.makeText(requireContext(), "Clipboard is empty.", Toast.LENGTH_LONG).show()
                analyticsHelper.logUiEvent(AnalyticsActions.ADD_LINK_EMPTY_CLIPBOARD)
            }
        }

        binding.tilCategory.setEndIconOnClickListener {
            if (clipboard.hasLink()) {
                binding.etCategory.setText(clipboard.getLink())
                analyticsHelper.logUiEvent(AnalyticsActions.ADD_CATEGORY_CLIPBOARD)
            } else {
                Toast.makeText(requireContext(), "Clipboard is empty.", Toast.LENGTH_LONG).show()
                analyticsHelper.logUiEvent(AnalyticsActions.ADD_CATEGORY_EMPTY_CLIPBOARD)
            }
        }

        binding.etUrl.doOnTextChanged { text, _, _, _ ->
            addViewModel.typeUrl(text.toString())
        }

        binding.fabSaveLink.setOnClickListener {
            addViewModel.addLink(binding.etUrl.text.toString(), binding.etCategory.text.toString())
            analyticsHelper.logUiEvent(AnalyticsActions.ADD_LINK_MANUALLY)
            binding.root.dismissKeyboard()
            requireActivity().onBackPressed()
        }

        addViewModel.canAddLink.observe(this, Observer {
            binding.fabSaveLink.isEnabled = it
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyticsHelper.sendScreenView(AnalyticsScreens.ADD, requireActivity())
    }
}
