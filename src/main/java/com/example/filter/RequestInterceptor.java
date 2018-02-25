package com.example.filter;

import com.example.redis.RedisFactory;
import com.example.session.SessionFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2017/9/4/004.
 */
public class RequestInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        if(httpServletRequest.getMethod().equalsIgnoreCase("options")){
            return true;
        }
        JedisPool jedisPool = RedisFactory.getInstance();
        Jedis jedis = jedisPool.getResource();
        String token = httpServletRequest.getHeader("token");
        String tokenExist = jedis.get("token");
        if(!token.equals(tokenExist)) {
            httpServletResponse.getWriter().write("{\"status\":501;\"msg\":\"error!\"}");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }

    private boolean isEmpty(String s) {
        return s == null || "".equals(s);
    }
}
