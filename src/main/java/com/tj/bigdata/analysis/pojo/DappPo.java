package com.tj.bigdata.analysis.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author guoch
 */
@Accessors(chain = true)
@Table(name = "dapp")
@AllArgsConstructor
@Data
public class DappPo implements Serializable {

    @Column(name = "contract")
    private String contract;

}