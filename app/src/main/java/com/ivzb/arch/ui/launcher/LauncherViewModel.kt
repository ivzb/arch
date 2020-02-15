package com.ivzb.arch.ui.launcher

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.arch.domain.Event
import com.ivzb.arch.domain.Result
import com.ivzb.arch.domain.links.InsertLinkUseCase
import com.ivzb.arch.domain.links.FetchLinkMetaDataUseCase
import com.ivzb.arch.domain.prefs.OnboardingCompletedUseCase
import com.ivzb.arch.model.Link
import com.ivzb.arch.util.map
import javax.inject.Inject

/**
 * Logic for determining which screen to send users to on app launch.
 */
class LaunchViewModel @Inject constructor(
    onboardingCompletedUseCase: OnboardingCompletedUseCase,
    val insertLinkUseCase: InsertLinkUseCase,
    val fetchLinkMetaDataUseCase: FetchLinkMetaDataUseCase
) : ViewModel() {

    private val onboardingCompletedResult = MutableLiveData<Result<Boolean>>()

    val launchDestination: LiveData<Event<LaunchDestination>>

    init {
        launchDestination = onboardingCompletedResult.map {
            // If this check fails, prefer to launch main activity than show onboarding too often
            if ((it as? Result.Success)?.data == false) {
                Event(LaunchDestination.ONBOARDING)
            } else {
                Event(LaunchDestination.MAIN_ACTIVITY)
            }
        }

        // Check if onboarding has already been completed and then navigate the user accordingly
        onboardingCompletedUseCase(Unit, onboardingCompletedResult)
    }

    fun handleLink(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            Link(url = it).let {
                insertLinkUseCase(it)
                fetchLinkMetaDataUseCase(it)
            }
        }
    }
}

enum class LaunchDestination {
    ONBOARDING,
    MAIN_ACTIVITY
}
