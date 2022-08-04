package com.pocs.test_library.mock

import com.pocs.domain.model.post.Post
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.post.PostDetail

val mockPostDetail1 = PostDetail(1, "", mockPostWriter1, "", "2022-08-03", "", PostCategory.NOTICE)
val mockPostDetail2 = PostDetail(2, "hello", mockPostWriter1, "hi", "2022-08-03", "", PostCategory.NOTICE)

val mockPost = Post(2, "hello", "hi", "hello", "2022-08-03", "", "", PostCategory.NOTICE)