package com.example.dao;


import com.example.pojo.ResourceInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResourceDAO {
    int deleteByPrimaryKey(Long id);

    int insert(ResourceInfo resourceInfo);

    int insertSelective(ResourceInfo record);

    ResourceInfo selectByPrimaryKey(Long id);

    List<ResourceInfo> selectByPrimaryKeys(@Param("ids") List<Long> ids);

    int updateByPrimaryKeySelective(ResourceInfo record);

    int updateByPrimaryKey(ResourceInfo record);
}