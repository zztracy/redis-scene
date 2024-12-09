package com.zztracy.redisdemo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 詹泽
 * @since 2024/12/4 17:17
 */
@Slf4j
@Configuration
public class SessionConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityInterceptor()).excludePathPatterns("/user/login")
                .excludePathPatterns("/user/logout")
                .addPathPatterns("/**");
    }

    @Configuration
    public class SecurityInterceptor implements HandlerInterceptor {
        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
            HttpSession session = request.getSession();
            return true;
//            if (session.getAttribute(session.getId()) != null) {
//                log.info("session拦截器, session={}，验证通过", session.getId());
//                return true;
//            }
//            response.setCharacterEncoding("UTF-8");
//            response.setContentType("application/json; charset=utf-8");
//            response.getWriter().write("请登录！!!!!");
//            log.info("session拦截器，session={}，验证失败",session.getId());
//            return false;
        }
    }
}
