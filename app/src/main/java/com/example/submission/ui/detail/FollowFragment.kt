package com.example.submission.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission.databinding.FragmentFollowBinding
import com.example.submission.ui.main.ListUserAdapter
import kotlin.properties.Delegates

class FollowFragment : Fragment() {
    private val viewModel: FollowViewModel by viewModels()
    private lateinit var binding: FragmentFollowBinding
    private lateinit var rvUsers: RecyclerView

    private var index by Delegates.notNull<Int>()
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupProgressBar()
        setupRecyclerView()
        setupErrorPage()

        if (savedInstanceState === null) {
            fetchData()
        }

        observeData()
    }

    private fun setupErrorPage() {
        viewModel.isError.observe(requireActivity()) { isError ->
            with(binding) {
                content.isVisible = !isError
                layoutNoResult.textViewError.isVisible = isError
            }
        }
    }

    private fun fetchData() {
        when (index) {
            FOLLOWERS_INDEX -> viewModel.getUserFollowers(username)
            FOLLOWING_INDEX -> viewModel.getUserFollowing(username)
        }
    }

    private fun observeData() {
        viewModel.users.observe(viewLifecycleOwner) { users ->
            if (users.isEmpty()) {
                binding.layoutNoResult.textViewEmpty.visibility = View.VISIBLE
                binding.content.isVisible = false
            } else {
                binding.layoutNoResult.textViewEmpty.isVisible = false
                rvUsers.adapter = ListUserAdapter(users)
            }
        }
    }

    private fun setupUI() {
        index = arguments?.getInt(INDEX) ?: 0
        username = arguments?.getString(USERNAME).toString()
    }

    private fun setupRecyclerView() {
        rvUsers = binding.content

        rvUsers.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity())
        }
    }

    private fun setupProgressBar() {
        viewModel.isLoading.observe(requireActivity()) { isLoading ->
            binding.content.isVisible = !isLoading
            binding.progressBar.isVisible = isLoading
        }
    }

    companion object {
        const val INDEX = "index"
        const val USERNAME = "username"

        const val FOLLOWERS_INDEX = 0
        const val FOLLOWING_INDEX = 1
    }
}