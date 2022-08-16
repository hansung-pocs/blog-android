package com.pocs.domain.usecase.comment

import com.pocs.domain.model.comment.Comment
import com.pocs.domain.model.comment.CommentWriter
import kotlinx.coroutines.delay
import javax.inject.Inject

// TODO: 임시 목업 객체임. API 완성되면 data 모듈단에서 구현후 이곳에 연결해야함.
class GetCommentsUseCase @Inject constructor() {

    suspend operator fun invoke(postId: Int): Result<List<Comment>> {
        delay(1000)
        val mockComment = Comment(
            id = 10,
            parentId = null,
            childrenCount = 0,
            postId = postId,
            writer = CommentWriter(
                userId = 1,
                name = "홍길동"
            ),
            content = "댓글 내용입니다.",
            createdAt = "2022-09-02 13:09",
            updatedAt = null
        )
        return Result.success(
            listOf(
                mockComment,
                mockComment.copy(id = 11, childrenCount = 1),
                mockComment.copy(id = 12, parentId = 11)
            )
        )
    }
}