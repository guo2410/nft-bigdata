package com.tj.bigdata.analysis;

import com.tj.bigdata.analysis.constant.EsConstant;
import com.tj.bigdata.analysis.service.EsService;
import io.vavr.Tuple2;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigInteger;

@SpringBootTest
class EsServiceTest {

    @Autowired
    EsService esService;

    @Test
    public void test11() {
    }

    @Test
    public void test() throws IOException {
        esService.createContractIndex("0xcca95e580bbbd04851ebfb85f77fd46c9b91f11c");
    }

    @Test
    public void stampNodetest() throws Exception {
        Class<?> cls = Class.forName("com.tj.bigdata.analysis.service.impl.EsServiceImpl");
        Object o = cls.newInstance();

        Method stampNode = cls.getDeclaredMethod("stampNode", String.class);
        stampNode.setAccessible(true);
        Tuple2<BigInteger, BigInteger> res = (Tuple2<BigInteger, BigInteger>) stampNode.invoke(o, EsConstant.TRANSACTION_INFO_3000000_500000);
        System.out.println(res._1());
        System.out.println(res._2());
    }
}
