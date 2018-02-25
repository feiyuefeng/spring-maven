package com.example.job;

import com.example.pojo.dto.ResourceDto;
import com.example.service.ResourceService;
import com.example.utils.SpringContextUtils;

import java.text.DecimalFormat;

/**
 * Created by Administrator on 2017/9/7/007.
 */
public class InsertResource {
    public static void main(String[] args) {
        ResourceService resourceService = SpringContextUtils.getBean("resourceService");
        ResourceDto resourceInfo = new ResourceDto();
        DecimalFormat df = new DecimalFormat("#.00");
        int i = 5000000;
        while(i-- > 0){
            resourceInfo.setCommunity("传说小区");
            resourceInfo.setArea(Double.valueOf(df.format(Math.random() * 150 + 30)));
            resourceInfo.setBuildYear(2017);
            resourceInfo.setDescription("美好的地方哦");
            resourceInfo.setFloor(Double.valueOf(Math.random() * 10 + 1).intValue());
            resourceInfo.setHall(Double.valueOf(Math.random() * 2 + 1).intValue());
            resourceInfo.setLocation("东方路1217号");
            resourceInfo.setTotalFloor(12);
//            resourceInfo.setCreateTime(new Date());
//            resourceInfo.setPublishTime(new Date());
//            resourceInfo.setUpdateTime(new Date());
            resourceInfo.setPrice(Double.valueOf(df.format(Math.random() * 1500000 + 500000)));
            resourceInfo.setPublisher(123456L);
            resourceInfo.setRoom(Double.valueOf(Math.random() * 3 +1).intValue());
            resourceInfo.setTitle("这里环境可好了");
//            resourceInfo.setIsDelete(0);
            resourceService.addResource(resourceInfo);
        }
    }
}
