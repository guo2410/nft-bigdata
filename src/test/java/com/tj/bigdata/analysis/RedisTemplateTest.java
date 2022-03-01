package com.tj.bigdata.analysis;

import com.tj.bigdata.analysis.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RedisTemplateTest {


    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void test() {
        redisTemplate.opsForZSet().removeRange("hour_0xf629cbd94d3791c9250152bd8dfbdf380e2a3b9c",0,-25);
    }
}
