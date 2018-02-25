package com.example.service;

import com.alibaba.fastjson.JSONObject;
import com.example.pojo.ResourceInfo;
import com.example.pojo.dto.ResourceDto;


/**
 * Created by Administrator on 2017/8/28.
 */
public interface ResourceService {
    Long addResource (ResourceDto resourceDto);

    JSONObject queryResourceBaseList(Long brokerId, Integer areaFilter,
                                     Integer priceFilter, Integer roomFilter, Integer sort, Integer page, Integer pageSize) throws Exception;

    ResourceInfo queryResourceDetail(Long brokerId, Long resourceId);

}
