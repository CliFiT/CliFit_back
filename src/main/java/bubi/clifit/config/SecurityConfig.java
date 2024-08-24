//package bubi.clifit.config;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@RequiredArgsConstructor
//@EnableWebSecurity
//@Configuration
//public class SecurityConfig {
//    private final JwtTokenProvider jwtTokenProvider;
//
//    //비밀번호 암호화
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
//    }
//
//    //authenticationManager를 Bean등록
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .httpBasic(httpBasic -> httpBasic.disable()) // rest api만을 고려하여 기본설정 해제
//                .csrf(csrf -> csrf.disable())
//                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //토큰 기반 인증이라 세션 사용 안 함
//                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/api/post/**").authenticated()
//                        .anyRequest().permitAll()
//                )
//                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
//        //JwtAuthenticationFilter를 UsernamePasswordAuthenticationFilter 전에 넣음
//
//        return http.build();
//    }
//}
