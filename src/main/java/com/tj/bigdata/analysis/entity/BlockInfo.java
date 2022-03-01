package com.tj.bigdata.analysis.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/**
 * @author guoch
 */
@Data
public class BlockInfo implements Serializable {
    private BigInteger timestamp;
}
