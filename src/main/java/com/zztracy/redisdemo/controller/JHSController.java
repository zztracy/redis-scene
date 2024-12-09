package com.zztracy.redisdemo.controller;

import com.zztracy.redisdemo.constant.JHSConstants;
import com.zztracy.redisdemo.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 查看聚划算页面的分页结果
 *
 * @author 詹泽
 * @since 2024/12/2 10:41
 */
@RestController
public class JHSController {
    @Autowired
    private RedisTemplate redisTemplate;

    private static final int STEP = 5;

    @GetMapping(value = "jhs")
    public List<Product> getJHSList(int page) {
        int start = (page - 1) * STEP;
        int end = page * STEP - 1;
        List<Product> productList = Collections.emptyList();
        productList = redisTemplate.opsForList().range(JHSConstants.JHS_KEY_A, start, end);
        if (CollectionUtils.isEmpty(productList)) {
            productList = redisTemplate.opsForList().range(JHSConstants.JHS_KEY_B, start, end);
        }
        return productList;
    }
}
