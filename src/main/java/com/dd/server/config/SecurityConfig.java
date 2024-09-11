package com.dd.server.config;


import com.dd.server.jwt.CustomLogoutFilter;
import com.dd.server.jwt.JWTFilter;
import com.dd.server.jwt.JWTUtil;
import com.dd.server.jwt.LoginFilter;
import com.dd.server.repository.RefreshRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //AuthenticationManager 가 인자로 받을 AuthenticationConfiguration 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    private final JWTUtil jwtUtil;

    private final RefreshRepository refreshRepository;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration, JWTUtil jwtUtil, RefreshRepository refreshRepository) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;
    }

    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        //csrf disable
        http
                .csrf((auth) -> auth.disable());

        //From 로그인 방식 disable
        http
                .formLogin((auth) -> auth.disable());

        //http basic 인증 방식 disable
        http
                .httpBasic((auth) -> auth.disable());

        // 인가 설정
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/join").permitAll()
                        .requestMatchers("/reissue").permitAll()

                        .requestMatchers("/profile/edit").permitAll()
                        .requestMatchers("/profile/delete").permitAll()
                        .requestMatchers("/profile/find").permitAll()

                        .requestMatchers("/medicine/info").permitAll()
                        .requestMatchers("/medicine/image").permitAll()
                        .requestMatchers("/medicine/find").permitAll()
                        .requestMatchers("/medicine/statistics").permitAll()

                        //임시 허용
                        .requestMatchers("/user/find").permitAll()
                        .requestMatchers("/user/edit").permitAll()
                        .requestMatchers("/user/delete").permitAll()

                        .requestMatchers("/statistic/find").permitAll()
                        .requestMatchers("/statistic/create").permitAll()
                        .requestMatchers("/statistic/edit").permitAll()
                        .requestMatchers("/statistic/delete").permitAll()

                        .requestMatchers("/reminder/medicine/find").permitAll()
                        .requestMatchers("/reminder/medicine/create").permitAll()
                        .requestMatchers("/reminder/medicine/edit").permitAll()
                        .requestMatchers("/reminder/medicine/delete").permitAll()

                        .requestMatchers("/reminder/booked/find").permitAll()
                        .requestMatchers("/reminder/booked/create").permitAll()
                        .requestMatchers("/reminder/booked/edit").permitAll()
                        .requestMatchers("/reminder/booked/delete").permitAll()

                        .requestMatchers("/data/{fileName}").permitAll()
                        .requestMatchers("/data").permitAll()

                        .anyRequest().authenticated());

        // JwtFilter 등록
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class);


        //로그인 필터 추가
        http
                .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository), UsernamePasswordAuthenticationFilter.class);

        //로그아웃 필터 추가
        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository), LogoutFilter.class);

        //세션 설정
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}