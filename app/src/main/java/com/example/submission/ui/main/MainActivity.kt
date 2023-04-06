package com.example.submission.ui.main

import com.example.submission.R
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvUsers: RecyclerView
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupUI()
        setupProgressBar()
        setupErrorPage()
        setupRecyclerView()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.search)?.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.searchView_query_hint)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    viewModel.searchUsers(query)
                }

                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        val searchViewMenuItem: MenuItem = menu.findItem(R.id.search)
        searchViewMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                if (viewModel.searchQuery.value?.isNotEmpty() == true) {
                    binding.textViewSearchInfo.visibility = View.VISIBLE
                }
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                binding.textViewSearchInfo.visibility = View.GONE
                return true
            }
        })

        observeData()

        return super.onCreateOptionsMenu(menu)
    }

    private fun setupErrorPage() {
        viewModel.isError.observe(this) { isError ->
            with(binding) {
                content.isVisible = !isError
                layoutNoResult.textViewEmpty.isVisible = !isError
                layoutNoResult.textViewError.isVisible = isError
            }
        }
    }

    private fun observeData() {
        viewModel.users.observe(this) { users ->
            if (users.isEmpty()) {
                with(binding) {
                    layoutNoResult.textViewEmpty.visibility = View.VISIBLE
                    binding.content.isVisible = false
                }
            } else {
                if (searchView.query.isEmpty()) {
                    binding.textViewSearchInfo.visibility = View.GONE
                } else {
                    binding.textViewSearchInfo.visibility = View.VISIBLE
                }

                binding.layoutNoResult.textViewEmpty.visibility = View.GONE
                val searchInfoText =
                    "${users.size} users found named '${if (searchView.query.isEmpty()) "Jim" else searchView.query}'"
                viewModel._searchQuery.value = searchInfoText
                binding.textViewSearchInfo.text = viewModel._searchQuery.value

                rvUsers.adapter = ListUserAdapter(users)
            }
        }
    }

    private fun setupUI() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.textViewSearchInfo.text = viewModel._searchQuery.value
    }

    private fun setupRecyclerView() {
        rvUsers = binding.recyclerViewUsers

        rvUsers.apply {
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setupProgressBar() {
        viewModel.isLoading.observe(this) { isLoading ->
            with(binding) {
                content.isVisible = !isLoading
                progressBar.isVisible = isLoading
            }
        }
    }
}