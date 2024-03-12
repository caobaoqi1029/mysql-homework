package jz.cbq.backend.service.impl;

import jz.cbq.backend.service.ILoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * LoginServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class LoginServiceImpl implements ILoginService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Async("taskExecutor")
    @Override
    public void logout(String token) {
        System.out.println("线程 --" + Thread.currentThread().getName() + " 执行异步任务：从 redis 中删除 token");
        redisTemplate.delete(token);
    }
}
