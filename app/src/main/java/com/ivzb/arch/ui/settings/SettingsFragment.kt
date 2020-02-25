package com.ivzb.arch.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.updatePaddingRelative
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModelProvider
import com.ivzb.arch.R
import com.ivzb.arch.analytics.AnalyticsHelper
import com.ivzb.arch.analytics.AnalyticsScreens
import com.ivzb.arch.databinding.FragmentSettingsBinding
import com.ivzb.arch.domain.EventObserver
import com.ivzb.arch.ui.main.MainNavigationFragment
import com.ivzb.arch.util.doOnApplyWindowInsets
import com.ivzb.arch.util.viewModelProvider
import javax.inject.Inject

class SettingsFragment : MainNavigationFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        analyticsHelper.sendScreenView(AnalyticsScreens.SETTINGS, requireActivity())
    }
}

@BindingAdapter("versionName")
fun setVersionName(view: TextView, versionName: String) {
    view.text = view.resources.getString(R.string.version_name, versionName)
}
