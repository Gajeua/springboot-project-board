package com.springboot.board.service;

import com.springboot.board.domain.Article;
import com.springboot.board.domain.ArticleComment;
import com.springboot.board.dto.ArticleCommentDto;
import com.springboot.board.repository.ArticleCommentRepository;
import com.springboot.board.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 -> 댓글")
@ExtendWith(MockitoExtension.class)  // 목키토 사용
class ArticleCommentServiceTest {

    // Mock을 주입하는 대상은 InjectMocks 어노테이션
    @InjectMocks
    private ArticleCommentService sut;

    @Mock private ArticleCommentRepository articleCommentRepository;
    @Mock private ArticleRepository articleRepository;

    @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments(){
        // Given
        Long articleId = 1L;
        given(articleRepository.findById(articleId)).willReturn(Optional.of(Article.of("title", "content", "#java")));
        // When
        List<ArticleCommentDto> articleComments = sut.searchArticleComment(articleId);
        // Then
        assertThat(articleComments).isNotNull();
        then(articleRepository).should().findById(articleId);
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
        // Given
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);
        // When
        sut.saveArticleComment(ArticleCommentDto.of(LocalDateTime.now(), "Hun", LocalDateTime.now(), "Hun", "comment"));
        // Then
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

}
