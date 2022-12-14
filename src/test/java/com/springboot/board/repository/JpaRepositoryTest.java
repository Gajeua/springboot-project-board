package com.springboot.board.repository;

import com.springboot.board.config.JpaConfig;
import com.springboot.board.domain.Article;
import com.springboot.board.domain.UserAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JPA Connect Test")
@Import(JpaConfig.class) // 직접 만든 JpaConfig 클래스를 임포트 해서 적용
@DataJpaTest // 슬라이스 테스트
class JpaRepositoryTest {

    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final UserAccountRepository userAccountRepository;

    // 생성자 주입 패턴
    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository,
            @Autowired UserAccountRepository userAccountRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @DisplayName("select Test")
    @Test
    void givenTestData_whenSelecting_thenWorksFine(){
        // Given

        // When
        List<Article> articles = articleRepository.findAll();
        // Then
        assertThat(articles)
                .isNotNull()
                .hasSize(123);

    }

    @DisplayName("insert Test")
    @Test
    void givenTestData_whenInserting_thenWorksFine(){
        // Given
        long previousCount = articleRepository.count();
        UserAccount userAccount = userAccountRepository.save(UserAccount.of("uno", "pw", null, null, null));
        Article article = Article.of(userAccount, "new article", "new content", "#spring");
        // When
        articleRepository.save(article);
        // Then
        assertThat(articleRepository.count())
                .isEqualTo(previousCount + 1);

    }

    @DisplayName("update Test")
    @Test
    void givenTestData_whenUpdating_thenWorksFine(){
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updatedHashtag = "#springboot";
        article.setHashtag(updatedHashtag);
        // When
        Article savedArticle = articleRepository.save(article);
        // Then
        assertThat(savedArticle)
                .hasFieldOrPropertyWithValue("hashtag", updatedHashtag);

    }

    @DisplayName("delete Test")
    @Test
    void givenTestData_whenDeleting_thenWorksFine(){
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long previousArticleCount = articleRepository.count();
        // When
        articleRepository.delete(article);
        // Then
        assertThat(articleRepository.count())
                .isEqualTo(previousArticleCount -1);

    }

}
