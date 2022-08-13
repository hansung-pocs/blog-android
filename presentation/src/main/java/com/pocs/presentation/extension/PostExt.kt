package com.pocs.presentation.extension

import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.post.PostFilterType
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

val PostFilterType.koreanStringResource: Int
    get() {
        return when (this) {
            PostFilterType.ALL -> R.string.all
            PostFilterType.BEST -> R.string.best
            PostFilterType.NOTICE -> R.string.notice
            PostFilterType.STUDY -> R.string.study
            PostFilterType.MEMORY -> R.string.memory
            PostFilterType.KNOWHOW -> R.string.knowhow
            PostFilterType.REFERENCE -> R.string.reference
        }
    }