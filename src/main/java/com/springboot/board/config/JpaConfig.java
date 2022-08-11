package com.springboot.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

@EnableJpaAuditing  // JPA Auditing 기능을 추가 활성화
@Configuration
public class JpaConfig {

    // 현재는 Spring Security 같은 인증이 없기 때문에 작성자, 수정자 데이터가 없다.
    // JPA Auditing 을 할때마다 작성자를 설정.
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.of("hun");  // TODO: 스프링 시큐리티로 인증 기능을 붙이게 될 때, 수정
    }
}
