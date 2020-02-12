package com.ivzb.arch.ui.launcher

import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.arch.domain.Event
import com.ivzb.arch.domain.Result
import com.ivzb.arch.domain.links.InsertLinkUseCase
import com.ivzb.arch.domain.prefs.OnboardingCompletedUseCase
import com.ivzb.arch.model.Link
import com.ivzb.arch.util.map
import javax.inject.Inject

/**
 * Logic for determining which screen to send users to on app launch.
 */
class LaunchViewModel @Inject constructor(
    onboardingCompletedUseCase: OnboardingCompletedUseCase,
    val insertLinkUseCase: InsertLinkUseCase
) : ViewModel() {

    private val onboardingCompletedResult = MutableLiveData<Result<Boolean>>()
    val launchDestination: LiveData<Event<LaunchDestination>>

    init {
        // Check if onboarding has already been completed and then navigate the user accordingly
        onboardingCompletedUseCase(Unit, onboardingCompletedResult)
        launchDestination = onboardingCompletedResult.map {
            // If this check fails, prefer to launch main activity than show onboarding too often
            if ((it as? Result.Success)?.data == false) {
                Event(LaunchDestination.ONBOARDING)
            } else {
                Event(LaunchDestination.MAIN_ACTIVITY)
            }
        }
    }

    fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            insertLinkUseCase(
                Link(
                    title = "handle send text",
                    value = it
                )
            )
        }
    }

    fun handleSendImage(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            Log.d("arch", it.toString())
        }
    }

    fun handleSendMultipleImages(intent: Intent) {
        intent.getParcelableArrayListExtra<Parcelable>(Intent.EXTRA_STREAM)?.let {
            Log.d("arch", it.toString())
        }
    }
}

enum class LaunchDestination {
    ONBOARDING,
    MAIN_ACTIVITY
}
