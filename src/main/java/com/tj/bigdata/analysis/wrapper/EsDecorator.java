package com.tj.bigdata.analysis.wrapper;

import com.tj.bigdata.analysis.entity.Transactions;
import com.tj.bigdata.analysis.service.EsService;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;

/**
 * @author guoch
 */
public abstract class  EsDecorator implements EsService {

    /**
     * 装饰器方法
     */
    abstract List<Transactions> invoke(List<Transactions> transactionsList, String index, Integer type);


    @Override
    public BigInteger getTimeStampFromEs(BigInteger blockNumber) {
        return null;
    }

    @Override
    public Boolean batchContract(List<Transactions> TransactionsList) throws IOException {
        return null;
    }

    @Override
    public void createContractIndex(String contract) throws IOException {

    }

}