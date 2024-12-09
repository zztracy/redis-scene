package com.zztracy.redisdemo.task;

import com.zztracy.redisdemo.constant.PVConstants;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * @author 詹泽
 * @since 2024/12/1 16:53
 */
@Slf4j
@Service
public class LevelTwoCacheTask {
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void cacheTask() {
        log.info("启动定时器：二级缓存消费......");
//        new Thread(() -> runCache()).start();
    }

    public void runCache() {
        while (true) {
            while (this.pop()) {
            }
            try {
                Thread.sleep(1000 * 60 * 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("消费二级缓存，定时刷新");
        }
    }

    public boolean pop() {
        ListOperations<String, Map<Integer, Integer>> listOperations = redisTemplate.opsForList();
        Map<Integer, Integer> map = listOperations.rightPop(PVConstants.CACHE_PV_LIST);
        log.info("弹出pop = {}", map);
        if (CollectionUtils.isEmpty(map)) {
            return false;
        }
        // todo：存入数据库
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            String key = PVConstants.CACHE_ARTICLE + entry.getKey();
            Long increment = redisTemplate.opsForValue().increment(key, entry.getValue());
        }
        return true;
    }
}
