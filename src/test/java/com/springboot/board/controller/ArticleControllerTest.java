package com.springboot.board.controller;

import com.springboot.board.config.SecurityConfig;
import com.springboot.board.dto.ArticleWithCommentsDto;
import com.springboot.board.dto.UserAccountDto;
import com.springboot.board.service.ArticleService;
import com.springboot.board.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleController.class)  // 해당 클래스만 읽어들여서 테스트 한다.
class ArticleControllerTest {

    private final MockMvc mvc;

    // ArticleService를 배제하여 API 입출력만 확인한다.
    @MockBean private ArticleService articleService;
    @MockBean private PaginationService paginationService;

    // 생성자 주입 방식
    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    @DisplayName("[view] [GET] 게시글 리스트 (게시판) 페이지 - 정상호출")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnArticlesView() throws Exception {
        // Given
        // any와 eq는 매치를 해준다. any는 어떤 Page든 상관 없고, eq는 무조건 null 로만 매치를 했다.
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));
        // When & Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk()) // 상태가 OK인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))  // 미디어 타입 지정 (화면이니 TEXT_HTML)
                .andExpect(view().name("articles/index"))  // 매핑 될 View의 HTML에 대한 이름 테스트
                .andExpect(model().attributeExists("articles"))  // 데이터가 있는지 확인
                .andExpect(model().attributeExists("paginationBarNumbers")); // 데이터가 있는지 확인
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    void givenPagingAndSortingParams_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);
        given(articleService.searchArticles(null, null, pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // When & Then
        mvc.perform(
                        get("/articles")
                                .queryParam("page", String.valueOf(pageNumber))
                                .queryParam("size", String.valueOf(pageSize))
                                .queryParam("sort", sortName + "," + direction)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers));
        then(articleService).should().searchArticles(null, null, pageable);
        then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
    }

    @DisplayName("[view] [GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnArticleView() throws Exception {
        // Given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentDto());
        // When & Then
        mvc.perform(get("/articles/" + articleId))
                .andExpect(status().isOk()) // 상태가 OK인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))  // 미디어 타입 지정 (화면이니 TEXT_HTML)
                .andExpect(view().name("articles/detail"))  // 매핑 될 View의 HTML에 대한 이름 테스트
                .andExpect(model().attributeExists("article")) // 데이터가 있는지 확인
                .andExpect(model().attributeExists("articleComments")); // 데이터가 있는지 확인
        then(articleService).should().getArticle(articleId);
    }

    @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleSearchView_thenReturnArticleSearchView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk()) // 상태가 OK인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))  // 미디어 타입 지정 (화면이니 TEXT_HTML)
                .andExpect(view().name("article/search")); // 매핑 될 View의 HTML에 대한 이름 테스트
    }

    @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 해시태그 검색 페이지 - 정상 호출")
    @Test
    public void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnArticleHashtagSearchView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk()) // 상태가 OK인지
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))  // 미디어 타입 지정 (화면이니 TEXT_HTML)
                .andExpect(view().name("article/search-hashtag")); // 매핑 될 View의 HTML에 대한 이름 테스트
    }

    private ArticleWithCommentsDto createArticleWithCommentDto(){
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "hun",
                LocalDateTime.now(),
                "hun"
        );
    }

    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "hun",
                "pw",
                "hun@mail.com",
                "Hun",
                "This is memo",
                LocalDateTime.now(),
                "hun",
                LocalDateTime.now(),
                "hun"
        );
    }

}
