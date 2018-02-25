package com.example.job;

import com.example.solr.server.SolrServer;

/**
 * Created by Administrator on 2017/9/7/007.
 */
public class DeleteSolr {
    public static void main(String[] args) {
        SolrServer solrServer = new SolrServer();
        try {
            solrServer.deleteDocumentByIdSolr(177402L);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
