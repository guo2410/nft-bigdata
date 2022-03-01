package com.tj.bigdata.analysis.job.eth;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * @author 以太坊仪表盘接口
 */
public interface EthMonitorStrategy {


    /**
     * 处理合约数据
     *
     * @param contract 地址
     * @param type     类型
     */
    void processData(String contract, Integer type);

    /**
     * 同步合约
     */
    void syncDappHour();

    void syncDapp();

    /**
     * 同步合约
     */
    void syncDappDay();



    /**
     * 获取合约的时间节点余额
     * @throws Exception 异常
     */
    void syncBalance() throws Exception;

}
