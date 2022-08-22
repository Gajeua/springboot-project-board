package com.springboot.board.service;

import com.springboot.board.domain.Article;
import com.springboot.board.domain.type.SearchType;
import com.springboot.board.dto.ArticleDto;
import com.springboot.board.dto.ArticleWithCommentsDto;
import com.springboot.board.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Slf4j // 롬복의 로깅하는 어노테이션
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String search_keyword, Pageable pageable) {
        if (search_keyword == null || search_keyword.isBlank()) { // 검색어가 있는지 확인
            return articleRepository.findAll(pageable).map(ArticleDto::from); // Page에 map 메서드를 사용하여 ArticleDto from 메서드를 연결, 엔티티에서 Dto로 변경
        }

        // switch ~ case 문으로 searchType 별로 검색 (새로운 switch ~ case문 사용)
        return switch (searchType){
            case TITLE -> articleRepository.findByTitleContaining(search_keyword, pageable).map(ArticleDto::from);
            case CONTENT -> articleRepository.findByContentContaining(search_keyword, pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(search_keyword, pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(search_keyword, pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag("#"+search_keyword, pageable).map(ArticleDto::from);
        };

    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(()-> new EntityNotFoundException("게시글이 없습니다 - articleId: " + articleId));
    }

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());  // dto 정보로부터 엔티티를 만들어서 세이브.
    }

    public void updateArticle(ArticleDto dto) {
        try {
            Article article = articleRepository.getReferenceById(dto.id()); // findById는 영속성 컨텍스트에 넣기 위해 Select 쿼리를 날리지만 getReferenceById는 reference 만 가져오는 코드이다.
            if (dto.title() != null) { article.setTitle(dto.title()); } // 타이틀이 null이 아닐때만
            if (dto.content() != null) { article.setContent(dto.content()); } // 타이틀이 null이 아닐때만
            article.setHashtag(dto.hashtag());
        } catch (EntityNotFoundException e){
            log.warn("게시글 업데이트 실패, 게시글을 찾을 수 없습니다 - dto: {}", dto);  // "dto :" + dto 로 사용할 때와 차이는 워닝 로그를 찍지 않아도 될때 메모리를 잡아야 하는 부담을 덜 수 있다.
        }
    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
