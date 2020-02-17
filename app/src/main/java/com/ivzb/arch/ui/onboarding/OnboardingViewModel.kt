package com.ivzb.arch.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ivzb.arch.domain.Event
import javax.inject.Inject

/**
 * Records that onboarding has been completed and navigates user onward.
 */
class OnboardingViewModel @Inject constructor(
    private val onboardingCompleteActionUseCase: OnboardingCompleteActionUseCase
) : ViewModel() {

    private val _navigateToMainActivity = MutableLiveData<Event<Unit>>()
    val navigateToMainActivity: LiveData<Event<Unit>> = _navigateToMainActivity

    fun getStartedClick() {
        _navigateToMainActivity.postValue(Event(Unit))
    }

    fun completeOnboarding() {
        onboardingCompleteActionUseCase(true)
    }
}
