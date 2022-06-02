package com.nraliim.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.nraliim.storyapp.response.StoryItem

class DetailStoryViewModel : ViewModel() {
    lateinit var storyItem: StoryItem

    fun setDetailStory(story: StoryItem): StoryItem {
        storyItem = story
        return storyItem
    }
}