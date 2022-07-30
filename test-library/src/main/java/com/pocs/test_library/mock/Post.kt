package com.pocs.test_library.mock

import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.post.PostDetail

val mockPostDetail1 = PostDetail(1, "", mockUser1, "", "", "", PostCategory.NOTICE)
val mockPostDetail2 = PostDetail(2, "hello", mockUser1, "hi", "", "", PostCategory.NOTICE)