package com.zztracy.redisdemo.task;

import com.zztracy.redisdemo.constant.PVConstants;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模拟PV
 *
 * @author 詹泽
 * @since 2024/12/1 15:35
 */
@Slf4j
@Service
public class InitPVTask {
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void initPV() {
        log.info("启动模拟大量PV请求定时器..");
//        new Thread(() -> runArticlePV()).start();
    }

    public void runArticlePV() {
        while (true) {
            this.batchAddArticle();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void batchAddArticle() {
        for (int i = 0; i < 1000; i++) {
            addPV(i);
        }
    }

    /**
     * 当有新的PV时，获取当前时间区段，并且为当前时间区段的文章的PV增加1
     *
     * @param id 文章id
     */
    private void addPV(Integer id) {
        long timeBlock = System.currentTimeMillis() / (1000 * 60 * 1);
        Map<Integer, Integer> timeBlockMap = PVConstants.PV_MAP.get(timeBlock);
        if (CollectionUtils.isEmpty(timeBlockMap)) {
            // 用ConcurrentHashMap模拟一个计数器
            timeBlockMap = new ConcurrentHashMap<>();
            timeBlockMap.put(id, 1);
            PVConstants.PV_MAP.put(timeBlock, timeBlockMap);
        } else {
            Integer value = timeBlockMap.get(id);
            if (value == null) {
                timeBlockMap.put(id, 1);
            } else {
                timeBlockMap.put(id, value + 1);
            }
        }
    }
}
