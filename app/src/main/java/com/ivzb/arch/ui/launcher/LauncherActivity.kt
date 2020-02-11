package com.ivzb.arch.ui.launcher

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.ivzb.arch.domain.EventObserver
import com.ivzb.arch.ui.main.MainActivity
import com.ivzb.arch.ui.onboarding.OnboardingActivity
import com.ivzb.arch.util.checkAllMatched
import com.ivzb.arch.util.viewModelProvider
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject
import java.net.URL

/**
 * A 'Trampoline' activity for sending users to an appropriate screen on launch.
 */
class LauncherActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModel: LaunchViewModel = viewModelProvider(viewModelFactory)
        viewModel.launchDestination.observe(this, EventObserver { destination ->
            when (destination) {
                LaunchDestination.MAIN_ACTIVITY -> startActivity(Intent(this, MainActivity::class.java))
                LaunchDestination.ONBOARDING -> startActivity(Intent(this, OnboardingActivity::class.java))
            }.checkAllMatched
            finish()
        })

        when {
            intent?.action == Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    viewModel.handleSendText(intent)
                } else if (intent.type?.startsWith("image/") == true) {
                    viewModel.handleSendImage(intent)
                }
            }

            intent?.action == Intent.ACTION_SEND_MULTIPLE && intent.type?.startsWith("image/") == true -> {
                viewModel.handleSendMultipleImages(intent)
            }
        }
    }
}
