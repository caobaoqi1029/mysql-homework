package jz.cbq.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static jz.cbq.backend.utils.JWTUtils.createToken;
import static jz.cbq.backend.utils.JWTUtils.getUserIdByToken;

/**
 * BackendApplicationTests
 *
 * @author cbq
 * @date 2023/12/7 8:04
 * @since 1.0.0
 */
@SpringBootTest
public class BackendApplicationTests {

    @Test
    void testJWT() {
        String token = createToken("CaoBaoQi");
        System.out.println("token = " + token);
        Object o = null;
        try {
            o = getUserIdByToken("eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MDE5OTQwNDMsInVzZXJJZCI6IkNhb0Jhb1FpIiwiaWF0IjoxNzAxOTA3NjQzfQ.4yFYG58p4N0tlxALOaXjFfVRtE5ip9V5zE1xfwwmV8Y");
            System.out.println("token 合法");
        } catch (Exception e) {
            System.out.println("token 无效");
        }
    }
}
