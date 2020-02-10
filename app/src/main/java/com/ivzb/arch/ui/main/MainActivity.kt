package com.ivzb.arch.ui.main

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.core.view.updatePadding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.ivzb.arch.R
import com.ivzb.arch.databinding.NavigationHeaderBinding
import com.ivzb.arch.util.*
import com.ivzb.arch.widget.HashtagArchDecoration
import com.ivzb.arch.widget.NavigationBarContentFrameLayout
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), NavigationHost {

    companion object {
        /** Key for an int extra defining the initial navigation target. */
        const val EXTRA_NAVIGATION_ID = "extra.NAVIGATION_ID"

        private const val NAV_ID_NONE = -1

        private val TOP_LEVEL_DESTINATIONS = setOf(
            R.id.navigation_feed
        )
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: MainActivityViewModel

    private lateinit var content: FrameLayout
    private lateinit var drawer: DrawerLayout
    private lateinit var navigation: NavigationView
    private lateinit var navHeaderBinding: NavigationHeaderBinding
    private lateinit var navController: NavController
    private var navHostFragment: NavHostFragment? = null

    private lateinit var statusScrim: View

    private var currentNavId = NAV_ID_NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = viewModelProvider(viewModelFactory)
        // Update for Dark Mode straight away
//        updateForTheme(viewModel.currentTheme)

        setContentView(R.layout.activity_main)

        val drawerContainer: NavigationBarContentFrameLayout = findViewById(R.id.drawer_container)
        // Let's consume any
        drawerContainer.setOnApplyWindowInsetsListener { v, insets ->
            // Let the view draw it's navigation bar divider
            v.onApplyWindowInsets(insets)

            // Consume any horizontal insets and pad all content in. There's not much we can do
            // with horizontal insets
            v.updatePadding(
                left = insets.systemWindowInsetLeft,
                right = insets.systemWindowInsetRight
            )
            insets.replaceSystemWindowInsets(
                0, insets.systemWindowInsetTop,
                0, insets.systemWindowInsetBottom
            )
        }

        content = findViewById(R.id.content_container)
        content.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        // Make the content ViewGroup ignore insets so that it does not use the default padding
        content.setOnApplyWindowInsetsListener(NoopWindowInsetsListener)

        statusScrim = findViewById(R.id.status_bar_scrim)
        statusScrim.setOnApplyWindowInsetsListener(HeightTopWindowInsetsListener)

        drawer = findViewById(R.id.drawer)

        navHeaderBinding = NavigationHeaderBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@MainActivity
        }

        navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

        navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentNavId = destination.id
            val isTopLevelDestination = TOP_LEVEL_DESTINATIONS.contains(destination.id)
            val lockMode = if (isTopLevelDestination) {
                DrawerLayout.LOCK_MODE_UNLOCKED
            } else {
                DrawerLayout.LOCK_MODE_LOCKED_CLOSED
            }
            drawer.setDrawerLockMode(lockMode)
        }

        navigation = findViewById(R.id.navigation)
        navigation.apply {
            // Add the #io19 decoration
            val menuView = findViewById<RecyclerView>(R.id.design_navigation_view)?.apply {
                addItemDecoration(HashtagArchDecoration(context))
            }
            // Update the Navigation header view to pad itself down
            navHeaderBinding.root.doOnApplyWindowInsets { v, insets, padding ->
                v.updatePadding(top = padding.top + insets.systemWindowInsetTop)
                // NavigationView doesn't dispatch insets to the menu view, so pad the bottom here.
                menuView?.updatePadding(bottom = insets.systemWindowInsetBottom)
            }
            addHeaderView(navHeaderBinding.root)

            itemBackground = navigationItemBackground(context)

            setupWithNavController(navController)
        }

        if (savedInstanceState == null) {
            // default to showing Home
            val initialNavId = intent.getIntExtra(EXTRA_NAVIGATION_ID, R.id.navigation_feed)
            navigation.setCheckedItem(initialNavId) // doesn't trigger listener
            navigateTo(initialNavId)
        }

//        viewModel.theme.observe(this, Observer(::updateForTheme))
    }

    override fun registerToolbarWithNavigation(toolbar: Toolbar) {
        val appBarConfiguration = AppBarConfiguration(TOP_LEVEL_DESTINATIONS, drawer)
        toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        currentNavId = navigation.checkedItem?.itemId ?: NAV_ID_NONE
    }

    override fun onBackPressed() {
        /**
         * If the drawer is open, the behavior changes based on the API level.
         * When gesture nav is enabled (Q+), we want back to exit when the drawer is open.
         * When button navigation is enabled (on Q or pre-Q) we want to close the drawer on back.
         */
        if (drawer.isDrawerOpen(navigation) && drawer.shouldCloseDrawerFromBackPress()) {
            closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    private fun closeDrawer() {
        drawer.closeDrawer(GravityCompat.START)
    }

    override fun onUserInteraction() {
        super.onUserInteraction()
        getCurrentFragment()?.onUserInteraction()
    }

    private fun getCurrentFragment(): MainNavigationFragment? {
        return navHostFragment
            ?.childFragmentManager
            ?.primaryNavigationFragment as? MainNavigationFragment
    }

    private fun navigateTo(navId: Int) {
        if (navId == currentNavId) {
            return // user tapped the current item
        }
        navController.navigate(navId)
    }
}
