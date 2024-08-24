//package bubi.clifit.config;
//
//import bubi.clifit.domain.Role;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//
//import java.util.Base64;
//import java.util.Date;
//
//@RequiredArgsConstructor
//@Component
//public class JwtTokenProvider {
//    @Value("${JWT_SECRET}")
//    private String secretkey;
//
//    //토큰 유효시간 30분
//    private long tokenValidTime = 30*60*1000L;
//
//    private final UserDetailsService userDetailsService;
//
//    // 객체 초기화 secretkey를 base64로 인코딩
//    protected void init(){
//        secretkey = Base64.getEncoder().encodeToString(secretkey.getBytes());
//    }
//
//    //JWT 토큰 생성
//    public String createToken(String userPK, Role roles) {
//        Claims claims = Jwts.claims().setSubject(userPK); // JWT payload에 저장되는 정보 단위
//        claims.put("roles", roles);
//        Date now = new Date();
//        return Jwts.builder()
//                .setClaims(claims)
//                .setIssuedAt(now)
//                .setExpiration(new Date(now.getTime()+tokenValidTime))
//                .signWith(SignatureAlgorithm.HS256,secretkey)
//                .compact();
//    }
//
//    //JWT토큰에서 인증 정보 조회
//    public Authentication getAuthentication(String token) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPK(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
//
//    //토큰에서 회원 정보 추출
//    public String getUserPK(String token) {
//        return Jwts.parser().setSigningKey(secretkey).parseClaimsJws(token).getBody().getSubject();
//    }
//
//    //Request의 Header에서 token 값을 가져온다. "X-AUTH-TOKEN" : "TOKEN 값"
//    public String resolveToken(HttpServletRequest request) {
//        return request.getHeader("X-AUTH-TOKEN");
//    }
//
//    //토큰의 유효성 + 만료일자 확인
//    public boolean validateToken(String jwtToken) {
//        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secretkey).parseClaimsJws(jwtToken);
//            return !claims.getBody().getExpiration().before(new Date());
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}
