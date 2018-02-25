package com.example.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.Result.WebResult;
import com.example.pojo.ResourceInfo;
import com.example.service.ResourceService;
import com.example.constants.ResultStatus;
import com.example.pojo.dto.ResourceBaseDto;
import com.example.pojo.dto.ResourceDto;
import com.example.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;


/**
 * @author fyf
 * @create 2016-07-23-20:20
 */
@Controller
@RequestMapping("/resource")
public class ResourceController {
    @Resource
    private ResourceService resourceService;
    private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);
    @ResponseBody
    @RequestMapping(value = "/addResource", method = RequestMethod.POST)
    public ResponseEntity<WebResult> addResource(@RequestBody ResourceDto resourceDto){
        Long resourceId = resourceService.addResource(resourceDto);
        logger.info("add a resource :" + resourceId);
        return new ResponseEntity<>(new WebResult(ResultStatus.SUCCESS, resourceId), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/listResourceForBroker", method = RequestMethod.GET)
    public ResponseEntity<WebResult> listResourceForBroker(Long brokerId, Integer areaFilter, Integer priceFilter, Integer roomFilter,
               Integer sort, Integer page, Integer pageSize){
        try{
            JSONObject jsonObject = resourceService.queryResourceBaseList(brokerId, areaFilter, priceFilter, roomFilter, sort, page, pageSize);
            logger.info("query resources :");
            return new ResponseEntity<>(new WebResult(ResultStatus.SUCCESS, getListResult(jsonObject)), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(new WebResult(ResultStatus.FAILURE), HttpStatus.OK);
    }

    @ResponseBody
    @RequestMapping(value = "/detailResource", method = RequestMethod.GET)
    public ResponseEntity<WebResult> listResourceForBroker(Long brokerId, Long resourceId) {
        try {
            ResourceInfo resourceInfo =  resourceService.queryResourceDetail(brokerId, resourceId);
//            logger.info("获取房源详情" + resourceInfo.getId() + "经纪人：" + brokerId);
            return new ResponseEntity<>(new WebResult(ResultStatus.SUCCESS, getDetailResult(resourceInfo)), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn("查询房源详情失败", e.getMessage());
        }
        return new ResponseEntity<>(new WebResult(ResultStatus.FAILURE), HttpStatus.OK);
    }

    private Object getDetailResult(ResourceInfo resourceInfo) {
        JSONObject result = new JSONObject();
        result.put("id", resourceInfo.getId());
        result.put("title", resourceInfo.getTitle());
        result.put("area", resourceInfo.getArea() + "㎡");
        result.put("room", resourceInfo.getRoom() + "室" + resourceInfo.getHall() + "厅");
        Double price = resourceInfo.getPrice()/resourceInfo.getArea();
        DecimalFormat df = new DecimalFormat("#.00");
        result.put("price", df.format(price) + "元/㎡");
        result.put("floor", resourceInfo.getFloor() + "楼/共" + resourceInfo.getTotalFloor() + "楼");
        result.put("publisher", "费越峰");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        result.put("publishTime", simpleDateFormat.format(resourceInfo.getCreateTime()));
        result.put("community", resourceInfo.getCommunity());
        result.put("buildYear", resourceInfo.getBuildYear());
        result.put("location", resourceInfo.getLocation());
        result.put("description", resourceInfo.getDescription());
        return result;
    }


    private JSONObject getListResult(JSONObject json) {
        List<ResourceBaseDto> resourceBaseList = new LinkedList<>();
        try {
            resourceBaseList = JsonUtil.parseList(json.get("list").toString(), ResourceBaseDto.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = new JSONObject();
        JSONObject result = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        for(ResourceBaseDto resourceBaseDto : resourceBaseList) {
            jsonObject.put("resourceId", resourceBaseDto.getResourceId());
            jsonObject.put("title", resourceBaseDto.getTitle());
            DecimalFormat df = new DecimalFormat("#.00");
            jsonObject.put("price", df.format(resourceBaseDto.getPrice()/10000) + "万元");
            jsonObject.put("area", resourceBaseDto.getArea() + "㎡");
            jsonObject.put("room", resourceBaseDto.getRoom() + "室" + resourceBaseDto.getHall() + "厅");
            jsonObject.put("floor", resourceBaseDto.getFloor() + "楼/共" + resourceBaseDto.getTotalFloor() + "楼");
            jsonObject.put("publisher", "费越峰");
            jsonArray.add(jsonObject);
        }
        result.put("list", jsonArray);
        result.put("size", json.get("total"));
        return result;
    }
}