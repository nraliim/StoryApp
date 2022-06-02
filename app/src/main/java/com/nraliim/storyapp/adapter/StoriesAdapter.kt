package com.nraliim.storyapp.adapter

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nraliim.storyapp.R
import com.nraliim.storyapp.databinding.ItemListStoryBinding
import com.nraliim.storyapp.response.StoryItem
import com.nraliim.storyapp.ui.activities.DetailStoryActivity
import com.nraliim.storyapp.utils.DiffCallback
import com.nraliim.storyapp.utils.Utils
import java.util.*

class StoriesAdapter : RecyclerView.Adapter<StoriesAdapter.ViewHolder>() {

    private val listStory = ArrayList<StoryItem>()

    fun setListStory(itemStory: List<StoryItem>) {
        val diffCallback = DiffCallback(this.listStory, itemStory)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.listStory.clear()
        this.listStory.addAll(itemStory)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListStoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listStory[position])
    }

    override fun getItemCount() = listStory.size

    inner class ViewHolder(private var binding: ItemListStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(story: StoryItem) {
            with(binding) {
                Glide.with(itemView)
                    .load(story.photoUrl) // URL Avatar
                    .placeholder(R.drawable.ic_image)
                    .error(R.drawable.ic_broken_image)
                    .into(imgItem)
                tvName.text = story.name
                tvDescription.text = story.description
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    tvCreatedTime.text =
                        binding.root.resources.getString(
                            R.string.created_add,
                            Utils.formatDate(story.createdAt, TimeZone.getDefault().id)
                        )
                }

                // image OnClickListener
                itemView.setOnClickListener {
                    val intent = Intent(it.context, DetailStoryActivity::class.java)
                    intent.putExtra(DetailStoryActivity.EXTRA_STORY, story)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(imgItem, "photo"),
                            Pair(tvName, "name"),
                            Pair(tvDescription, "description"),
                            Pair(tvCreatedTime, "created")
                        )

                    it.context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

}