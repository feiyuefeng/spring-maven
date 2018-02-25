package com.example.solr.server;

import com.example.solr.vo.SolrResourceSearchVo;
import com.example.solr.vo.SolrResourceVo;
import com.example.solr.vo.SolrResultVo;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fyf on 2017/8/26.
 */
public class SolrServer {
    private final static String SOLR_URL="http://10.249.5.20:8983/solr/mycore";

    /**
     * 创建SolrServer对象
     *
     * 该对象有两个可以使用，都是线程安全的
     * 1、CommonsHttpSolrServer：启动web服务器使用的，通过http请求的
     * 2、 EmbeddedSolrServer：内嵌式的，导入solr的jar包就可以使用了
     * 3、solr 4.0之后好像添加了不少东西，其中CommonsHttpSolrServer这个类改名为HttpSolrClient
     *
     * @return
     */
    public HttpSolrClient createSolrServer(){
        return new HttpSolrClient(SOLR_URL);
    }

    /**
     * 往索引库添加文档
     * @throws IOException
     * @throws SolrServerException
     */
    public void addResourceSolr(SolrResourceVo solrResourceVo) throws SolrServerException, IOException {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", solrResourceVo.getId());
        document.addField("price", solrResourceVo.getPrice());
        document.addField("area", solrResourceVo.getArea());
        document.addField("room", solrResourceVo.getRoom());
        document.addField("publish_time", solrResourceVo.getPublish_time());
        HttpSolrClient solr = new HttpSolrClient(SOLR_URL);
        solr.add(document);
        solr.commit();
        solr.close();
    }


    /**
     * 根据id从索引库删除文档
     */
    public void deleteDocumentByIdSolr(Long resourceId) throws Exception {
        HttpSolrClient server = new HttpSolrClient(SOLR_URL);
        //删除文档
        server.deleteById(resourceId.toString());
        server.commit();
        server.close();
    }

    /**
     * 查询
     * @throws Exception
     */
    public SolrResultVo queryResourceSolr(SolrResourceSearchVo solrResourceSearchVo) throws Exception{
        HttpSolrClient solrServer = new HttpSolrClient(SOLR_URL);
        SolrResultVo result = new SolrResultVo();
        List<Long> ids = new LinkedList<>();
        SolrQuery query = new SolrQuery();
        //下面设置solr查询参数
        query.set("q", "*:*");// 参数q  查询所有
        setQueryFilter(query, solrResourceSearchVo);
        setQuerySort(query, solrResourceSearchVo);
        setQueryPage(query, solrResourceSearchVo);
        QueryResponse response = solrServer.query(query);
        SolrDocumentList solrDocumentList = response.getResults();
        Long total = solrDocumentList.getNumFound();
        result.setNumFound(total);
        for (SolrDocument doc : solrDocumentList) {
            ids.add(Long.valueOf(doc.get("id").toString()));
        }
        result.setIds(ids);
        return result;
    }

    private void setQueryFilter(SolrQuery solrQuery, SolrResourceSearchVo solrResourceSearchVo){
        if(solrResourceSearchVo.getPrice() != null){
            switch (solrResourceSearchVo.getPrice()){
                case 1:
                    solrQuery.addFilterQuery("price:[* TO 1000000]");
                    break;
                case 2:
                    solrQuery.addFilterQuery("price:[1000000 TO 1500000]");
                    break;
                case 3:
                    solrQuery.addFilterQuery("price:[1500000 TO *]");
                    break;
                    default:
                        solrQuery.addFilterQuery("price:[* TO *]");
            }
        }
        if(solrResourceSearchVo.getArea() != null) {
            switch (solrResourceSearchVo.getArea()) {
                case 1:
                    solrQuery.addFilterQuery("area:[* TO 50]");
                    break;
                case 2:
                    solrQuery.addFilterQuery("area:[50 TO 100]");
                    break;
                case 3:
                    solrQuery.addFilterQuery("area:[100 TO *]");
                    break;
                    default:
                        solrQuery.addFilterQuery("area:[* TO *]");
            }
        }
        if(solrResourceSearchVo.getRoom() != null) {
            switch (solrResourceSearchVo.getRoom()) {
                case 1:
                    solrQuery.addFilterQuery("room:1");
                    break;
                case 2:
                    solrQuery.addFilterQuery("room:2");
                    break;
                case 3:
                    solrQuery.addFilterQuery("room:3");
                    break;
                case 4:
                    solrQuery.addFilterQuery("room:4");
                    break;
                case 5:
                    solrQuery.addFilterQuery("room:5");
                    break;
                    default:
            }
        }
    }

    private void setQuerySort(SolrQuery solrQuery, SolrResourceSearchVo solrResourceSearchVo){
        if(solrResourceSearchVo.getSort() != null){
            switch (solrResourceSearchVo.getSort()){
                case 1:
                    solrQuery.setSort("area", SolrQuery.ORDER.asc);
                    break;
                case 2:
                    solrQuery.setSort("area", SolrQuery.ORDER.desc);
                    break;
                case 3:
                    solrQuery.setSort("price", SolrQuery.ORDER.asc);
                    break;
                case 4:
                    solrQuery.setSort("price", SolrQuery.ORDER.desc);
                    break;
                case 5:
                    solrQuery.setSort("publish_time", SolrQuery.ORDER.asc);
                    break;
                case 6:
                    solrQuery.setSort("publish_time", SolrQuery.ORDER.desc);
                    break;
            }
        }
    }

    private void setQueryPage(SolrQuery solrQuery, SolrResourceSearchVo solrResourceSearchVo){
        if(solrResourceSearchVo.getPage() == null ){
            solrQuery.setStart(0);
        }else {
            solrQuery.setStart((solrResourceSearchVo.getPage() - 1) * solrResourceSearchVo.getPageSize());
        }
        if(solrResourceSearchVo.getPageSize() == null){
            solrQuery.setRows(10);
        }else {
            solrQuery.setRows(solrResourceSearchVo.getPageSize());
        }
    }

    public static void main(String[] args) throws Exception {
        SolrServer solr = new SolrServer();
        SolrResourceSearchVo solrResourceSearchVo = new SolrResourceSearchVo();
        solrResourceSearchVo.setPrice(3);
//        SolrResourceVo solrResourceVo = new SolrResourceVo();
//        solrResourceVo.setId(5L);
//        solrResourceVo.setPrice(566.0);
//        solrResourceVo.setArea(140.1);
//        solrResourceVo.setRoom(4);
//        solrResourceVo.setPublish_time(new Date());
//        //solr.createSolrServer();
//        solr.addResource(solrResourceVo);
//        solr.deleteDocumentById(4l);
        solr.queryResourceSolr(solrResourceSearchVo);
    }
}
