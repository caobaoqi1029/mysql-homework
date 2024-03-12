package jz.cbq.backend.utils;

import com.alibaba.fastjson2.JSON;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 *
 * @author caobaoqi
 */
@Component
public class JWTUtils {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Value("${cbq.jwt-secret}")
    private static final String JWT_SECRET = "CaoBaoQi";

    /**
     * 成绩 token
     * @param userId userId
     * @return token
     */
    public static String createToken(String userId) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, JWT_SECRET)
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
        return jwtBuilder.compact();
    }

    /**
     *  解析 token
     * @param token token
     * @return Object
     */
    public static Object getUserIdByToken(String token) {
        Map<String, Object> body = (Map<String, Object>) Jwts.parser().setSigningKey(JWT_SECRET).parse(token).getBody();
        return body.get("userId");
    }

    /**
     * 通过 token 获取 User
     * @param token token
     * @return User
     */
    public Object getUserByToken(String token) {
        Object userIdByToken = getUserIdByToken(token);
        if (null == userIdByToken) {
            return null;
        }
        String userJson = redisTemplate.opsForValue().get(token);
        if (null == userJson) {
            return null;
        }
        return JSON.parseObject(userJson, Object.class);
    }
}
