package com.nraliim.storyapp.ui.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.nraliim.storyapp.R
import com.nraliim.storyapp.databinding.ActivityDetailStoryBinding
import com.nraliim.storyapp.response.StoryItem
import com.nraliim.storyapp.utils.Utils
import com.nraliim.storyapp.viewmodel.DetailStoryViewModel
import java.util.*

class DetailStoryActivity : AppCompatActivity() {

    private lateinit var story: StoryItem
    private lateinit var detailBinding: ActivityDetailStoryBinding

    private val viewModel: DetailStoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detailBinding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(detailBinding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        story = intent.getParcelableExtra(EXTRA_STORY)!!
        viewModel.setDetailStory(story)
        showDetailStory()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showDetailStory() {
        with(detailBinding) {
            tvName.text = viewModel.storyItem.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                tvCreatedTime.text = getString(
                    R.string.created_add, Utils.formatDate(
                        viewModel.storyItem.createdAt,
                        TimeZone.getDefault().id
                    )
                )
            }
            tvDescription.text = viewModel.storyItem.description

            Glide.with(ivStory)
                .load(viewModel.storyItem.photoUrl) // URL Avatar
                .placeholder(R.drawable.ic_image)
                .error(R.drawable.ic_broken_image)
                .into(ivStory)
        }
    }

    companion object {
        const val EXTRA_STORY = "story"
    }
}