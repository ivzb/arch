package com.ivzb.arch.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import com.ivzb.arch.databinding.FragmentOnboardingPagerBinding
import com.ivzb.arch.util.activityViewModelProvider
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class OnboardingPagerFragment : DaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentOnboardingPagerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingPagerBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }

        requireArguments().run {
            binding.tvTitle.setText(getInt(EXTRA_TITLE_ID))
            binding.tvDescription.setText(getInt(EXTRA_DESCRIPTION_ID))
            binding.ivImage.setImageResource(getInt(EXTRA_IMAGE_ID))
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.activityViewModel = activityViewModelProvider(viewModelFactory)
    }

    companion object {

        private val EXTRA_TITLE_ID = "extra_title_id"
        private val EXTRA_DESCRIPTION_ID = "extra_description_id"
        private val EXTRA_IMAGE_ID = "extra_image_id"

        internal fun newInstance(
            @StringRes titleResId: Int,
            @StringRes descriptionResId: Int,
            @DrawableRes imageResId: Int
        ) =
            OnboardingPagerFragment().apply {
                arguments = Bundle().apply {
                    putInt(EXTRA_TITLE_ID, titleResId)
                    putInt(EXTRA_DESCRIPTION_ID, descriptionResId)
                    putInt(EXTRA_IMAGE_ID, imageResId)
                }
            }
    }
}
