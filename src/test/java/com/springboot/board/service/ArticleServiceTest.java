package com.springboot.board.service;

import com.springboot.board.domain.Article;
import com.springboot.board.domain.type.SearchType;
import com.springboot.board.dto.ArticleDto;
import com.springboot.board.dto.ArticleUpdateDto;
import com.springboot.board.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 -> 게시글")
@ExtendWith(MockitoExtension.class)  // 목키토 사용
class ArticleServiceTest {

    // Mock을 주입하는 대상은 InjectMocks 어노테이션
    @InjectMocks private ArticleService sut;

    // 그외 나머지 목은 Mock 어노테이션
    @Mock private ArticleRepository articleRepository;

    @DisplayName("게시글을 검색하면, 게시글 리스트를 반환한다.")
    @Test
    void givenSearchParameters_whenSearchingArticles_thenReturnsArticleList(){
        // Given

        // When
            Page<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "search keyword");  // 제목, 본문, ID, 닉네임, 해쉬태그
        //Then
        assertThat(articles).isNotNull();
    }

    @DisplayName("게시글을 조회하면, 게시글을 반환한다.")
    @Test
    void givenId_whenSearchingArticle_thenReturnsArticle(){
        // Given

        // When
        ArticleDto article = sut.searchArticle(1L);
        //Then
        assertThat(article).isNotNull();
    }

    @DisplayName("게시글을 정보를 입력하면, 게시글을 생성한다.")
    @Test
    void givenArticleInfo_whenSavingArticle_thenSavesArticle(){
        // Given
       given(articleRepository.save(any(Article.class))).willReturn(null);  // 무슨일이 일어날지 명시
        // When
        sut.saveArticle(ArticleDto.of(LocalDateTime.now(), "Hun", "title", "content", "#java"));
        //Then
        then(articleRepository).should().save(any(Article.class));  // 세이브를 호출 했는가 검사
    }

    @DisplayName("게시글을의 ID와 수정 정보를 입력하면, 게시글을 수정한다.")
    @Test
    void givenArticleIdAndModifiedInfo_whenUpdatingArticle_thenUpdatesArticle(){
        // Given
        given(articleRepository.save(any(Article.class))).willReturn(null);  // 무슨일이 일어날지 명시
        // When
        sut.updateArticle(1L, ArticleUpdateDto.of("title", "content", "#java"));
        //Then
        then(articleRepository).should().save(any(Article.class));  // 세이브를 호출 했는가 검사
    }

    @DisplayName("게시글을의 ID를 입력하면, 게시글을 삭제한다.")
    @Test
    void givenArticleId_whenDeletingArticle_thenDeletesArticle(){
        // Given
        willDoNothing().given(articleRepository).delete(any(Article.class));  // 무슨일이 일어날지 명시
        // When
        sut.deleteArticle(1L);
        //Then
        then(articleRepository).should().delete(any(Article.class));  // delete 를 호출 했는가 검사
    }

}