package me.shinsunyoung.springbootdeveloper.config;

import lombok.RequiredArgsConstructor;
import me.shinsunyoung.springbootdeveloper.config.jwt.TokenProvider;
import me.shinsunyoung.springbootdeveloper.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import me.shinsunyoung.springbootdeveloper.config.oauth.OAuth2SuccessHandler;
import me.shinsunyoung.springbootdeveloper.config.oauth.OAuth2UserCustomService;
import me.shinsunyoung.springbootdeveloper.repository.RefreshTokenRepository;
import me.shinsunyoung.springbootdeveloper.sevice.UserService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {
    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                // /h2-console의 접속에 스프링 시큐리티를 해제
//                .requestMatchers(toH2Console())
                /*,requestMachers(new AntPathMatcher("/static/**"));*/
                //resources/static폴더 접속에 스프링 시큐리티 해제
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
   public SecurityFilterChain filterChain(HttpSecurity http, OAuth2SuccessHandler oAuth2SuccessHandler) throws Exception {
        return http
                //csrf설정 끄기
                .csrf(AbstractHttpConfigurer::disable)
                //로그인 시 id와 pw를 base64로 인코딩하여 전달하는 설정 끄지
                .httpBasic(AbstractHttpConfigurer::disable)
                //JWT를 이용한  로그인을 사용하고 일반로그인 끄기
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                //세션 사용하지 않도록 설정
                .sessionManagement(management ->management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //addFilterBefor(필터1, 필터2)
                //필터2가 실행되기 전 필터1을 실행하도록 추가하는 메서드
                .addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests(auth-> auth
                        //아무런 권한이 없어도 실행 가능한 Mapping설정
                        .requestMatchers(new AntPathRequestMatcher("/api/token")).permitAll()
                        //로그인을 해야만 실행 가능한 매핑
                        .requestMatchers(new AntPathRequestMatcher("/api/**")).authenticated()
                        //위 두개를 제외한 모든 mapping은 권한 없어도 실행가능하도록
                        .anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2.loginPage("/login")
                        //로그인 처리를 실행할 서비스를 설정
                        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint.authorizationRequestRepository(
                                oAuth2AuthoriztionRequestBasedOnCookieRepository()))
                        .userInfoEndpoint(userInfoEndpoint ->userInfoEndpoint.userService(oAuth2UserCustomService))
                        .successHandler(oAuth2SuccessHandler())
                )
                        .exceptionHandling(exceptionHandling -> exceptionHandling
                                .defaultAuthenticationEntryPointFor(
                                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                                        new AntPathRequestMatcher("/api/**")
                                ))
                        .build();
    }
    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler(){
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AuthoriztionRequestBasedOnCookieRepository(),
                userService
        );
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(){
        return new TokenAuthenticationFilter(tokenProvider);
    }
    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthoriztionRequestBasedOnCookieRepository(){
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
