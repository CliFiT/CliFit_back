//package bubi.clifit.config;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.ServletRequest;
//import jakarta.servlet.ServletResponse;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.GenericFilterBean;
//
//import java.io.IOException;
//
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends GenericFilterBean {
//
//    private final JwtTokenProvider jwtTokenProvider;
//
//    @Override
//    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
//        //헤더에서 JWT를 받아옴
//        String token = jwtTokenProvider.resolveToken((HttpServletRequest) req);
//        //유효한 토큰인지 확인
//        if(token != null && jwtTokenProvider.validateToken(token)) {
//            //토큰이 유효하면 토큰에서 유저 정보를 받아옴
//            Authentication auth = jwtTokenProvider.getAuthentication(token);
//            //securityContext에 auth객체를 저장
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//        chain.doFilter(req, res);
//    }
//}
