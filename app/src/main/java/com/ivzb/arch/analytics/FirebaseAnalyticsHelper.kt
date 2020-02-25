package com.ivzb.arch.analytics

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.ivzb.arch.BuildConfig

/**
 * Firebase Analytics implementation of AnalyticsHelper
 */
class FirebaseAnalyticsHelper(
    context: Context
) : AnalyticsHelper {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun sendScreenView(screenName: String, activity: Activity) {
        if (BuildConfig.DEBUG) {
            return
        }

        val params = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, screenName)
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, FA_CONTENT_TYPE_SCREENVIEW)
        }

        firebaseAnalytics.run {
            setCurrentScreen(activity, screenName, null)
            logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params)
        }
    }

    override fun logUiEvent(action: String) {
        if (BuildConfig.DEBUG) {
            return
        }

        val params = Bundle().apply {
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, FA_CONTENT_TYPE_UI_EVENT)
            putString(FA_KEY_UI_ACTION, action)
        }

        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, params)
    }

    companion object {
        /**
         * Log a specific screen view under the `screenName` string.
         */
        private const val FA_CONTENT_TYPE_SCREENVIEW = "screen"
        private const val FA_KEY_UI_ACTION = "ui_action"
        private const val FA_CONTENT_TYPE_UI_EVENT = "ui event"
    }
}
