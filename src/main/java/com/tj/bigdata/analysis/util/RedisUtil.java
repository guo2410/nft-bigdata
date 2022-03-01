package com.tj.bigdata.analysis.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author Administrator
 */
@Component
@Slf4j
public class RedisUtil {

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public void deleteSortedSetDay() {
        Set<String> keys = stringRedisTemplate.keys("0x" + "*");
        assert keys != null;
        for (String key : keys) {
            Long aLong = stringRedisTemplate.opsForZSet().zCard(key);
            if (aLong != null && aLong > 35) {
                log.info("地址是 {} 长度是 {}", key, aLong);
                stringRedisTemplate.opsForZSet().removeRange(key, 0, -31);
                log.info("地址 {} 清除成功", key);
            }
        }
    }

    public void deleteSortedSetHour() {
        Set<String> keysHour = stringRedisTemplate.keys("hour" + "*");
        assert keysHour != null;
        for (String hour : keysHour) {
            Long aLong = stringRedisTemplate.opsForZSet().zCard(hour);
            if (aLong != null && aLong > 30) {
                log.info("地址是 {} 长度是 {}", hour, aLong);
                stringRedisTemplate.opsForZSet().removeRange(hour, 0, -25);
                log.info("地址 {} 清除成功", hour);
            }
        }
    }

    public void totalCount() {
        LongAdder longAdder = new LongAdder();

        Set<String> keyDay = stringRedisTemplate.keys("0x" + "*");
        if (keyDay != null && keyDay.size() != 0) {
            for (String day : keyDay) {
                Long aLong = stringRedisTemplate.opsForZSet().zCard(day);
                if (aLong != null) {
                    longAdder.add(aLong);
                    log.info("条数为 {}", aLong);
                }
            }
        }

        Set<String> keysHour = stringRedisTemplate.keys("hour" + "*");
        if (keysHour != null && keysHour.size() != 0) {
            for (String hour : keysHour) {
                Long aLong = stringRedisTemplate.opsForZSet().zCard(hour);
                if (aLong != null) {
                    longAdder.add(aLong);
                    log.info("条数为 {}", aLong);
                }
            }
        }

        long l = longAdder.longValue();
        log.info("总条数为 {}", l);
    }

    public void deleteAppCache() {
        Set<String> keysHour = stringRedisTemplate.keys("dapp" + "*");
        assert keysHour != null;
        for (String hour : keysHour) {
            stringRedisTemplate.delete(hour);
        }
    }
}
