package com.tj.bigdata.analysis.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigInteger;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class AnalysisTxEntity {
    /**
     * 区块号
     */
    private BigInteger blockNumber;

    /**
     * 发起交易的外部地址
     * 由椭圆曲线数字签名算法的v,r,s这三个组件计算得出
     */
    private String from;

    /**
     * 目标以太坊地址  这个地址既可以是外部账户，也可以是合约的地址。
     */
    private String to;

    private BigInteger timestamp;

    /**
     * 发送给目标地址的以太币的数量。
     */
    private BigInteger value;
}
