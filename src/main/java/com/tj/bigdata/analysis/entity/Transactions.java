package com.tj.bigdata.analysis.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * 合约注册交易中唯一需要的就是在data字段中包含经过编译的合约字节码。这个交易的唯一用处就是把合约注册到以太坊区块链上。向零地址发送包含data值的交易
 *
 * @author guoch
 */
@Data
@Accessors(chain = true)
public class Transactions {

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


    private String hash;

    private BigInteger transactionIndex;

    /**
     * data 只包含data的交易是针对合约的调用  如果目标地址（to）是一个合约，那么EVM会执行这个合约，并尝 试调用在交易的data字段中指定的函数
     * 当交易的data字段含有内容时，多数情况下这个交易的目标是一个合约
     */
    private String input;

    /**
     * 一个序列编号，由构建这个交易的外部账号提供，用于防止交易的重放攻击
     */
    private BigInteger nonce;

    /**
     * 发送给目标地址的以太币的数量。
     */
    private Double value;

    /**
     * 交易的类型，ContractCreation（创建合约）还是MessageCall（调用合约或转账）
     */
    private String type;

    /**
     * 成功与否，1表示成功，0表示失败
     */
    private String status;

    private Long timestamp;
}
