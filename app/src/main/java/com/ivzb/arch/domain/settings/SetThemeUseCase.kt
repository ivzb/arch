package com.ivzb.arch.domain.settings

import com.ivzb.arch.data.prefs.PreferenceStorage
import com.ivzb.arch.domain.UseCase
import com.ivzb.arch.model.Theme
import javax.inject.Inject

open class SetThemeUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<Theme, Unit>() {

    override fun execute(parameters: Theme) {
        preferenceStorage.selectedTheme = parameters.storageKey
    }
}
