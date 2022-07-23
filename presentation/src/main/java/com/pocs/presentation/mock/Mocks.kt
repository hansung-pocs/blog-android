package com.pocs.presentation.mock

import androidx.paging.PagingData
import com.pocs.domain.model.PostCategory
import com.pocs.presentation.model.PostDetailUiState
import com.pocs.presentation.model.PostItemUiState
import com.pocs.presentation.model.UserItemUiState

val mockArticleItemsPagingData = PagingData.from(
    listOf(
        PostItemUiState(1, "가나다라마", "작성자", "2022.07.19"),
        PostItemUiState(2, "가나다라마", "작성자", "2022.07.19"),
        PostItemUiState(3, "가나다라마", "작성자", "2022.07.19"),
        PostItemUiState(4, "가나다라마", "작성자", "2022.07.19"),
        PostItemUiState(5, "가나다라마", "작성자", "2022.07.19"),
    )
)

val mockPostDetailItems = listOf(
    PostDetailUiState(
        1,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.STUDY
    ),
    PostDetailUiState(
        2,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.STUDY
    ),
    PostDetailUiState(
        3,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.STUDY
    ),
    PostDetailUiState(
        4,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.STUDY
    ),
    PostDetailUiState(
        5,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.NOTICE
    ),
    PostDetailUiState(
        6,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.NOTICE
    ),
    PostDetailUiState(
        7,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.NOTICE
    ),
    PostDetailUiState(
        8,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.NOTICE
    ),
    PostDetailUiState(
        9,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.NOTICE
    ),
    PostDetailUiState(
        10,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.NOTICE
    ),
    PostDetailUiState(
        11,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.NOTICE
    ),
    PostDetailUiState(
        12,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.NOTICE
    ),
    PostDetailUiState(
        13,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.NOTICE
    ),
    PostDetailUiState(
        14,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.NOTICE
    ),
    PostDetailUiState(
        15,
        title = "가나다라마",
        writer = "작성자",
        date = "2022.07.19",
        content = "Material Design은 사용자 인터페이스 설계의 모범 사례를 지원하는 지침, 구성 요소 및 도구의 적응 가능한 시스템입니다. 오픈 소스 코드를 기반으로 하는 Material Design은 디자이너와 개발자 간의 협업을 간소화하고 팀이 아름다운 제품을 신속하게 제작할 수 있도록 지원합니다.",
        category = PostCategory.NOTICE
    ),
)

val mockNoticeItemsPagingData = PagingData.from(
    listOf(
        PostItemUiState(6, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(7, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(8, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(9, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(10, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(11, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(12, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(13, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(14, "가나다라마", "관리자", "2022.07.19"),
        PostItemUiState(15, "가나다라마", "관리자", "2022.07.19"),
    )
)

val mockUsersPagingData = PagingData.from(
    listOf(
        UserItemUiState("오인성", "1891065", 2),
        UserItemUiState("김민성", "1871034", 3),
        UserItemUiState("가가가", "1991066", 5),
        UserItemUiState("나나나", "1891067", 6),
        UserItemUiState("다다다", "1891068", 1),
        UserItemUiState("마마마", "1891069", 3),
        UserItemUiState("바바바", "1691010", 8),
        UserItemUiState("사사사", "1791011", 9),
        UserItemUiState("아아아", "1891012", 10),
    )
)