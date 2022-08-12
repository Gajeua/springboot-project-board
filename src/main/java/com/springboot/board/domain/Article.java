package com.springboot.board.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter  // 임의의 값을 바꿀 필드에만 Setter를 사용
@ToString  // toString 메서드를 Lombok이 알아서 해준다.
@Table(indexes = {  // 검색기능을 할때 사용할 인덱싱 작업.
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class Article extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mySQL의 Auto_increament 사용
    private Long id;

    @Setter @Column(nullable = false) private String title;  // 제목
    @Setter @Column(nullable = false, length = 10000) private String content;  // 내용

    @Setter private String hashtag;  // 해시태그

    // 양방향 바인딩
    // Article에 연동되어있는 Comment는 중복을 허용하지 않고 컬렉션으로 보겠다.
    @ToString.Exclude
    @OrderBy("id")
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    // 기본 생성자
    protected Article() {
    }

    // 값 변경을 할(Setter를 가진) 파라미터를 모두 가진 생성자
    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    // 게시글은 추후에 List로 데이터를 받아오고 내보내주는 니즈가 있다.
    // List에 중복요소를 제거하거나, 정렬을 해야할 때 비교를 해야한다.
    // 그때 동등성, 동일성 검사를 하는 equlas, hashCode 메서드다.
    // 동등성 검사
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article article)) return false;  // o 객체가 Article인지 확인
        return id !=null && id.equals(article.id);  // 아직 영속화 되지 않은 Entity는 모두 동등성 검사를 탈락.
    }

    // 동일성 검사
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
