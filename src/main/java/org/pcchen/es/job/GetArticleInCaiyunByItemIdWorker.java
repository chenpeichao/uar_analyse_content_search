package org.pcchen.es.job;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.pcchen.es.domain.Article;
import org.pcchen.es.domain.ESTest;
import org.pcchen.es.repository.CaiyunRepository;
import org.pcchen.es.repository.ESTestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * @author cpc
 * @create 2018-09-03 15:33
 **/
public class GetArticleInCaiyunByItemIdWorker implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(GetArticleInCaiyunByItemIdWorker.class);

    private BlockingQueue<ESTest> cbContentBlockingQueue;

    private CaiyunRepository caiyunRepository;
    private ESTestRepository esTestRepository;

    public GetArticleInCaiyunByItemIdWorker(BlockingQueue cbContentBlockingQueue, CaiyunRepository caiyunRepository, ESTestRepository esTestRepository) {
        this.caiyunRepository = caiyunRepository;
        this.cbContentBlockingQueue = cbContentBlockingQueue;
        this.esTestRepository = esTestRepository;
    }

    public void run() {
        ESTest esTest = null;
        try {
            while ((esTest = cbContentBlockingQueue.take()) != null) {
                if (cbContentBlockingQueue.size() % 1000 == 0) {
                    System.out.println("还有【" + cbContentBlockingQueue.size() + "】未处理");
                }
                String itemId = esTest.getCaiyunItemId();
                BoolQueryBuilder builder = QueryBuilders.boolQuery();
                builder.must(QueryBuilders.termQuery("itemId", itemId));

//        FieldSortBuilder sort = SortBuilders.fieldSort("age").order(SortOrder.DESC);
                //设置分页
                //====注意!es的分页和Hibernate一样api是从第0页开始的=========
                PageRequest page = new PageRequest(0, 2);

                NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
                //将搜索条件设置到构建中
                nativeSearchQueryBuilder.withQuery(builder);
                //将分页设置到构建中
                nativeSearchQueryBuilder.withPageable(page);

                //将排序设置到构建中
//        nativeSearchQueryBuilder.withSort(sort);
                //生产NativeSearchQuery
                NativeSearchQuery query = nativeSearchQueryBuilder.build();
                //执行,返回包装结果的分页
                Page<Article> resutlList = caiyunRepository.search(query);
                List<Article> content = resutlList.getContent();
                if (content.size() == 1) {
                    esTest.setColumnName(content.get(0).getMediaName());
                    try {
                        esTest.setPublishTime(new SimpleDateFormat("yyyyMMddHHmmss").parse(content.get(0).getPublishTime()));
                    } catch (ParseException e) {
                        logger.error("时间解析！", e);
                    }
                } else if (content.size() < 1) {
                    logger.error("itemId为【" + itemId + "】对应的没有内容库的文章！");
                } else {
                    logger.error("itemId为【" + itemId + "】对应的内容库的文章多余1篇！");
                }
                esTestRepository.save(esTest);
            }
        } catch (InterruptedException e) {
            logger.error("消息消费异常！", e);
        }
    }
}
