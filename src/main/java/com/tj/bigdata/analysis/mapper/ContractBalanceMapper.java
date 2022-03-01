package com.tj.bigdata.analysis.mapper;

import com.tj.bigdata.analysis.pojo.ContractBalancePo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.BaseMapper;
import tk.mybatis.mapper.common.ExampleMapper;
import tk.mybatis.mapper.common.MySqlMapper;

import java.math.BigInteger;

/**
 * @author guoch
 */
@Repository
public interface ContractBalanceMapper extends BaseMapper<ContractBalancePo>, MySqlMapper<ContractBalancePo>, ExampleMapper<ContractBalancePo> {

    /**
     *  获取合约最老的余额记录
     *
     * @param contract 合约
     * @return 条数
     */
    ContractBalancePo oneOldestPoint(@Param("contract") String contract);

    /**
     *  获取合约最新的余额记录
     *
     * @param contract 合约
     * @return 条数
     */
    ContractBalancePo oneLatestPoint(@Param("contract") String contract);

    /**
     * 查询一条合约几条记录
     * @param contract 合约
     * @return 条数
     */
    int contractNum(@Param("contract") String contract);

    /**
     *  获取数据库最大区块
     * @return 区块
     */
    BigInteger maxNumberFromDb();

}
