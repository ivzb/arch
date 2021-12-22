package com.ivzb.arch.ui.edit

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
import com.ivzb.arch.databinding.FragmentEditBinding
import com.ivzb.arch.ui.main.MainNavigationFragment
import com.ivzb.arch.util.*
import javax.inject.Inject

class EditFragment : MainNavigationFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private lateinit var detailsViewModel: EditViewModel

    private lateinit var binding: FragmentEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        detailsViewModel = viewModelProvider(viewModelFactory)

        sharedElementReturnTransition =
            TransitionInflater.from(context).inflateTransition(R.transition.link_shared_enter)

        binding = FragmentEditBinding.inflate(inflater, container, false).apply {
            viewModel = detailsViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        val clipboard =
            requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

        binding.tilUrl.setEndIconOnClickListener {
            if (clipboard.hasLink()) {
                binding.etUrl.setText(clipboard.getLink())
                analyticsHelper.logUiEvent(AnalyticsActions.EDIT_LINK_CLIPBOARD)
            } else {
                Toast.makeText(requireContext(), "Clipboard is empty.", Toast.LENGTH_LONG).show()
                analyticsHelper.logUiEvent(AnalyticsActions.EDIT_LINK_EMPTY_CLIPBOARD)
            }
        }

        binding.tilCategory.setEndIconOnClickListener {
            if (clipboard.hasLink()) {
                binding.etCategory.setText(clipboard.getLink())
                analyticsHelper.logUiEvent(AnalyticsActions.EDIT_CATEGORY_CLIPBOARD)
            } else {
                Toast.makeText(requireContext(), "Clipboard is empty.", Toast.LENGTH_LONG).show()
                analyticsHelper.logUiEvent(AnalyticsActions.EDIT_CATEGORY_EMPTY_CLIPBOARD)
            }
        }

        binding.etUrl.doOnTextChanged { text, _, _, _ ->
            detailsViewModel.typeUrl(text.toString())
        }

        requireArguments().apply {
            val linkId = EditFragmentArgs.fromBundle(this).linkId
            val linkUrl = EditFragmentArgs.fromBundle(this).linkUrl
            val linkCategory = EditFragmentArgs.fromBundle(this).linkCategory

            binding.etUrl.setText(linkUrl)
            binding.etCategory.setText(linkCategory)

            binding.fabSaveLink.setOnClickListener {
                detailsViewModel.editLink(linkId, binding.etUrl.text.toString(), binding.etCategory.text.toString())
                analyticsHelper.logUiEvent(AnalyticsActions.EDIT_LINK_MANUALLY)
                binding.root.dismissKeyboard()
                requireActivity().onBackPressed()
            }
        }

        detailsViewModel.canEditLink.observe(this, Observer {
            binding.fabSaveLink.isEnabled = it
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyticsHelper.sendScreenView(AnalyticsScreens.EDIT, requireActivity())
    }
}
