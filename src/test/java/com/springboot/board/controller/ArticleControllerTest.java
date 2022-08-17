package com.springboot.board.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@DisplayName("View Controller - Article")
@WebMvcTest(ArticleController.class)  // 해당 클래스만 읽어들여서 테스트 한다.
class ArticleControllerTest {

    private final MockMvc mvc;

    // 생성자 주입 방식
    public ArticleControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[view] [GET] Articles List (article) page")
    @Test
    public void givenNothing_whenRequestingArticlesView_thenReturnArticlesView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk()) // 상태가 OK인지
                .andExpect(content().contentType(MediaType.TEXT_HTML))  // 미디어 타입 지정 (화면이니 TEXT_HTML)
                .andExpect(model().attributeExists("articles")); // 데이터가 있는지 확인
    }

    @DisplayName("[view] [GET] Article details page")
    @Test
    public void givenNothing_whenRequestingArticleView_thenReturnArticleView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk()) // 상태가 OK인지
                .andExpect(content().contentType(MediaType.TEXT_HTML))  // 미디어 타입 지정 (화면이니 TEXT_HTML)
                .andExpect(model().attributeExists("article")); // 데이터가 있는지 확인
    }

    @DisplayName("[view] [GET] Article search page")
    @Test
    public void givenNothing_whenRequestingArticleSearchView_thenReturnArticleSearchView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk()) // 상태가 OK인지
                .andExpect(content().contentType(MediaType.TEXT_HTML));  // 미디어 타입 지정 (화면이니 TEXT_HTML)
    }

    @DisplayName("[view] [GET] Article hashtag search page")
    @Test
    public void givenNothing_whenRequestingArticleHashtagSearchView_thenReturnArticleHashtagSearchView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk()) // 상태가 OK인지
                .andExpect(content().contentType(MediaType.TEXT_HTML));  // 미디어 타입 지정 (화면이니 TEXT_HTML)
    }

}
