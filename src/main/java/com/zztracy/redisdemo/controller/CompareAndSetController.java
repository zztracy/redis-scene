package com.zztracy.redisdemo.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 使用lua脚本进行compareAndSet
 *
 * @author 詹泽
 * @since 2024/12/3 16:35
 */
@Slf4j
@RestController
public class CompareAndSetController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource(name = "compareAndSetScript")
    private DefaultRedisScript<Long> compareAndSetScript;

    @GetMapping(value = "updateuser-lua")
    public void updateUserLua(Integer userid, String username) {
        String key = "user:" + userid;
        List<String> keys = Arrays.asList(key);
        Long res = redisTemplate.execute(compareAndSetScript, keys, username);
        if (res == 0) {
            log.info("{}不用修改", key);
        } else {
            log.info("{}修改为{}", key, username);
        }
    }
}
