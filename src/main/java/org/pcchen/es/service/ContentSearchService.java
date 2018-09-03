package org.pcchen.es.service;

/**
 * 运营分析中内容搜索
 *
 * @author cpc
 * @create 2018-08-03 16:22
 **/
public interface ContentSearchService {
    /**
     *
     * @param startDate
     * @param endDate
     * @param at
     */
    public void executeData(String startDate, String endDate, String at);

    public void executeUpdateArticlePublishFromMysql(String at);
}
