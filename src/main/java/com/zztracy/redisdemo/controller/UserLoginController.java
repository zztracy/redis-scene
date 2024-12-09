package com.zztracy.redisdemo.controller;

import com.zztracy.redisdemo.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户登陆controller
 *
 * @author 詹泽
 * @since 2024/12/4 16:51
 */
@Slf4j
@RestController
@RequestMapping(value = "/user")
public class UserLoginController {
    public static final Map<String, User> USER_MAP = new HashMap<>();
    static {
        // 模拟数据库用户
        User u1 = new User(1, "zz", "pwd");
        User u2 = new User(2, "yy", "pwd");
        USER_MAP.put("zz", u1);
        USER_MAP.put("yy", u2);
    }

    @GetMapping(value = "/login")
    public String login(String username, String password, HttpSession session) {
        User user = USER_MAP.get(username);
        if (user != null) {
            if (!password.equals(user.getPassword())) {
                return "用户名或密码错误";
            } else {
                session.setAttribute(session.getId(), user);
                log.info("登录成功{}", user);
            }
        } else {
            return "用户名或密码错误";
        }
        return "登录成功！！！";
    }

    @GetMapping(value = "/find/{username}")
    public User find(@PathVariable String username) {
        User user = USER_MAP.get(username);
        log.info("通过用户名={}, 查找出用户{}", username, user);
        return user;
    }

    /**
     * 获取当前用户的session
     * @param session 会话对象
     * @return sessionId
     */
    @GetMapping(value = "/session")
    public String session(HttpSession session) {
        log.info("当前用户session={}", session.getId());
        return session.getId();
    }

    /**
     * 退出登录
     * @param session 会话对象
     * @return 退出登录
     */
    @GetMapping(value = "/logout")
    public String logout(HttpSession session) {
        session.removeAttribute(session.getId());
        log.info("退出登录session={}", session.getId());
        return "成功退出";
    }
}
