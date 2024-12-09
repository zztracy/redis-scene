package com.zztracy.redisdemo.controller;

import com.zztracy.redisdemo.util.IpUtills;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.net.IPv6Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 使用lua脚本进行requestLimit
 *
 * @author 詹泽
 * @since 2024/12/3 16:35
 */
@Slf4j
@RestController
public class RequestLimitController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource(name = "requestLimit")
    private DefaultRedisScript<Long> requestLimit;

    @GetMapping(value = "request-limit-lua")
    public void updateUserLua(HttpServletRequest request) {
        String ipAddress = IpUtills.getIpAddress(request);
        List<String> keys = Arrays.asList(ipAddress);
        Integer expireSecond = 30;
        Integer limitRate = 5;
        Long res = redisTemplate.execute(requestLimit, keys, expireSecond + "", limitRate + "");
        if (res == 0) {
            log.info("{}异常ip，{}秒内请求超过{}次", ipAddress, expireSecond, limitRate);
        } else {
            log.info("{}正常ip", ipAddress);
        }
    }
}
