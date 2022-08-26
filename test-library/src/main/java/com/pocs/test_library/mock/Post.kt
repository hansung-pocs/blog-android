package com.pocs.test_library.mock

import com.pocs.domain.model.post.Post
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.post.PostDetail

val mockPostDetail1 = PostDetail(1, "", mockPostWriter1, views = 1, "", "2022-08-03", null, PostCategory.NOTICE)
val mockPostDetail2 = PostDetail(
    2,
    "hello",
    mockPostWriter1,
    views = 2,
    true,
    "hi",
    "2022-08-03",
    null,
    PostCategory.NOTICE
)

val mockPost = Post(2, "hello", "hi", "hello", views = 1,"2022-08-03", null, "", PostCategory.NOTICE)