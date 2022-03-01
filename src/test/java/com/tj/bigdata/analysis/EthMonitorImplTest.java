package com.tj.bigdata.analysis;

import com.tj.bigdata.analysis.job.eth.EthMonitorStrategy;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;

@RunWith(SpringRunner.class)
class EthMonitorImplTest {

    @Autowired
    EthMonitorStrategy ethMonitorStrategy;

    @Test
    public void test() {
        ethMonitorStrategy.processData("0xcca95e580bbbd04851ebfb85f77fd46c9b91f11c",1);
    }

    @Test
    public void test1() {
        HashSet<String> strings = new HashSet<>();

        strings.add("111");
        strings.add("222");
        strings.add("121");
        strings.add("121");
        strings.add("444");
        strings.add("444");
        strings.add("666");

        strings.forEach(System.out::println);
    }
}
