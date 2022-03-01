package com.tj.bigdata.analysis.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;

/**
 * @author guoch
 */
@Data
@Accessors(chain = true)
@Table(name = "contract_transaction")
@NoArgsConstructor
public class ContractTransaction implements Serializable {

    /**
     * 区块号
     */
    @Column(name = "block_number")
    private BigInteger blockNumber;

    /**
     * 发起交易的外部地址
     * 由椭圆曲线数字签名算法的v,r,s这三个组件计算得出
     */
    @Column(name = "from")
    private String from;

    /**
     * 目标以太坊地址  这个地址既可以是外部账户，也可以是合约的地址。
     */
    @Column(name = "to")
    private String to;

    @Column(name = "hash")
    private String hash;

    @Column(name = "transaction_index")
    private BigInteger transactionIndex;

    /**
     * data 只包含data的交易是针对合约的调用  如果目标地址（to）是一个合约，那么EVM会执行这个合约，并尝 试调用在交易的data字段中指定的函数
     * 当交易的data字段含有内容时，多数情况下这个交易的目标是一个合约
     */
    @Column(name = "input")
    private String input;

    @Column(name = "time_stamp")
    private BigInteger timeStamp;

    /**
     * 一个序列编号，由构建这个交易的外部账号提供，用于防止交易的重放攻击
     */
    @Column(name = "nonce")
    private BigInteger nonce;

    /**
     * 发送给目标地址的以太币的数量。
     */
    @Column(name = "value")
    private BigInteger value;

    /**
     * 成功与否，1表示成功，0表示失败
     */
    @Column(name = "status")
    private String status;
}
