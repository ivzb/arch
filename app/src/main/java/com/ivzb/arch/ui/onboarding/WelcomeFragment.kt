package com.ivzb.arch.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.ivzb.arch.databinding.FragmentOnboardingWelcomeBinding
import com.ivzb.arch.util.activityViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

/**
 * First page of onboarding showing a welcome message.
 */
class WelcomeFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentOnboardingWelcomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingWelcomeBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.activityViewModel = activityViewModelProvider(viewModelFactory)
    }
}
