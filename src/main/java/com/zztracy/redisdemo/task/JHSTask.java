package com.zztracy.redisdemo.task;

import com.zztracy.redisdemo.constant.JHSConstants;
import com.zztracy.redisdemo.model.Product;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 聚划算任务
 *
 * @author 詹泽
 * @since 2024/12/2 10:27
 */
@Slf4j
@Service
public class JHSTask {
    @Autowired
    private RedisTemplate redisTemplate;

    @PostConstruct
    public void initJHS() {
        log.info("启动定时器: 模拟从数据库读取聚划算数据到redis中......");
        new Thread(() -> runJHS()).start();
    }

    /**
     * 模拟定时器，定时将数据库中的特价商品刷新到redis中
     */
    public void runJHS() {
        while (true) {
            List<Product> productList = this.getProductList();
            this.redisTemplate.delete(JHSConstants.JHS_KEY_B);
            this.redisTemplate.opsForList().leftPushAll(JHSConstants.JHS_KEY_B, productList);
            this.redisTemplate.delete(JHSConstants.JHS_KEY_A);
            this.redisTemplate.opsForList().leftPushAll(JHSConstants.JHS_KEY_A, productList);
            try {
                Thread.sleep(1000 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("runJHS 定时刷新......");
        }
    }

    /**
     * 模拟从数据库读取100件特价商品，用于加载到聚划算页面中
     *
     * @return 特价商品
     */
    public List<Product> getProductList() {
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Random random = new Random();
            int id = random.nextInt(10000);
            Product product = new Product((long) id, "product" + i, i, "detail");
            productList.add(product);
        }
        return productList;
    }
}
