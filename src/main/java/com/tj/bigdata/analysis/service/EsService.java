package com.tj.bigdata.analysis.service;

import com.tj.bigdata.analysis.entity.AnalysisTxEntity;
import com.tj.bigdata.analysis.entity.KafkaAnalysisEntity;
import com.tj.bigdata.analysis.entity.Transactions;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

/**
 * 查询es数据库
 *
 * @author guoch
 */
public interface EsService {

    /**
     * 获取合约交易
     *
     * @param contract 合约
     * @param index    索引
     * @param type     事件类型 1 小时 2 天
     * @return 交易集合
     */
    List<Transactions> getTxsByContract(String contract, String index, Integer type);

    /**
     * 从es获取时间戳
     *
     * @param blockNumber
     * @return
     */
    BigInteger getTimeStampFromEs(BigInteger blockNumber);

    Boolean batchContract(List<Transactions> TransactionsList) throws IOException;


    void createContractIndex(String contract) throws IOException;

    BigInteger maxBlockNumber();

//    /**
//     * 获取一个区块的交易
//     *
//     * @param blockNum
//     */
//    void oneTxFromEs(Integer blockNum);

}