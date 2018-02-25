package com.example.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.Result.WebResult;
import com.example.constants.ResultStatus;
import com.example.redis.RedisFactory;
import com.example.session.SessionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;
import java.util.UUID;

/**
 * Created by fyf on 2017/8/31/031.
 */
@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String USER_NAME = "jikejia";
    private static final String PASS_WORD = "qwer1234";

    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<WebResult> login(@RequestBody Map<String, Object> map){
        String userName = map.get("userName").toString();
        String password = map.get("password").toString();
        if (USER_NAME.equals(userName) && PASS_WORD.equals(password)) {
            String token = UUID.randomUUID().toString();
//            SessionFactory.setUserName(token);
            JedisPool jedisPool = RedisFactory.getInstance();
            Jedis jedis = jedisPool.getResource();
            jedis.set("token", token);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("token",token);
            logger.info(USER_NAME + " login");
            return new ResponseEntity<>(new WebResult(ResultStatus.SUCCESS, jsonObject), HttpStatus.OK);
        }
        return new ResponseEntity<>(new WebResult(ResultStatus.FAILURE, "用户名或者密码错误"), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping("/logout")
    public ResponseEntity<WebResult> logout() {
        JedisPool jedisPool = RedisFactory.getInstance();
        Jedis jedis = jedisPool.getResource();
        jedis.del("token");
//        SessionFactory.deleteUserName();
        return new ResponseEntity<>(new WebResult(ResultStatus.SUCCESS), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping("/testToken")
    public ResponseEntity<WebResult> testToken(@RequestBody Map<String,Object> map) {
        String token = map.get("token").toString();
        JedisPool jedisPool = RedisFactory.getInstance();
        Jedis jedis = jedisPool.getResource();
        String tokenExist = jedis.get("token");
        if(token.equals(tokenExist))
            return new ResponseEntity<>(new WebResult(ResultStatus.SUCCESS), HttpStatus.OK);
        return new ResponseEntity<>(new WebResult(ResultStatus.ACTIVEEXPIRE,"登陆已经过期"), HttpStatus.OK);
    }
}
