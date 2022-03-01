package com.tj.bigdata.analysis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author guoch
 */
@Accessors(chain = true)
@Table(name = "contract_balance")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContractBalancePo implements Serializable {

    @Column(name = "contract")
    private String contract;

    @Column(name = "record_time")
    private Long recordTime;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "block_number")
    private BigInteger blockNumber;
}
