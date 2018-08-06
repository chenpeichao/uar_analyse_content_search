package org.pcchen.es.repository;

import org.pcchen.es.domain.ESTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 数据分析师
 *
 * @author cpc
 * @create 2018-08-02 14:51
 **/
public interface ESTestRepository extends JpaRepository<ESTest, Integer> {
    @Query("from org.pcchen.es.domain.ESTest esTest where esTest.appAt = :appAt")
    public List<ESTest> findAllByAt(@Param("appAt")String appAt);
}
