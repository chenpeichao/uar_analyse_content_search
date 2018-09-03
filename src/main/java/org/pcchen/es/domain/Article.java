package org.pcchen.es.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

/**
 * 内容库文章
 *
 * @author cpc
 * @create 2018-08-13 16:08
 **/
@Document(indexName = "caiyun", type = "spider_result")
public class Article implements Serializable {
    @Id
    private String id;
    private String mediaName;  //媒体
    private String publishTime;  //发表时间

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Article article = (Article) o;

        if (id != null ? !id.equals(article.id) : article.id != null) return false;
        if (mediaName != null ? !mediaName.equals(article.mediaName) : article.mediaName != null) return false;
        return publishTime != null ? publishTime.equals(article.publishTime) : article.publishTime == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (mediaName != null ? mediaName.hashCode() : 0);
        result = 31 * result + (publishTime != null ? publishTime.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", mediaName='" + mediaName + '\'' +
                ", publishTime='" + publishTime + '\'' +
                '}';
    }
}
