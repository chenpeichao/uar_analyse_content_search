package org.pcchen.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 运营分析网站内容搜索中文章详情从es中获取
 *
 * @author cpc
 * @create 2018-08-03 16:14
 **/
@EnableAutoConfiguration
@EnableScheduling
@SpringBootApplication
public class UarAnalyseContentSearch {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(UarAnalyseContentSearch.class);
        springApplication.run(args);
    }
}
