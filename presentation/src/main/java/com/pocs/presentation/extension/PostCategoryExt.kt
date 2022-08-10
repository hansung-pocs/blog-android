package com.pocs.presentation.extension

import com.pocs.domain.model.post.PostCategory
import com.pocs.presentation.R

val PostCategory.koreanStringResource: Int
    get() {
        return when (this) {
            PostCategory.NOTICE -> R.string.notice
            PostCategory.STUDY -> R.string.study
            PostCategory.MEMORY -> R.string.memory
            PostCategory.KNOWHOW -> R.string.knowhow
            PostCategory.REFERENCE -> R.string.reference
        }
    }