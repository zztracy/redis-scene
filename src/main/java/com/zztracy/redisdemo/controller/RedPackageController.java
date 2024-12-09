package com.zztracy.redisdemo.controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 模拟并发抢红包
 * @author 詹泽
 * @since 2024/12/1 9:50
 */
@Slf4j
@RestController
public class RedPackageController {
    private static final String ID_KEY = "id:generator:repacket";
    private static final String RED_PACKET_KEY = "redpacket:";
    private static final String RED_PACKET_CONSUME_KEY = "redpacket:consume";

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping(value = "/set")
    public long setRedPacket(Integer total, Integer count) {
        Integer[] packet = this.splitRedPacket(total, count);
        long n = this.incrementId();
        String key = RED_PACKET_KEY + n;
        this.redisTemplate.opsForList().leftPushAll(key, packet);
        //设置3天过期
        this.redisTemplate.expire(key, 3, TimeUnit.DAYS);
        log.info("拆解红包{}={}", key, packet);
        return n;
    }
    @GetMapping(value = "/rob")
    public int rob(Long redId, Long userId) {
        Object packet = this.redisTemplate.opsForHash().get(RED_PACKET_CONSUME_KEY + redId, String.valueOf(userId));
        if (packet == null) {
            Object obj = redisTemplate.opsForList().leftPop(RED_PACKET_KEY + redId);
            if (obj != null) {
                redisTemplate.opsForHash().put(RED_PACKET_CONSUME_KEY + redId, String.valueOf(userId), obj);
                log.info("用户 = {}, 抢到红包{}", userId, obj);
                return (Integer) obj;
            }
            // -1 代表抢完红包
            log.info("红包已抢完");
            return -1;
        }
        // -2 代表已抢
        log.info("用户 = {}, 已经抢过红包", userId);
        return -2;
    }

    public Long incrementId() {
        return this.redisTemplate.opsForValue().increment(ID_KEY);
    }

    public Integer[] splitRedPacket(int total, int count) {
        int use = 0;
        Integer[] array = new Integer[count];
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            if (i == count - 1) {
                array[i] = total - use;
            } else {
                int avg = (total - use) * 2 / (count - i);
                array[i] = 1 + random.nextInt(avg - 1);
            }
            use = use + array[i];
        }
        return array;
    }
}
