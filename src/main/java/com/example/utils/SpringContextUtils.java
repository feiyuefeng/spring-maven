package com.example.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 初始化spring容器
 * Created by leonfu on 2016/1/23.
 */
public class SpringContextUtils {
    private static ApplicationContext context;

    public static ApplicationContext getContext() {
        if (context == null) {
            context = new ClassPathXmlApplicationContext("spring-mvc.xml", "spring-mybatis.xml");
        }
        return context;
    }

    public void setContext(ApplicationContext context) {
        SpringContextUtils.context = context;
    }

    public static <T> T getBean(String beanName) throws BeansException {
        return (T) getContext().getBean(beanName);
    }

    public static <T> T getBean(Class<T> tClass) throws BeansException {
        return (T) getContext().getBean(tClass);
    }

}
