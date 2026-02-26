package com.maxbon.springFirst.config;

import com.maxbon.springFirst.domain.user.entity.UserRoleType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean // MANAGER 계급도 추가해야함.
    public RoleHierarchy roleHierarchy() {

        return RoleHierarchyImpl.withRolePrefix("ROLE_")
                .role(UserRoleType.ADMIN.toString()).implies(UserRoleType.USER.toString())
                .build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // csrf 보안 해체
        http
                .csrf(csrf -> csrf.disable());

        // 접근 경로별 인가 설정
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/update/**").hasRole("USER")
                        .requestMatchers("/delete/**").hasRole("USER")
                        .requestMatchers("/board/create").hasRole("USER")
                        .requestMatchers("/board/update/**").hasRole("USER")
                        .requestMatchers("/board/delete/**").hasRole("USER")
                        .requestMatchers("/**").permitAll());

        // 로그인 방식 설정 Form 로그인 방식
        http
                .formLogin(form -> form
                        .loginPage("/login") // 로그인 페이지 URL
                        .loginProcessingUrl("/login-proc") // templates 패키지에 있는 파일의 form 의 action 과 일치 해야함.
                        .defaultSuccessUrl("/") // 로그인 성공 시 이동 할 페이지
                        .permitAll()
                );

        return http.build();
    }
}
