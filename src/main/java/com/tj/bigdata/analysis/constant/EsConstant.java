package com.tj.bigdata.analysis.constant;


import io.vavr.Tuple2;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author guoch
 */
public class EsConstant {
    public static final String BLOCK_INFO = "block_info";

    public static final String BLOCK_INFO_11000000_500000 = "block_info_11000000_500000";

    private EsConstant() {
    }

    public volatile static EsConstant instance;

    public static Tuple2<EsConstant,List<String>> getInstance() {
        if (instance == null) {
            synchronized (EsConstant.class) {
                if (instance == null) {
                    instance = new EsConstant();
                }
            }
        }
        return new Tuple2<>(instance,instance.TRANSACTIONINDEX);
    }


    private  List<String> TRANSACTIONINDEX = Stream.of(TRANSACTION_INFO_10000001_500000, TRANSACTION_INFO_10500001_500000, TRANSACTION_INFO_9500000_500000,TRANSACTION_INFO_11000000_500000,
            TRANSACTION_INFO_9000000_500000, TRANSACTION_INFO_8500001_500000, TRANSACTION_INFO_8000001_500000, TRANSACTION_INFO_7500001_500000, TRANSACTION_INFO_7000001_500000,
            TRANSACTION_INFO_6500001_500000, TRANSACTION_INFO_6000001_500000, TRANSACTION_INFO_5500000_500000, TRANSACTION_INFO_5000001_500000,
            TRANSACTION_INFO_4500000_500000, TRANSACTION_INFO_4000001_500000, TRANSACTION_INFO_3500000_500000, TRANSACTION_INFO_3000000_500000, TRANSACTION_INFO_0_4000000).collect(Collectors.toList());

    public static final String TRANSACTION_INFO_10000001_500000 = "transaction_info_10000001_500000";
    public static final String TRANSACTION_INFO_10500001_500000 = "transaction_info_10500001_500000";
    public static final String TRANSACTION_INFO_11000000_500000 = "transaction_info_11000000_500000";
    public static final String TRANSACTION_INFO_9500000_500000 = "transaction_info_9500000_500000";
    public static final String TRANSACTION_INFO_9000000_500000 = "transaction_info_9000000_500000";
    public static final String TRANSACTION_INFO_8500001_500000 = "transaction_info_8500001_500000";
    public static final String TRANSACTION_INFO_8000001_500000 = "transaction_info_8000001_500000";
    public static final String TRANSACTION_INFO_7500001_500000 = "transaction_info_7500001_500000";
    public static final String TRANSACTION_INFO_7000001_500000 = "transaction_info_7000001_500000";
    public static final String TRANSACTION_INFO_6500001_500000 = "transaction_info_6500001_500000";
    public static final String TRANSACTION_INFO_6000001_500000 = "transaction_info_6000001_500000";
    public static final String TRANSACTION_INFO_5500000_500000 = "transaction_info_5500000_500000";
    public static final String TRANSACTION_INFO_5000001_500000 = "transaction_info_5000001_500000";
    public static final String TRANSACTION_INFO_4500000_500000 = "transaction_info_4500000_500000";
    public static final String TRANSACTION_INFO_4000001_500000 = "transaction_info_4000001_500000";
    public static final String TRANSACTION_INFO_3500000_500000 = "transaction_info_3500000_500000";
    public static final String TRANSACTION_INFO_3000000_500000 = "transaction_info_3000000_500000";
    public static final String TRANSACTION_INFO_0_4000000 = "transaction_info_0_4000000";


    public static final Integer STEP_SIZE_MARK_50W = 500000;

    public static final Integer STEP_SIZE_MARK_300W = 3000000;

    public static final Integer CORRECT = 1;
}
