package com.zztracy.redisdemo.controller;

import com.zztracy.redisdemo.util.ShortUrlUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 詹泽
 * @since 2024/12/5 15:59
 */
@Slf4j
@RestController
public class ShortUrlController {
    public static final String SHORT_URL_KEY = "short:url";
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 长链接转为短链接加密串，然后存储在redis的hash结构中
     *
     * @param url
     * @return
     */
    @GetMapping(value = "/encode")
    public String encode(String url) {
        String[] keys = ShortUrlUtils.shortUrl(url);
        String key = keys[0];
        redisTemplate.opsForHash().put(SHORT_URL_KEY, key, url);
        log.info("长链接={}， 转换={}", url, key);
        return "http://127.0.0.1:8001/" + key;
    }


}
