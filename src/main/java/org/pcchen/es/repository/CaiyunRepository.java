package org.pcchen.es.repository;

import org.pcchen.es.domain.Article;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

/**
 * Caiyun操作esrepository
 *
 * @author cpc
 * @create 2018-09-03 14:49
 **/
@Component
public interface CaiyunRepository extends ElasticsearchRepository<Article, String> {
}
