package org.pcchen.es.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.pcchen.es.domain.ESTest;
import org.pcchen.es.repository.ESTestRepository;
import org.pcchen.es.service.ContentSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.net.*;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 运营分析内容搜索
 *
 * @author cpc
 * @create 2018-08-03 16:23
 **/
@Service
@Transactional
public class ContentSearchServiceImpl implements ContentSearchService {
    @Autowired
    private ESTestRepository esTestRepository;

    /**
     *
     * @param startDate
     * @param endDate
     * @param at
     */
    public void executeData(String startDate, String endDate, String at) {
        try {
            Settings settings = Settings.settingsBuilder().put("cluster.name", "elasticsearch_statistic").put("client.transport.sniff", true).build();

            TransportClient client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.32.46"), 9301));
            QueryBuilder queryBuilder = QueryBuilders.termQuery("at", at);
            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("day").gte(startDate).lte(endDate);
//            QueryBuilder queryBuilder2 = QueryBuilders.termQuery("item_id","45350775a3acdad24cea7a2e32a320b3");


            //es中使用setQuery不能多条件查询-----使用boolQueryBuilder多条件查询
            BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
            mustQuery.must(queryBuilder);
            mustQuery.must(rangeQueryBuilder);

            SearchResponse scrollResponse = client.prepareSearch("item_statistic").setTypes("item_basic_statistic").setQuery(mustQuery)
                    .setSearchType(SearchType.SCAN).setSize(2000).setScroll(TimeValue.timeValueMinutes(1))
                    .execute().actionGet();
            long count = scrollResponse.getHits().getTotalHits();//第一次不返回数据
            List<ESTest> esTestList = new ArrayList<ESTest>();
            for (int i = 0, sum = 0; sum < count; i++) {
                scrollResponse = client.prepareSearchScroll(scrollResponse.getScrollId())
                        .setScroll(TimeValue.timeValueMinutes(8))
                        .execute().actionGet();
                sum += scrollResponse.getHits().hits().length;
                System.out.println("总量" + count + " 已经查到" + sum);
                Iterator<SearchHit> hitIterator = scrollResponse.getHits().iterator();
                while (hitIterator.hasNext()) {
                    SearchHit next = hitIterator.next();
                    Map<String, Object> source = next.getSource();
                    ESTest esTest = new ESTest();
                    esTest.setUrl(source.get("uri").toString());
                    esTest.setTitle(source.get("tt").toString());
                    esTest.setAppAt(source.get("at").toString());
                    if (!"default".equals(source.get("cl").toString())) {
                        esTest.setColumnId(Integer.parseInt(source.get("cl").toString()));
                    } else {
                        esTest.setColumnId(-1);
                    }
                    esTest.setPv(Integer.parseInt(source.get("pv").toString()));
                    esTest.setUv(Integer.parseInt(source.get("uv").toString()));
                    esTest.setStaticItemId(source.get("item_id").toString());
                    esTest.setVisitTime(Double.parseDouble(source.get("visit_time").toString()));
                    try {
                        esTest.setCountTime(new SimpleDateFormat("yyyyMMdd").parse(source.get("day").toString()));
                    } catch (Exception e) {
                        System.err.println("文章统计时间转换错误【"+source.get("day").toString()+"】" + source.get("tt").toString());
                    }
                    try {
                        URL url = new URL(esTest.getUrl());
                        String urlQueryStr = url.getQuery();
                        if(StringUtils.isNotBlank(urlQueryStr)) {
                            String[] queryStrSplit = urlQueryStr.split("&");
                            for(String queryKV : queryStrSplit) {
                                String[] paramSplit = queryKV.split("=");
                                if("itemId".equals(paramSplit[0])) {
                                    esTest.setCaiyunItemId(paramSplit[1]);
                                }
                            }
                        }
                    } catch (MalformedURLException e) {
                        System.err.println("文章url解析内容库itemId错误【"+source.get("day").toString()+"】" + esTest.getUrl());
                    }
                    esTestList.add(esTest);
                }
            }
            esTestRepository.save(esTestList);
            System.out.println("保存到数据库的总记录数为：" + esTestList.size());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

//    /**
//     *
//     * @param startDate
//     * @param endDate
//     * @param at
//     */
//    public void executeData1(String startDate, String endDate, String at) {
//        try {
//            Settings settings = Settings.settingsBuilder().put("cluster.name","elasticsearch_statistic").put("client.transport.sniff", true).build();
//
//            TransportClient client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.32.46"), 9301));
//            QueryBuilder queryBuilder = QueryBuilders.termQuery("at",at);
//            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("day").gte(startDate).lte(endDate);
////            WildcardQueryBuilder wildcardQueryBuilder = QueryBuilders.wildcardQuery("uri", "*http*");
//
//
//            //es中使用setQuery不能多条件查询-----使用boolQueryBuilder多条件查询
//            BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
//            mustQuery.must(queryBuilder);
//            mustQuery.must(rangeQueryBuilder);
////			mustQuery.must(wildcardQueryBuilder);
//            Long count = client.prepareSearch("item_statistic").setTypes("item_basic_statistic")
//                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH).setQuery(mustQuery).execute().actionGet().getHits().getTotalHits();
//System.err.println("总记录数为：" + count);
//            SearchRequestBuilder requestBuilder = client.prepareSearch("item_statistic").setTypes("item_basic_statistic")
//                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                    .setQuery(mustQuery);
//
//            List<ESTest> esTestList = new ArrayList<ESTest>();
//            for (int i = 0, sum = 0; sum < count; i++) {
//                int size = 5000;
//                SearchResponse sr = requestBuilder.setFrom(i * size).setSize(size).execute().actionGet();
//                Iterator<SearchHit> hitIterator = sr.getHits().iterator();
//                while(hitIterator.hasNext()) {
//                    SearchHit next = hitIterator.next();
//                    Map<String, Object> source = next.getSource();
//                    ESTest esTest = new ESTest();
//                    esTest.setUrl(source.get("uri").toString());
//                    esTest.setTitle(source.get("tt").toString());
//                    esTest.setAppAt(source.get("at").toString());
//                    if(!"default".equals(source.get("cl").toString())) {
//                        esTest.setColumnId(Integer.parseInt(source.get("cl").toString()));
//                    } else {
//                        esTest.setColumnId(-1);
//                    }
//                    esTest.setPv(Integer.parseInt(source.get("pv").toString()));
//                    esTest.setUv(Integer.parseInt(source.get("uv").toString()));
//                    esTest.setVisitTime(Double.parseDouble(source.get("visit_time").toString()));
//                    try {
//                        esTest.setCountTime(new SimpleDateFormat("yyyyMMdd").parse(source.get("day").toString()));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.err.println(source.get("tt").toString());
//                        throw  new RuntimeException("哈哈");
//                    }
//                    esTestList.add(esTest);
//                }
//                System.out.println("总量" + count + " i=" + i + " 已经查到=" + sum);
//            }
//            esTestRepository.save(esTestList);
//
////            SearchResponse response = client.prepareSearch("item_statistic")
////                    .setTypes("item_basic_statistic")
////                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//////                    .setSearchType(SearchType.SCAN)
////                    .setQuery(mustQuery) //查询所有
////                    .setFrom(10000)
////                    .setSize(10000)
////                    .execute()
////                    .actionGet();
////            SearchHits hits = response.getHits();
////            Iterator<SearchHit> iterator1 = hits.iterator();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//    }

//    /**
//     *
//     * @param startDate
//     * @param endDate
//     * @param at
//     */
//    public void executeData2(String startDate, String endDate, String at) {
//        try {
//            Settings settings = Settings.settingsBuilder().put("cluster.name","elasticsearch_statistic").put("client.transport.sniff", true).build();
//
//            TransportClient client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.32.46"), 9301));
//            QueryBuilder queryBuilder = QueryBuilders.termQuery("at",at);
//            RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("day").gte(startDate).lte(endDate);
//            QueryBuilder queryBuilder2 = QueryBuilders.termQuery("item_id","45350775a3acdad24cea7a2e32a320b3");
//
//
//            //es中使用setQuery不能多条件查询-----使用boolQueryBuilder多条件查询
//            BoolQueryBuilder mustQuery = QueryBuilders.boolQuery();
//            mustQuery.must(queryBuilder);
//            mustQuery.must(rangeQueryBuilder);
////			mustQuery.must(wildcardQueryBuilder);
//            mustQuery.must(queryBuilder2);
//
//            SearchRequestBuilder srb = null;
//            SearchResponse searchResponse = null;
//            srb = client.prepareSearch("item_statistic")
//                    .setTypes("item_basic_statistic")
//                    .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
//                    .setSize(5000)
//                    .setScroll(TimeValue.timeValueMinutes(2))
//                    .setQuery(mustQuery);
//
//            searchResponse = srb.execute().actionGet();
//            System.out.println("total count is " + searchResponse.getHits().getTotalHits());
//            List<ESTest> esTestList = new ArrayList<ESTest>();
//            Iterator<SearchHit> hitIterator = searchResponse.getHits().iterator();
//            while(true) {
//
//                for (SearchHit hit : searchResponse.getHits()) {
//                    Map<String, Object> source = hit.getSource();
//                    ESTest esTest = new ESTest();
//                    esTest.setUrl(source.get("uri").toString());
//                    esTest.setTitle(source.get("tt").toString());
//                    esTest.setAppAt(source.get("at").toString());
//                    if(!"default".equals(source.get("cl").toString())) {
//                        esTest.setColumnId(Integer.parseInt(source.get("cl").toString()));
//                    } else {
//                        esTest.setColumnId(-1);
//                    }
//                    esTest.setPv(Integer.parseInt(source.get("pv").toString()));
//                    esTest.setUv(Integer.parseInt(source.get("uv").toString()));
//                    esTest.setVisitTime(Double.parseDouble(source.get("visit_time").toString()));
//
//                    try {
//
//                        URI uri = new URI(source.get("uri").toString());
//                        List<NameValuePair> parse = URLEncodedUtils.parse(uri, Charset.forName("UTF-8"));
//                        for(NameValuePair nameValuePair : parse) {
//                            if(StringUtils.isNotBlank(nameValuePair.getName()) || "itemId".equals(nameValuePair.getName())) {
//                                executeCaiyunData(nameValuePair.getValue(), esTest);
//                            }
//                        }
//                    } catch (Exception e) {
//                        System.err.println("url解析异常==" + source.get("uri").toString());
//                        e.printStackTrace();
//                    }
//
//                    try {
//                        esTest.setCountTime(new SimpleDateFormat("yyyyMMdd").parse(source.get("day").toString()));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        System.err.println(source.get("tt").toString());
//                        throw  new RuntimeException("哈哈");
//                    }
//                    esTestList.add(esTest);
//                }
//                searchResponse = client
//                        .prepareSearchScroll(searchResponse.getScrollId())
//                        .setScroll(TimeValue.timeValueMinutes(6)).execute().actionGet();
//                System.err.println("保存到mysql的记录为：" + esTestList.size());
//                esTestRepository.save(esTestList);
//                if (searchResponse.getHits().getHits().length == 0) {
//                    break;
//                }
//            }
//
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//    }

    public void executeUpdateArticlePublishFromMysql() {
        List<ESTest> all = esTestRepository.findAllByAt("UAR-000405_374");
        List<ESTest> result = new ArrayList<ESTest>();
        for(ESTest esTest : all) {
            try {
                URI uri = new URI(esTest.getUrl());
                List<NameValuePair> parse = URLEncodedUtils.parse(uri, Charset.forName("UTF-8"));
                for(NameValuePair nameValuePair : parse) {
                    if(StringUtils.isNotBlank(nameValuePair.getName()) || "itemId".equals(nameValuePair.getName())) {
                        executeCaiyunData(nameValuePair.getValue(), esTest);
                    }
                }
                result.add(esTest);
            } catch (Exception e) {
                System.err.println("url解析异常==" + esTest.getUrl());
                e.printStackTrace();
            }
        }
        esTestRepository.save(result);
    }

    /**
     * 根据itemId获取内容库中文章的基本信息
     * @param itemId
     */
    public void executeCaiyunData(String itemId, ESTest esTest) throws Exception{
        Settings settings = Settings.settingsBuilder().put("cluster.name","elasticsearch_caiyun").put("client.transport.sniff", true).build();

        TransportClient client = TransportClient.builder().settings(settings).build().addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("10.10.32.41"), 9300));
        QueryBuilder queryBuilder = QueryBuilders.termQuery("itemId",itemId);

        SearchResponse response = client.prepareSearch("caiyun")
                .setTypes("spider_result")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .setSize(10000)
                .execute()
                .actionGet();

        SearchHits hits = response.getHits();
        Iterator<SearchHit> iterator1 = hits.iterator();
        System.out.println(response.getHits().getTotalHits());
        while(iterator1.hasNext()) {
            SearchHit next = iterator1.next();
            Map<String, Object> source = next.getSource();
            esTest.setColumnName(source.get("mediaName").toString());
            try {
                esTest.setPublishTime(DateUtils.parseDate(source.get("publishTime").toString(), "yyyyMMddHHmmss"));
            } catch (ParseException e) {
                System.err.println("内容库文章发布时间错误" + "【"+ esTest.getUrl() +"】");
                e.printStackTrace();
            }
        }
    }
}
