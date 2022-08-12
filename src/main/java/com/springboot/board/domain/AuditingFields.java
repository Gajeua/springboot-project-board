package com.springboot.board.domain;

import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class) // Auditing을 확인해주는 어노테이션
@MappedSuperclass  // 슈퍼클래스 지정
public class AuditingFields {

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // 파싱에 대한 룰 정해주는 어노테이션 (ISO 객체를 쓰는 방법)
    @CreatedDate
    @Column(nullable = false, updatable = false) // 업데이트 불가능하다.
    private LocalDateTime createdAt;  // 생성일시

    @CreatedBy
    @Column(nullable = false, length = 100)
    private String createdBy;  // 생성자

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) // 파싱에 대한 룰 정해주는 어노테이션 (ISO 객체를 쓰는 방법)
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedAt;  // 수정일시

    @LastModifiedBy
    @Column(nullable = false, length = 100)
    private String modifiedBy;  // 수정자
}
