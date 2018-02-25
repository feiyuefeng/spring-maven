package com.example.solr.vo;

import java.util.List;

/**
 * Created by Administrator on 2017/8/27.
 */
public class SolrResultVo {
    private Long numFound;

    private List<Long> ids;

    public Long getNumFound() {
        return numFound;
    }

    public void setNumFound(Long numFound) {
        this.numFound = numFound;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }
}
