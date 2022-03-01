package com.tj.bigdata.analysis.mapper;

import org.apache.ibatis.annotations.InsertProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

@RegisterMapper
public interface SpecialBatchMapper<T> {

    /**
     *  批量插入数据库
     * @param recordList
     * @return
     */

    @InsertProvider(type = SpecialBatchProvider.class, method = "dynamicSQL")
    int batchInsertList(List<T> recordList);
}
