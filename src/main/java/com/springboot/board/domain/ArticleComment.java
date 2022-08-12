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
import java.util.Objects;

@Getter  // 임의의 값을 바꿀 필드에만 Setter를 사용
@ToString  // toString 메서드를 Lombok이 알아서 해준다.
@Table(indexes = {  // 검색기능을 할때 사용할 인덱싱 작업.
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
@Entity
public class ArticleComment extends AuditingFields{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // mySQL의 Auto_increament 사용
    private Long id;

    @Setter @ManyToOne(optional = false) private Article article;  // 게시글 (ID)
    @Setter @Column(nullable = false, length = 500) private String content;  // 내용

    // 기본생성자
    protected ArticleComment(){}

    // 값 변경을 할(Setter를 가진) 파라미터를 모두 가진 생성자
    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, String content) {
        return new ArticleComment(article, content);
    }

    // 동등성, 동일성 검사
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null && id.equals(that.id);
    }

   @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
