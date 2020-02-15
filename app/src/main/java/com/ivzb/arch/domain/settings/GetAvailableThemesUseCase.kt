package com.ivzb.arch.domain.settings

import androidx.core.os.BuildCompat
import com.ivzb.arch.domain.UseCase
import com.ivzb.arch.domain.internal.SyncScheduler
import com.ivzb.arch.model.Theme
import javax.inject.Inject

class GetAvailableThemesUseCase @Inject constructor() : UseCase<Unit, List<Theme>>() {

    init {
        taskScheduler = SyncScheduler
    }

    override fun execute(parameters: Unit): List<Theme> = when {
        BuildCompat.isAtLeastQ() -> {
            listOf(Theme.LIGHT, Theme.DARK, Theme.SYSTEM)
        }
        else -> {
            listOf(Theme.LIGHT, Theme.DARK, Theme.BATTERY_SAVER)
        }
    }
}
