package com.zztracy.redisdemo.task;

import com.zztracy.redisdemo.constant.PVConstants;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Map;

/**
 * @author 詹泽
 * @since 2024/12/1 16:27
 */
@Service
@Slf4j
public class LevelOneCacheTask {
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void cacheTask() {
        log.info("启动定时器：一级缓存消费......");
//        new Thread(() -> runCache()).start();
    }

    public void runCache() {
        while (true) {
            consumePV();
            try {
                Thread.sleep(90000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void consumePV() {
        long currentTimeBlock = System.currentTimeMillis() / (1000 * 60 * 1);
        Iterator<Long> iterator = PVConstants.PV_MAP.keySet().iterator();
        while (iterator.hasNext()) {
            Long historyTimeBlock = iterator.next();
            // 小于当前每分钟的key，就消费
            if (historyTimeBlock < currentTimeBlock) {
                Map<Integer, Integer> map = PVConstants.PV_MAP.get(historyTimeBlock);
                redisTemplate.opsForList().leftPush(PVConstants.CACHE_PV_LIST, map);
                // 后remove
                PVConstants.PV_MAP.remove(historyTimeBlock);
                log.info("push进{}", map);
            }
        }
    }
}
