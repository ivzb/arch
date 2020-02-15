package com.ivzb.arch.ui.theme

import androidx.lifecycle.LiveData
import com.ivzb.arch.domain.Result
import com.ivzb.arch.domain.settings.GetThemeUseCase
import com.ivzb.arch.domain.settings.ObserveThemeModeUseCase
import com.ivzb.arch.model.Theme
import com.ivzb.arch.util.map
import javax.inject.Inject

/**
 * Interface to implement activity theming via a ViewModel.
 *
 * You can inject a implementation of this via Dagger2, then use the implementation as an interface
 * delegate to add the functionality without writing any code
 *
 * Example usage:
 * ```
 * class MyViewModel @Inject constructor(
 *     themedActivityDelegate: ThemedActivityDelegate
 * ) : ViewModel(), ThemedActivityDelegate by themedActivityDelegate {
 * ```
 */
interface ThemedActivityDelegate {

    /**
     * Allows observing of the current theme
     */
    val theme: LiveData<Theme>

    /**
     * Allows querying of the current theme synchronously
     */
    val currentTheme: Theme
}

class ThemedActivityDelegateImpl @Inject constructor(
    private val observeThemeUseCase: ObserveThemeModeUseCase,
    private val getThemeUseCase: GetThemeUseCase
) : ThemedActivityDelegate {

    override val theme: LiveData<Theme> by lazy(LazyThreadSafetyMode.NONE) {
        observeThemeUseCase.observe().map {
            if (it is Result.Success) it.data else Theme.SYSTEM
        }
    }

    override val currentTheme: Theme
        get() = getThemeUseCase.executeNow(Unit).let {
            if (it is Result.Success) it.data else Theme.SYSTEM
        }

    init {
        // Observe updates in dark mode setting
        observeThemeUseCase.execute(Unit)
    }
}
