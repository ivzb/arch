package com.ivzb.arch.ui.link

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ShareCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ivzb.arch.R
import com.ivzb.arch.databinding.DialogLinkOptionsBinding
import com.ivzb.arch.domain.EventObserver
import com.ivzb.arch.util.executeAfter
import com.ivzb.arch.util.viewModelProvider
import dagger.android.support.DaggerAppCompatDialogFragment
import javax.inject.Inject

/**
 * Dialog that tells the user to sign in to continue the operation.
 */
class LinkOptionsDialogFragment : DaggerAppCompatDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        linkOptionsViewModel = viewModelProvider(viewModelFactory)

        val url = arguments?.getString(URL) ?: ""
        linkOptionsViewModel.setUrl(url)

        linkOptionsViewModel.performLinkOptionEvent.observe(this, EventObserver { request ->
            when (request) {
                LinkOptionsEvent.Copy -> copy(url)
                LinkOptionsEvent.Share -> share(url)
                LinkOptionsEvent.Delete -> delete(url)
                LinkOptionsEvent.Visit -> visit(url)
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

    private fun copy(url: String) {
        // TODO
    }

    private fun share(url: String) {
        ShareCompat.IntentBuilder.from(requireActivity())
            .setType("text/plain")
            .setText(url)
            .setChooserTitle(R.string.a11y_share)
            .startChooser()
    }

    private fun delete(url: String) {
        // TODO
    }

    private fun visit(url: String) {
        // do nothing
    }

    companion object {

        const val URL = "URL"
    }
}
