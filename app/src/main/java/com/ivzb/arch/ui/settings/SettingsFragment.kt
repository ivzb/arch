package com.ivzb.arch.ui.settings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.TextView
import androidx.core.view.updatePaddingRelative
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModelProvider
import com.ivzb.arch.R
import com.ivzb.arch.databinding.FragmentSettingsBinding
import com.ivzb.arch.domain.EventObserver
import com.ivzb.arch.ui.main.MainNavigationFragment
import com.ivzb.arch.util.doOnApplyWindowInsets
import com.ivzb.arch.util.viewModelProvider
import javax.inject.Inject

class SettingsFragment : MainNavigationFragment() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = viewModelProvider(viewModelFactory)

        viewModel.navigateToThemeSelector.observe(this, EventObserver {
            ThemeSettingDialogFragment.newInstance()
                .show(requireFragmentManager(), null)
        })

        val binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.settingsScroll.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePaddingRelative(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }

        return binding.root
    }
}

@BindingAdapter(value = ["dialogTitle", "fileLink"], requireAll = true)
fun createDialogForFile(button: View, dialogTitle: String, fileLink: String) {
    val context = button.context
    button.setOnClickListener {
        val webView = WebView(context).apply { loadUrl(fileLink) }
        AlertDialog.Builder(context)
            .setTitle(dialogTitle)
            .setView(webView)
            .create()
            .show()
    }
}

@BindingAdapter("versionName")
fun setVersionName(view: TextView, versionName: String) {
    view.text = view.resources.getString(R.string.version_name, versionName)
}
