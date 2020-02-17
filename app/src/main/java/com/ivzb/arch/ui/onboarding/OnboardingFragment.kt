package com.ivzb.arch.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import com.ivzb.arch.R
import com.ivzb.arch.databinding.FragmentOnboardingBinding
import com.ivzb.arch.domain.EventObserver
import com.ivzb.arch.ui.main.MainActivity
import com.ivzb.arch.util.viewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * Contains the pages of the onboarding experience and responds to [OnboardingViewModel] events.
 */
class OnboardingFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var onboardingViewModel: OnboardingViewModel

    private lateinit var binding: FragmentOnboardingBinding

    private lateinit var pagerPager: ViewPagerPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        onboardingViewModel = viewModelProvider(viewModelFactory)

        val adapter = OnboardingAdapter(childFragmentManager)

        binding = FragmentOnboardingBinding.inflate(inflater, container, false).apply {
            viewModel = onboardingViewModel
            lifecycleOwner = viewLifecycleOwner
            pager.adapter = adapter
            pagerPager = ViewPagerPager(pager)
        }

        onboardingViewModel.navigateToMainActivity.observe(this, EventObserver {
            if (adapter lastItemIs binding.pager.currentItem) {
                onboardingViewModel.completeOnboarding()

                requireActivity().run {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }

            binding.pager.currentItem = binding.pager.currentItem + 1
        })

        return binding.root
    }
}

class OnboardingAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val fragments = arrayOf(
        OnboardingPagerFragment.newInstance(
            R.string.onboarding_welcome_title,
            R.string.onboarding_welcome_description,
            R.drawable.welcome
        ),

        OnboardingPagerFragment.newInstance(
            R.string.onboarding_share_title,
            R.string.onboarding_share_description,
            R.drawable.welcome
        ),

        OnboardingPagerFragment.newInstance(
            R.string.onboarding_safe_title,
            R.string.onboarding_safe_description,
            R.drawable.welcome
        )
    )

    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    infix fun lastItemIs(position: Int?) = position == fragments.size - 1
}
