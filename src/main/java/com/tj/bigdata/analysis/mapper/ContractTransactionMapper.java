package com.tj.bigdata.analysis.mapper;

import com.tj.bigdata.analysis.pojo.ContractTransaction;
import org.apache.ibatis.annotations.Mapper;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * @author guoch
 */
@Mapper
public interface ContractTransactionMapper extends BaseMapper<ContractTransaction>, MySqlMapper<ContractTransaction>, ExampleMapper<ContractTransaction> {

//    /**
//     * 批量插入合约交易
//     *
//     * @param contractTransactionList 交易集合
//     * @param tableName               表名
//     */
//    void insertContract(List<ContractTransaction> contractTransactionList, String tableName);

}
