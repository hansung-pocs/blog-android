package com.pocs.domain.model.post

enum class PostCategory(val canAnonymousSee: Boolean = false) {
    NOTICE,
    STUDY,
    MEMORY,
    KNOWHOW,
    REFERENCE,
    QNA(canAnonymousSee = true),
}