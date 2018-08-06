package org.pcchen.es.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * 数据分析师-指定应用的标识
 *
 * @author cpc
 * @create 2018-08-02 14:38
 **/
@Entity(name = "es_item_static")
public class ESTest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "app_at")
    private String appAt;
    @Column(name = "app_name")
    private String appName;
    @Column
    private String title;
    @Column
    private String url;
    @Column
    private Integer pv;
    @Column
    private Integer uv;
    @Column(name = "visit_time")
    private Double visitTime;
    @Column(name = "count_time")
    private Date countTime;
    @Column(name = "publish_time")
    private Date publishTime;
    @Column(name = "column_name")
    private String columnName;
    @Column(name = "column_id")
    private Integer columnId;
    @Column(name = "static_item_id")
    private String staticItemId;           //统计库item_id
    @Column(name = "caiyun_item_id")
    private String caiyunItemId;           //内容库item_id

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppAt() {
        return appAt;
    }

    public void setAppAt(String appAt) {
        this.appAt = appAt;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getPv() {
        return pv;
    }

    public void setPv(Integer pv) {
        this.pv = pv;
    }

    public Integer getUv() {
        return uv;
    }

    public void setUv(Integer uv) {
        this.uv = uv;
    }

    public Double getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(Double visitTime) {
        this.visitTime = visitTime;
    }

    public Date getCountTime() {
        return countTime;
    }

    public void setCountTime(Date countTime) {
        this.countTime = countTime;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getColumnId() {
        return columnId;
    }

    public void setColumnId(Integer columnId) {
        this.columnId = columnId;
    }

    public String getStaticItemId() {
        return staticItemId;
    }

    public void setStaticItemId(String staticItemId) {
        this.staticItemId = staticItemId;
    }

    public String getCaiyunItemId() {
        return caiyunItemId;
    }

    public void setCaiyunItemId(String caiyunItemId) {
        this.caiyunItemId = caiyunItemId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ESTest esTest = (ESTest) o;

        if (id != null ? !id.equals(esTest.id) : esTest.id != null) return false;
        if (appAt != null ? !appAt.equals(esTest.appAt) : esTest.appAt != null) return false;
        if (appName != null ? !appName.equals(esTest.appName) : esTest.appName != null) return false;
        if (title != null ? !title.equals(esTest.title) : esTest.title != null) return false;
        if (url != null ? !url.equals(esTest.url) : esTest.url != null) return false;
        if (pv != null ? !pv.equals(esTest.pv) : esTest.pv != null) return false;
        if (uv != null ? !uv.equals(esTest.uv) : esTest.uv != null) return false;
        if (visitTime != null ? !visitTime.equals(esTest.visitTime) : esTest.visitTime != null) return false;
        if (countTime != null ? !countTime.equals(esTest.countTime) : esTest.countTime != null) return false;
        if (publishTime != null ? !publishTime.equals(esTest.publishTime) : esTest.publishTime != null) return false;
        if (columnName != null ? !columnName.equals(esTest.columnName) : esTest.columnName != null) return false;
        if (columnId != null ? !columnId.equals(esTest.columnId) : esTest.columnId != null) return false;
        if (staticItemId != null ? !staticItemId.equals(esTest.staticItemId) : esTest.staticItemId != null)
            return false;
        return caiyunItemId != null ? caiyunItemId.equals(esTest.caiyunItemId) : esTest.caiyunItemId == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (appAt != null ? appAt.hashCode() : 0);
        result = 31 * result + (appName != null ? appName.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (pv != null ? pv.hashCode() : 0);
        result = 31 * result + (uv != null ? uv.hashCode() : 0);
        result = 31 * result + (visitTime != null ? visitTime.hashCode() : 0);
        result = 31 * result + (countTime != null ? countTime.hashCode() : 0);
        result = 31 * result + (publishTime != null ? publishTime.hashCode() : 0);
        result = 31 * result + (columnName != null ? columnName.hashCode() : 0);
        result = 31 * result + (columnId != null ? columnId.hashCode() : 0);
        result = 31 * result + (staticItemId != null ? staticItemId.hashCode() : 0);
        result = 31 * result + (caiyunItemId != null ? caiyunItemId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ESTest{" +
                "id=" + id +
                ", appAt='" + appAt + '\'' +
                ", appName='" + appName + '\'' +
                ", title='" + title + '\'' +
                ", url='" + url + '\'' +
                ", pv=" + pv +
                ", uv=" + uv +
                ", visitTime=" + visitTime +
                ", countTime=" + countTime +
                ", publishTime=" + publishTime +
                ", columnName='" + columnName + '\'' +
                ", columnId=" + columnId +
                ", staticItemId=" + staticItemId +
                ", caiyunItemId=" + caiyunItemId +
                '}';
    }
}
