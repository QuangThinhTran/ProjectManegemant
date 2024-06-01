package com.vn.projectmanagement.security.JWT;

import com.vn.projectmanagement.entity.dto.AuthenticationDTO;
import com.vn.projectmanagement.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long expiration;

    /**
     * Tạo ra chuỗi JWT Token
     *
     * @param user thông tin của người dùng
     * @return chuỗi JWT Token
     */
    public String generateToken(AuthenticationDTO user) {
        Date date = new Date();
        Date expirationDate = new Date(date.getTime() + this.expiration * 1000000 );

        SecretKey secretKey = Keys.hmacShaKeyFor(this.jwtSecret.getBytes());
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("username", user.getUsername())
                .claim("email", user.getEmail())
                .claim("phone", user.getPhone())
                .claim("role", user.getRole())
                .setIssuedAt(date)
                .setExpiration(expirationDate)
                .signWith(secretKey)
                .compact();
    }

    /**
     * Kiểm tra xem chuỗi JWT Token có hợp lệ không
     *
     * @param token chuỗi JWT Token
     * @return true nếu token hợp lệ, ngược lại trả về false
     */
    public boolean validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(this.jwtSecret.getBytes()).build().parseClaimsJws(token).getBody()
                .getExpiration().before(new Date());
        return !expirationToken(token);
    }

    /**
     * Kiểm tra xem chuỗi JWT Token đã hết hạn chưa
     *
     * @param token chuỗi JWT Token
     * @return trả về true nếu token đã hết hạn, ngược lại trả về false
     */
    private boolean expirationToken(String token) {
        return Jwts.parserBuilder().setSigningKey(this.jwtSecret.getBytes()).build().parseClaimsJws(token).getBody()
                .getExpiration().before(new Date());
    }

    /**
     * Trích xuất username từ chuỗi JWT Token
     *
     * @param token chuỗi JWT Token
     * @return trả về username từ chuỗi JWT Token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Trích xuất thông tin từ chuỗi JWT Token
     *
     * @param token          chuỗi JWT Token
     * @param claimsResolver đối tượng chứa thông tin của người dùng
     * @param <T>            kiểu dữ liệu trả về
     * @return trả về đối tượng chứa thông tin của người dùng
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims); // Sẽ trả về các đôí tượng trong Claims ( Tức là truy xuất dữ liệu từ Claims)
    }

    /**
     * Truy xuất toàn bộ đối tượng đã được mã hóa từ chuỗi JWT Token
     *
     * @param token chuỗi JWT Token
     * @return trả về đối tượng chứa thông tin của người dùng
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(this.jwtSecret.getBytes()).build().parseClaimsJws(token).getBody();
    }
}
