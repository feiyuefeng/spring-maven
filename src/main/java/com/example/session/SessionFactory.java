package com.example.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by fyf on 2017/9/4/004.
 */
public class SessionFactory {
    private static final Logger logger = LoggerFactory.getLogger(SessionFactory.class);

    public static HttpSession getSession(){
        return getRequest().getSession();
    }

//    public static HttpServletResponse getResponse(){
//        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
//    }

    public static HttpServletRequest getRequest(){
        return ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * session设置用户名
     * @param userName
     */
    public static void setUserName(String userName){
        logger.debug("set userName " + userName);
        getRequest().getSession().setAttribute("userName", userName);
    }

    /**
     * session获取用户ID
     * @return
     */
    public static String getUserName(){
        Object userName = getRequest().getSession().getAttribute("userName");
        if(userName == null){
            logger.debug("getUserName null");
            return "";
        }else{
            logger.debug("getUserName = " + userName);
            return userName.toString();
        }
    }

    public static void deleteUserName(){
        getRequest().getSession().removeAttribute("userName");
    }
}
