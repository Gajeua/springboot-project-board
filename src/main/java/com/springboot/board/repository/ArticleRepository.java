package com.springboot.board.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import com.springboot.board.domain.Article;
import com.springboot.board.domain.QArticle;
import com.springboot.board.repository.querydsl.ArticleRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        ArticleRepositoryCustom,
        QuerydslPredicateExecutor<Article>, // 기본적으로 엔티티 안에있는 모든 필드에 대한 기본 검색기능을 추가한다. 기본적인 검색기능이라 Like 검색이나 대소문자 구분이 안된다.
        QuerydslBinderCustomizer<QArticle> { // 여기에는 Q클래스 엔티티로 넣어준다. 커스터마이즈를 오버라이딩해서 원하는 검색 기능으로 수정할 수 있다.

    // Containing을 사용하면 부분 검색 가능
    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
    Page<Article> findByHashtag(String hashtag, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QArticle root){
        bindings.excludeUnlistedProperties(true); // 리스트로 되어있지 않은 프로퍼티는 검색 제외
        bindings.including(root.title, root.content, root.hashtag, root.createdAt, root.createdBy); // 검색 가능하게 할 요소들 넣기
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);  // like '%${value}%' 로 구성되어 검색한다.
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq);
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}
