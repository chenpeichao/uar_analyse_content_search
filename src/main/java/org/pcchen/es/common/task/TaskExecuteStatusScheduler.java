package org.pcchen.es.common.task;

import org.pcchen.es.service.ContentSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 *
 * @author cpc
 * @create 2018-08-03 17:06
 **/
@Component
public class TaskExecuteStatusScheduler {
    @Autowired
    private ContentSearchService contentSearchService;

    //每整点执行一次--对于统计es库中数据落地本地数据库
//    @Scheduled(cron="0 0 * * * ?")
    @Scheduled(fixedRate=1000*60*50)
    public void updateTaskFinishStatus() {
        contentSearchService.executeData("20180801", "20180831", "UAR-000161_540");
    }

    /**
     * 使用blockingqueue消费，此处定时任务废弃
     */
//    @Scheduled(cron="0 0 * * * ?")
//    @Scheduled(fixedRate=1000*60*150)
//    //--对于本地数据库获取内容库中相关itemId的文章发布时间和文章媒体名称
//    public void updateArticlePublishFromMysql() {
//        contentSearchService.executeUpdateArticlePublishFromMysql("UAR-000405_374");
//    }
}
