package com.ivzb.arch.data.prefs

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import androidx.annotation.WorkerThread
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ivzb.arch.model.Theme
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/**
 * Storage for app and user preferences.
 */
interface PreferenceStorage {
    var onboardingCompleted: Boolean
    var snackbarIsStopped: Boolean
    var observableSnackbarIsStopped: LiveData<Boolean>
    var selectedTheme: String?
}

/**
 * [PreferenceStorage] impl backed by [android.content.SharedPreferences].
 */
@Singleton
class SharedPreferenceStorage @Inject constructor(context: Context) : PreferenceStorage {

    private val prefs: Lazy<SharedPreferences> = lazy { // Lazy to prevent IO access to main thread.
        context.applicationContext.getSharedPreferences(
            PREFS_NAME, MODE_PRIVATE
        ).apply {
            registerOnSharedPreferenceChangeListener(changeListener)
        }
    }

    private val observableShowSnackbarResult = MutableLiveData<Boolean>()

    private val observableSelectedThemeResult = MutableLiveData<String>()

    private val changeListener = OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            PREF_DARK_MODE_ENABLED -> observableSelectedThemeResult.value = selectedTheme
        }
    }

    override var onboardingCompleted by BooleanPreference(prefs, PREF_ONBOARDING, false)

    override var snackbarIsStopped by BooleanPreference(prefs, PREF_SNACKBAR_IS_STOPPED, false)

    override var observableSnackbarIsStopped: LiveData<Boolean>
        get() {
            observableShowSnackbarResult.value = snackbarIsStopped
            return observableShowSnackbarResult
        }
        set(_) = throw IllegalAccessException("This property can't be changed")

    override var selectedTheme by StringPreference(
        prefs, PREF_DARK_MODE_ENABLED, Theme.SYSTEM.storageKey
    )

    companion object {
        const val PREFS_NAME = "arch"
        const val PREF_ONBOARDING = "pref_onboarding"
        const val PREF_SNACKBAR_IS_STOPPED = "pref_snackbar_is_stopped"
        const val PREF_DARK_MODE_ENABLED = "pref_dark_mode"
    }

    fun registerOnPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) {
        prefs.value.registerOnSharedPreferenceChangeListener(listener)
    }
}

class BooleanPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean {
        return preferences.value.getBoolean(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) {
        preferences.value.edit { putBoolean(name, value) }
    }
}

class StringPreference(
    private val preferences: Lazy<SharedPreferences>,
    private val name: String,
    private val defaultValue: String?
) : ReadWriteProperty<Any, String?> {

    @WorkerThread
    override fun getValue(thisRef: Any, property: KProperty<*>): String? {
        return preferences.value.getString(name, defaultValue)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) {
        preferences.value.edit { putString(name, value) }
    }
}