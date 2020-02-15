package com.ivzb.arch.domain.settings

import com.ivzb.arch.data.prefs.PreferenceStorage
import com.ivzb.arch.domain.MediatorUseCase
import com.ivzb.arch.model.Theme
import com.ivzb.arch.model.themeFromStorageKey
import com.ivzb.arch.domain.Result
import javax.inject.Inject

open class ObserveThemeModeUseCase @Inject constructor(
    private val preferenceStorage: PreferenceStorage
) : MediatorUseCase<Unit, Theme>() {

    override fun execute(parameters: Unit) {
        result.addSource(preferenceStorage.observableSelectedTheme) {
            result.postValue(Result.Success(themeFromStorageKey(it)))
        }
    }
}
