package com.ivzb.arch.domain.settings

import androidx.core.os.BuildCompat
import com.ivzb.arch.data.prefs.PreferenceStorage
import com.ivzb.arch.domain.UseCase
import com.ivzb.arch.model.Theme
import com.ivzb.arch.model.themeFromStorageKey
import javax.inject.Inject

open class GetThemeUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : UseCase<Unit, Theme>() {

    override fun execute(parameters: Unit): Theme {
        preferenceStorage.selectedTheme?.let { key ->
            return themeFromStorageKey(key)
        }
        // If we get here, we don't currently have a theme set, so we need to provide a default
        return when {
            BuildCompat.isAtLeastQ() -> Theme.SYSTEM
            else -> Theme.BATTERY_SAVER
        }
    }
}
