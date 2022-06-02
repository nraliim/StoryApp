package com.nraliim.storyapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.nraliim.storyapp.R
import com.nraliim.storyapp.adapter.StoriesAdapter
import com.nraliim.storyapp.databinding.ActivityListStoryBinding
import com.nraliim.storyapp.model.User
import com.nraliim.storyapp.viewmodel.ListStoryViewModel

class ListStoryActivity : AppCompatActivity() {

    private lateinit var storyBinding: ActivityListStoryBinding
    private lateinit var user: User
    private lateinit var adapter: StoriesAdapter

    private val viewModel by viewModels<ListStoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        storyBinding = ActivityListStoryBinding.inflate(layoutInflater)
        setContentView(storyBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        user = intent.getParcelableExtra(EXTRA_USER)!!

        setListStory()
        adapter = StoriesAdapter()

        setRecyclerView()
        addStoryAction()

        showSnackBar()
        showLoading()
        showData()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showSnackBar() {
        viewModel.snackBarText.observe(this) {
            it.getContentIfNotHandled()?.let { snackBarText ->
                Snackbar.make(
                    findViewById(R.id.rv_list_story),
                    snackBarText,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setRecyclerView() {
        storyBinding.rvListStory.layoutManager = LinearLayoutManager(this)
        storyBinding.rvListStory.setHasFixedSize(true)
        storyBinding.rvListStory.adapter = adapter
    }


    private fun showLoading() {
        viewModel.isLoading.observe(this) {
            storyBinding.apply {
                if (it) {
                    progressBar.visibility = View.VISIBLE
                    rvListStory.visibility = View.INVISIBLE
                } else {
                    progressBar.visibility = View.GONE
                    rvListStory.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun showData() {
        viewModel.isAvailable.observe(this) {
            storyBinding.apply {
                if (it) {
                    rvListStory.visibility = View.VISIBLE
                } else {
                    rvListStory.visibility = View.GONE
                }
            }
        }
    }

    private fun setListStory() {
        viewModel.showListStory(user.token)
        viewModel.itemStory.observe(this) {
            adapter.setListStory(it)
        }
    }

    private fun addStoryAction() {
        storyBinding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            intent.putExtra(AddStoryActivity.EXTRA_USER, user)
            startActivity(intent)
        }
    }

    companion object {
        const val EXTRA_USER = "user"
    }
}