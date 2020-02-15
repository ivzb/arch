package com.ivzb.arch.ui.main

import androidx.lifecycle.ViewModel
import com.ivzb.arch.ui.theme.ThemedActivityDelegate
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
    themedActivityDelegate: ThemedActivityDelegate
) : ViewModel(), ThemedActivityDelegate by themedActivityDelegate
