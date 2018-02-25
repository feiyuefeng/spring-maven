package com.example.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.dao.ResourceDAO;
import com.example.pojo.ResourceInfo;
import com.example.service.ResourceService;
import com.example.pojo.dto.ResourceBaseDto;
import com.example.pojo.dto.ResourceDto;
import com.example.solr.server.SolrServer;
import com.example.solr.vo.SolrResourceSearchVo;
import com.example.solr.vo.SolrResourceVo;
import com.example.solr.vo.SolrResultVo;
import com.example.utils.JsonUtil;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Created by fyf on 2017/8/28.
 */
@Service("resourceService")
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private ResourceDAO resourceDAO;
    private SolrServer solrServer = new SolrServer();
    private static final Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);


    private Long addResource(ResourceInfo resourceInfo) {
        resourceDAO.insert(resourceInfo);
        try {
            solrServer.addResourceSolr(getSolrResourceVo(resourceInfo));
        }catch (SolrServerException sse) {
            sse.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return resourceInfo.getId();
    }

    private SolrResourceVo getSolrResourceVo(ResourceInfo resourceInfo){
        SolrResourceVo solrResourceVo = new SolrResourceVo();
        solrResourceVo.setPublish_time(resourceInfo.getCreateTime());
        solrResourceVo.setRoom(resourceInfo.getRoom());
        solrResourceVo.setArea(resourceInfo.getArea());
        solrResourceVo.setPrice(resourceInfo.getPrice());
        solrResourceVo.setId(resourceInfo.getId());
        return solrResourceVo;
    }

    @Override
    public Long addResource(ResourceDto resourceDto) {
        ResourceInfo resourceInfo = new ResourceInfo();
        resourceInfo.setArea(resourceDto.getArea());
        resourceInfo.setBuildYear(resourceDto.getBuildYear());
        resourceInfo.setCreateTime(new Date());
        resourceInfo.setUpdateTime(new Date());
        resourceInfo.setDescription(resourceDto.getDescription());
        resourceInfo.setCommunity(resourceDto.getCommunity());
        resourceInfo.setFloor(resourceDto.getFloor());
        resourceInfo.setTotalFloor(resourceDto.getTotalFloor());
        resourceInfo.setHall(resourceDto.getHall());
        resourceInfo.setRoom(resourceDto.getRoom());
        resourceInfo.setIsDelete(0);
        resourceInfo.setLocation(resourceDto.getLocation());
        resourceInfo.setPrice(resourceDto.getPrice());
        resourceInfo.setTitle(resourceDto.getTitle());
        resourceInfo.setPublisher(resourceDto.getPublisher());
        return this.addResource(resourceInfo);
    }

    private JSONObject queryResourceList(Long brokerId, Integer areaFilter,
                                         Integer priceFilter, Integer roomFilter, Integer sort, Integer page, Integer pageSize) {
        SolrResultVo solrResultVo = new SolrResultVo();
        JSONObject result = new JSONObject();
        Long startTime;
        Long endTime;
        try {
            startTime=System.currentTimeMillis();
            solrResultVo = solrServer.queryResourceSolr(getSolrResourceSearchVo(brokerId, areaFilter, priceFilter, roomFilter, sort, page, pageSize));
            endTime=System.currentTimeMillis();
            logger.info("Solr执行时间为：" + (endTime - startTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray resultList = new JSONArray();
        List<Long> resultIds = solrResultVo.getIds();
        List<ResourceInfo> resourceInfos = new LinkedList<>();
        Map<Long, ResourceInfo> resourceMap = new HashMap<>();
        startTime=System.currentTimeMillis();
        if(resultIds.size() > 0){
            resourceInfos = resourceDAO.selectByPrimaryKeys(resultIds);
        }
        endTime = System.currentTimeMillis();
        logger.info("对应id查数据库时间：" + (endTime - startTime) + "ms");
        startTime=System.currentTimeMillis();
        for(ResourceInfo resourceInfo : resourceInfos) {
            resourceMap.put(resourceInfo.getId(), resourceInfo);
        }
        endTime = System.currentTimeMillis();
        logger.info("列表转Map时间为：" + (endTime - startTime) + "ms");
        startTime=System.currentTimeMillis();
        for(Long id : resultIds) {
            ResourceInfo resourceInfo = resourceMap.get(id);
            ResourceDto resourceDto = new ResourceDto();
            resourceDto.setArea(resourceInfo.getArea());
            resourceDto.setBuildYear(resourceInfo.getBuildYear());
            resourceDto.setCommunity(resourceInfo.getCommunity());
            resourceDto.setDescription(resourceInfo.getDescription());
            resourceDto.setFloor(resourceInfo.getFloor());
            resourceDto.setHall(resourceInfo.getHall());
            resourceDto.setLocation(resourceInfo.getLocation());
            resourceDto.setPrice(resourceInfo.getPrice());
            resourceDto.setPublisher(resourceInfo.getPublisher());
            resourceDto.setPublishTime(resourceInfo.getCreateTime().getTime());
            resourceDto.setRoom(resourceInfo.getRoom());
            resourceDto.setTitle(resourceInfo.getTitle());
            resourceDto.setTotalFloor(resourceInfo.getTotalFloor());
            resourceDto.setId(resourceInfo.getId());
            resultList.add(resourceDto);
        }
        endTime = System.currentTimeMillis();
        logger.info("排序时间为：" + (endTime - startTime) + "ms");
//        for(Long id : resultIds) {
//            for(ResourceInfo resourceInfo : resourceInfos) {
//                if(resourceInfo.getId().equals(id)){
//                    ResourceDto resourceDto = new ResourceDto();
//                    resourceDto.setArea(resourceInfo.getArea());
//                    resourceDto.setBuildYear(resourceInfo.getBuildYear());
//                    resourceDto.setCommunity(resourceInfo.getCommunity());
//                    resourceDto.setDescription(resourceInfo.getDescription());
//                    resourceDto.setFloor(resourceInfo.getFloor());
//                    resourceDto.setHall(resourceInfo.getHall());
//                    resourceDto.setLocation(resourceInfo.getLocation());
//                    resourceDto.setPrice(resourceInfo.getPrice());
//                    resourceDto.setPublisher(resourceInfo.getPublisher());
//                    resourceDto.setPublishTime(resourceInfo.getCreateTime().getTime());
//                    resourceDto.setRoom(resourceInfo.getRoom());
//                    resourceDto.setTitle(resourceInfo.getTitle());
//                    resourceDto.setTotalFloor(resourceInfo.getTotalFloor());
//                    resourceDto.setId(resourceInfo.getId());
//                    resultList.add(resourceDto);
//                }
//            }
//        }
        result.put("list", resultList);
        result.put("total", solrResultVo.getNumFound());
        return result;
    }

    private SolrResourceSearchVo getSolrResourceSearchVo(Long brokerId, Integer areaFilter,
                                                         Integer priceFilter, Integer roomFilter, Integer sort, Integer page, Integer pageSize) {
        return new SolrResourceSearchVo(priceFilter, areaFilter, roomFilter, page, pageSize, sort);
    }

    @Override
    public JSONObject queryResourceBaseList(Long brokerId, Integer areaFilter,
           Integer priceFilter, Integer roomFilter, Integer sort, Integer page, Integer pageSize) throws Exception {
        JSONArray resultList = new JSONArray();
        JSONObject jsonObject = this.queryResourceList(brokerId, areaFilter, priceFilter, roomFilter, sort, page, pageSize);
        JSONObject result = new JSONObject();
        String resourceList = jsonObject.get("list").toString();
        List<ResourceDto> resourceInfos = JsonUtil.parseList(resourceList, ResourceDto.class);
        for(ResourceDto resourceInfo : resourceInfos) {
            ResourceBaseDto resourceBaseDto = new ResourceBaseDto();
            resourceBaseDto.setPrice(resourceInfo.getPrice());
            resourceBaseDto.setArea(resourceInfo.getArea());
            resourceBaseDto.setFloor(resourceInfo.getFloor());
            resourceBaseDto.setHall(resourceInfo.getHall());
            resourceBaseDto.setPublisher(resourceInfo.getPublisher());
            resourceBaseDto.setResourceId(resourceInfo.getId());
            resourceBaseDto.setRoom(resourceInfo.getRoom());
            resourceBaseDto.setTitle(resourceInfo.getTitle());
            resourceBaseDto.setTotalFloor(resourceInfo.getTotalFloor());
            resultList.add(resourceBaseDto);
        }
        result.put("list", resultList);
        result.put("total", jsonObject.get("total"));
        return result;
    }

    @Override
    public ResourceInfo queryResourceDetail(Long brokerId, Long resourceId) {
        return this.queryResourceDetail(resourceId);
    }

    private ResourceInfo queryResourceDetail(Long resourceId) {
        return resourceDAO.selectByPrimaryKey(resourceId);
    }

}
