package com.zztracy.redisdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置
 *
 * @author 詹泽
 * @since 2024/12/1 20:42
 */
@Configuration
// 该类使用 @Configuration 注解，表明这是一个配置类，Spring会扫描并加载这个类中的配置信息
public class RedisConfig {
    /**
     * 配置RedisTemplate的Bean
     * 用于在Spring应用中方便地操作Redis，定义了键、值以及哈希类型的键值的序列化方式等配置
     * @param redisConnectionFactory Redis连接工厂，用于创建与Redis服务器的连接
     * @return 配置好的RedisTemplate实例，可供其他组件注入并使用来操作Redis
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        // 设置RedisTemplate的连接工厂，通过它来建立与Redis服务器的实际连接
        template.setConnectionFactory(redisConnectionFactory);

        // 创建一个GenericJackson2JsonRedisSerializer实例，用于将对象序列化为JSON格式的字符串存储到Redis中
        // 它可以很好地处理Java对象的序列化与反序列化，方便存储复杂的对象数据
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();

        // 设置RedisTemplate的值序列化器为GenericJackson2JsonRedisSerializer
        // 这样在往Redis中存储值时，会将对象序列化为JSON格式
        template.setValueSerializer(genericJackson2JsonRedisSerializer);

        // 设置RedisTemplate的键序列化器为StringRedisSerializer
        // 意味着键会以简单的字符串形式进行序列化与反序列化，便于在Redis中进行查找等操作
        template.setKeySerializer(new StringRedisSerializer());

        // 设置Redis中哈希数据结构的键序列化器为StringRedisSerializer
        // 保证哈希类型数据的键以字符串形式处理
        template.setHashKeySerializer(new StringRedisSerializer());

        // 设置Redis中哈希数据结构的值序列化器为GenericJackson2JsonRedisSerializer
        // 使得哈希类型数据的值也能以JSON格式进行存储与读取
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);

        // 调用afterPropertiesSet方法，确保RedisTemplate完成属性设置后进行必要的初始化操作
        template.afterPropertiesSet();

        return template;
    }
}