package com.pocs.test_library.mock

import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.post.PostDetail

val mockPostDetail1 = PostDetail(1, "", mockPostWriter1, "", "", "", PostCategory.NOTICE)
val mockPostDetail2 = PostDetail(2, "hello", mockPostWriter1, "hi", "", "", PostCategory.NOTICE)