package com.ivzb.arch.analytics

import android.app.Activity

/** Analytics API surface */
interface AnalyticsHelper {

    /** Record a screen view */
    fun sendScreenView(screenName: String, activity: Activity)

    /** Record a UI event, e.g. user clicks a button */
    fun logUiEvent(action: String)
}

/** Actions that should be used when sending analytics events */
object AnalyticsActions {

    const val ADD_LINK_CLIPBOARD = "add_link_manually_clipboard"
    const val ADD_LINK_MANUALLY = "add_link_manually_manually"
    const val ADD_LINK_SHARE = "add_link_share"
    const val ADD_LINK_EMPTY_CLIPBOARD = "add_link_empty_clipboard"

    const val EDIT_LINK_CLIPBOARD = "edit_link_manually_clipboard"
    const val EDIT_LINK_MANUALLY = "edit_link_manually_manually"
    const val EDIT_LINK_EMPTY_CLIPBOARD = "edit_link_empty_clipboard"

    const val LINK_COPY = "link_copy"
    const val LINK_SHARE = "link_share"
    const val LINK_DELETE = "link_delete"
    const val LINK_VISIT = "link_visit"

    const val HOME_TO_DETAILS = "home_to_details"
    const val HOME_TO_OPTIONS = "home_to_options"
    const val HOME_TO_SEARCH = "home_to_search"
    const val HOME_TO_ADD = "home_to_add"

    const val ADD_TO_EDIT = "add_to_edit"
}

/** Screens that should be used when opening screens */
object AnalyticsScreens {

    const val HOME = "screen_home"
    const val SEARCH = "screen_search"
    const val OPTIONS = "screen_options"
    const val DETAILS = "screen_details"
    const val ADD = "screen_add"
    const val EDIT = "screen_edit"
    const val SETTINGS = "screen_settings"
}
