package com.tj.bigdata.analysis;

import com.tj.bigdata.analysis.constant.EsConstant;
import com.tj.bigdata.analysis.mapper.ContractTransactionMapper;
import com.tj.bigdata.analysis.pojo.ContractTransaction;
import com.tj.bigdata.analysis.service.EsService;
import io.vavr.Tuple2;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.math.BigInteger;
import java.util.List;


class AnalysisFactoryApplicationTests {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    ContractTransactionMapper contractTransactionMapper;

    @Autowired
    EsService esService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    void contextLoads() {
        System.out.println(restHighLevelClient);
    }

    @Test
    public void test() {

        ContractTransaction contractTransaction = new ContractTransaction();
        contractTransaction.setBlockNumber(new BigInteger("11")).setFrom("0xasdasdasd").setHash("asdasdasfasd").setInput("asjkhasfuyasjkhdajklsdyhioauyhjkfgajksgfjlaghdahsdoiuhhajks")
                .setNonce(new BigInteger("123123")).setTo("12casdasdfsd").setValue(new BigInteger("123123")).setTransactionIndex(new BigInteger("213"))
                .setStatus("asd").setTimeStamp(new BigInteger("1231245124"));

        contractTransactionMapper.insertSelective(contractTransaction);
    }

    @Test
    public void test2() {
//            esService.getTxsByContract("0xcca95e580bbbd04851ebfb85f77fd46c9b91f11c");
    }

    @Test
    public void test3() {
//        redisTemplate.opsForValue().set("guoch","hello");
//        String guoch = (String) redisTemplate.opsForValue().get("guoch");
//        System.out.println(guoch);
    }

    @Test
    public void test4() {
        BigInteger timeStamp = esService.getTimeStampFromEs(new BigInteger("4600000"));
        System.out.println(timeStamp);
    }

    @Test
    public void test5() {
        Tuple2<EsConstant, List<String>> instance = EsConstant.getInstance();
        System.out.println(instance._1());
        List<String> strings = instance._2();
        for (String string : strings) {
            System.out.println(string);
        }
    }

    @Test
    public void test6() {
        redisTemplate.opsForHash().put("txtime", "11111", "32233");
        Object txtime111 = redisTemplate.opsForHash().get("txtime", "11111");
        System.out.println(txtime111);

    }

}
