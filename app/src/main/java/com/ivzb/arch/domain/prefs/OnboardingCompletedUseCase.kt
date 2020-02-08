package com.ivzb.arch.domain.prefs

import com.ivzb.arch.data.prefs.PreferenceStorage
import com.ivzb.arch.domain.UseCase
import javax.inject.Inject

/**
 * Returns whether onboarding has been completed.
 */
open class OnboardingCompletedUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<Unit, Boolean>() {
    override fun execute(parameters: Unit): Boolean = preferenceStorage.onboardingCompleted
}
