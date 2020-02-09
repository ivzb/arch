package com.ivzb.arch.ui.onboarding

import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.updatePadding
import com.ivzb.arch.R
import com.ivzb.arch.util.doOnApplyWindowInsets
import com.ivzb.arch.util.inTransaction
import com.ivzb.arch.util.viewModelProvider

class OnboardingActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: OnboardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewModel = viewModelProvider(viewModelFactory)

        // immersive mode so images can draw behind the status bar
        val decor = window.decorView
        val flags = decor.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        decor.systemUiVisibility = flags

        val container: FrameLayout = findViewById(R.id.fragment_container)
        container.doOnApplyWindowInsets { v, insets, padding ->
            v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
        }

        if (savedInstanceState == null) {
            supportFragmentManager.inTransaction {
                add(R.id.fragment_container, OnboardingFragment())
            }
        }
    }
}
