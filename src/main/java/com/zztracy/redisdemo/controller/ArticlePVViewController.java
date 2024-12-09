package com.zztracy.redisdemo.controller;

import com.zztracy.redisdemo.constant.PVConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 詹泽
 * @since 2024/12/1 20:36
 */
@Slf4j
@RestController
public class ArticlePVViewController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping(value = "/view")
    public String view(Integer id) {
        String key = PVConstants.CACHE_ARTICLE + id;
        String n = redisTemplate.opsForValue().get(key);
        log.info("key = {}, 阅读量为{}", key, n);
        return n;
    }
}
