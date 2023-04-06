package com.example.submission.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.submission.R
import com.example.submission.databinding.ActivityDetailBinding
import com.example.submission.shared.Constants.Companion.GLIDE_HEIGHT_DETAIL_ACTIVITY
import com.example.submission.shared.Constants.Companion.GLIDE_WIDTH_DETAIL_ACTIVITY
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private val viewModel: DetailViewModel by viewModels()
    private lateinit var binding: ActivityDetailBinding
    private var username: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUI()
        setupProgressBar()
        setupFollowPager()
        setupErrorPage()

        if (savedInstanceState === null) {
            fetchData()
        }

        observeData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupErrorPage() {
        viewModel.isError.observe(this) { isError ->
            with (binding) {
                content.isVisible = !isError
                layoutNoResult.textViewError.isVisible = isError
            }
        }
    }

    private fun setupFollowPager() {
        val followPagerAdapter = FollowPagerAdapter(this)
        username?.let {
            followPagerAdapter.username = it
        }

        val viewPager: ViewPager2 = binding.viewPagerFollow
        viewPager.adapter = followPagerAdapter

        val tabs: TabLayout = binding.tabLayoutFollow
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun fetchData() {
        username?.let { viewModel.getUserDetail(it) }
    }

    private fun observeData() {
        viewModel.userDetail.observe(this) {
            val (bio, publicRepos, followers, avatarUrl, following, name, location) = it

            Glide.with(this)
                .load(avatarUrl)
                .override(GLIDE_WIDTH_DETAIL_ACTIVITY, GLIDE_HEIGHT_DETAIL_ACTIVITY)
                .circleCrop()
                .into(binding.imageViewAvatar)
            binding.textViewName.text = name
            location?.let {
                val locationText = "📍 $location"
                binding.textViewLocation.text = locationText
            }
            if (bio == null) {
                binding.textViewBio.visibility = View.GONE
            } else {
                binding.textViewBio.text = bio
            }
            binding.textViewRepos.text = publicRepos.toString()
            binding.textViewFollowersNumber.text = followers.toString()
            binding.textViewFollowingNumber.text = following.toString()
        }
    }

    private fun setupUI() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        username = intent.getStringExtra(EXTRA_USERNAME)
        supportActionBar?.title = username
    }

    private fun setupProgressBar() {
        viewModel.isLoading.observe(this) { isLoading ->
            with (binding) {
                content.isVisible = !isLoading
                progressBar.isVisible = isLoading
            }
        }
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers_tab_text,
            R.string.following_tab_text,
        )
    }
}