package com.nraliim.storyapp.utils

import androidx.recyclerview.widget.DiffUtil
import com.nraliim.storyapp.response.StoryItem

class DiffCallback(
    private val mOldStoryList: List<StoryItem>,
    private val mNewStoryList: List<StoryItem>
) : DiffUtil.Callback() {

    override fun getOldListSize() = mOldStoryList.size

    override fun getNewListSize() = mNewStoryList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        mOldStoryList[oldItemPosition].id == mNewStoryList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldStoryItem = mOldStoryList[oldItemPosition]
        val newStoryItem = mNewStoryList[newItemPosition]
        return oldStoryItem.id == newStoryItem.id
    }
}