package com.zztracy.redisdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;

/**
 * Lua脚本配置类
 *
 * @author 詹泽
 * @since 2024/12/3 16:32
 */
@Configuration
public class LuaConfig {
    @Bean
    public DefaultRedisScript<Long> compareAndSetScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/compareAndSet.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    @Bean
    public DefaultRedisScript<Long> requestLimit() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("lua/requestLimit.lua")));
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}
