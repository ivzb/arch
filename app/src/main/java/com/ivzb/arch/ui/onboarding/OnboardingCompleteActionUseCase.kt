package com.ivzb.arch.ui.onboarding

import com.ivzb.arch.data.prefs.PreferenceStorage
import com.ivzb.arch.domain.UseCase
import javax.inject.Inject

/**
 * Records whether onboarding has been completed.
 */
open class OnboardingCompleteActionUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<Boolean, Unit>() {

    override fun execute(parameters: Boolean) {
        preferenceStorage.onboardingCompleted = parameters
    }
}
