package com.ivzb.arch.ui.link

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ivzb.arch.analytics.AnalyticsActions
import com.ivzb.arch.analytics.AnalyticsHelper
import com.ivzb.arch.analytics.AnalyticsScreens
import com.ivzb.arch.databinding.DialogLinkOptionsBinding
import com.ivzb.arch.domain.EventObserver
import com.ivzb.arch.model.Link
import com.ivzb.arch.util.copy
import com.ivzb.arch.util.executeAfter
import com.ivzb.arch.util.share
import com.ivzb.arch.util.viewModelProvider
import dagger.android.support.DaggerAppCompatDialogFragment
import javax.inject.Inject

/**
 * Dialog that tells the user to sign in to continue the operation.
 */
class LinkOptionsDialogFragment : DaggerAppCompatDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private lateinit var linkOptionsViewModel: LinkOptionsViewModel

    private lateinit var binding: DialogLinkOptionsBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // We want to create a dialog, but we also want to use DataBinding for the content view.
        // We can do that by making an empty dialog and adding the content later.
        return MaterialAlertDialogBuilder(requireContext()).create()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // In case we are showing as a dialog, use getLayoutInflater() instead.
        binding = DialogLinkOptionsBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyticsHelper.sendScreenView(AnalyticsScreens.OPTIONS, requireActivity())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        linkOptionsViewModel = viewModelProvider(viewModelFactory)

        val link = arguments?.getParcelable<Link>(LINK) ?: throw IllegalArgumentException("no link passed")

        linkOptionsViewModel.setLink(link)

        linkOptionsViewModel.performLinkOptionEvent.observe(this, EventObserver { request ->
            when (request) {
                LinkOptionsEvent.Copy -> copy(link)
                LinkOptionsEvent.Share -> share(link)
                LinkOptionsEvent.Delete -> delete(link)
                LinkOptionsEvent.Visit -> visit()
            }

            dismiss()
        })

        binding.executeAfter {
            viewModel = linkOptionsViewModel
            lifecycleOwner = viewLifecycleOwner
        }

        if (showsDialog) {
            (requireDialog() as AlertDialog).setView(binding.root)
        }
    }

    private fun copy(link: Link) {
        copy(requireActivity(), link.title ?: "Link you copied", link.url)
        analyticsHelper.logUiEvent(AnalyticsActions.LINK_COPY)
    }

    private fun share(link: Link) {
        share(requireActivity(), link.url)
        analyticsHelper.logUiEvent(AnalyticsActions.LINK_SHARE)
    }

    private fun delete(link: Link) {
        linkOptionsViewModel.deleteLink(link)
        analyticsHelper.logUiEvent(AnalyticsActions.LINK_DELETE)
    }

    private fun visit() {
        analyticsHelper.logUiEvent(AnalyticsActions.LINK_VISIT)
    }

    companion object {

        const val LINK = "LINK"
    }
}
