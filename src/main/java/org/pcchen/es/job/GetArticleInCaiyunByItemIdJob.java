package org.pcchen.es.job;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.pcchen.es.domain.ESTest;
import org.pcchen.es.repository.CaiyunRepository;
import org.pcchen.es.repository.ESTestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 通过文章的itemId获取内容库中的文章媒体和发布时间
 *
 * @author cpc
 * @create 2018-09-03 15:22
 **/
@Component
public class GetArticleInCaiyunByItemIdJob {
    private static final Logger logger = LoggerFactory.getLogger(GetArticleInCaiyunByItemIdJob.class);
    private static ExecutorService workerExecutorService = Executors.newFixedThreadPool(100);

    private BlockingQueue<ESTest> cbContentBlockingQueue;

    @Autowired
    private CaiyunRepository caiyunRepository;
    @Autowired
    private ESTestRepository esTestRepository;

    //    @PostConstruct
    void init() {
        cbContentBlockingQueue = new LinkedBlockingDeque<ESTest>();
        for (int i = 0; i != 100; ++i) {
            GetArticleInCaiyunByItemIdWorker getArticleInCaiyunByItemIdWorker = new GetArticleInCaiyunByItemIdWorker(cbContentBlockingQueue, caiyunRepository, esTestRepository);
            workerExecutorService.execute(getArticleInCaiyunByItemIdWorker);
        }
    }

    //        @Scheduled(fixedRate = 1000*60*50)
//    @Scheduled(cron = "0 0 * * * ?")
    public void addTask() {
        logger.info("static的文章发送itemId查询caiyun中的文章媒体和发布时间--------开始");
        List<ESTest> all = esTestRepository.findAllByAtAndColumnNameIsNotNull("UAR-000371_354");
//        List<ESTest> all = esTestRepository.findAllByAt("UAR-000405_374");

        int i = 0;
        for (ESTest esTest : all) {
            i++;
            if (esTest.getCaiyunItemId() != null) {
                cbContentBlockingQueue.add(esTest);
            }
            if (i % 3000 == 0) {
                logger.info("发送了【" + i + "】条数据！");
            }
        }

        logger.info("static的文章发送itemId查询caiyun中的文章媒体和发布时间--------结束");
    }
}
