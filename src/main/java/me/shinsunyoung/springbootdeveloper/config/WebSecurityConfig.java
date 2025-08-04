/*
package me.shinsunyoung.springbootdeveloper.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                // /h2-console의 접속에 스프링 시큐리티를 해제
                .requestMatchers(toH2Console())
                */
/*,requestMachers(new AntPathMatcher("/static/**"));*//*

                //resources/static폴더 접속에 스프링 시큐리티 해제
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(authorize ->
                //requestMatchers(허용하고싶은 Controller에서 사용할 주소).permitAll()
                        authorize.requestMatchers("/login", "/signup","/user", "/articles")
                                .permitAll()
                        //위에서 허용한 주소 이외에는 모두 로그인이 필요하도록 설정
                .anyRequest().authenticated())
                //로그인 관련설정
                .formLogin(formLogin->
                        // /login 주소로 로그인페이지 설정
                        formLogin.loginPage("/login")
                                //로그인 성공 시 출력할 페이지설정
                                .defaultSuccessUrl("/articles"))
                //logout관련 설정
                .logout(logout ->
                                //logout성공시 실행할 페이지
                                logout.logoutSuccessUrl("/login")
                                        // 세션의 모든 데이터를 삭제
                                        .invalidateHttpSession(true)
                        //csrf 비활성화
                        //csrf(Cross-site Request Forgery)
                        //POST,PUT,DELETE 요청이 외부에서 위조되어 들어오는 것을 막는 기능
                        //페이지를 보낼때 CSRF용 토큰을 함께 전달하고 post,put,delete요청 시
                        //토큰이 없으면 실행되지 않도록 하여 외부 접속을 막는 방식
                        ).csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(
            HttpSecurity http,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            UserDetailsService userDetailsService
    ) throws Exception{
        //인증관리자
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        //사용자 정보가지고 올 방식설정 =>h2데이터베이스에서 User가져오도록
        authProvider.setUserDetailsService(userDetailsService);
        //비밀번호 암호화 인코더 설정
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authProvider);
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
*/
