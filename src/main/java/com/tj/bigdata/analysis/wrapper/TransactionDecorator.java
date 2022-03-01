package com.tj.bigdata.analysis.wrapper;

import com.alibaba.fastjson.JSON;
import com.tj.bigdata.analysis.config.ElasticSearchConfig;
import com.tj.bigdata.analysis.config.Web3jConfig;
import com.tj.bigdata.analysis.constant.EsConstant;
import com.tj.bigdata.analysis.constant.Web3jConstants;
import com.tj.bigdata.analysis.enmus.RedisTime;
import com.tj.bigdata.analysis.entity.Transactions;
import com.tj.bigdata.analysis.exception.EsException;
import com.tj.bigdata.analysis.exception.EthException;
import com.tj.bigdata.analysis.service.EsService;
import com.tj.bigdata.analysis.util.DateFormatUtil;
import com.tj.bigdata.analysis.util.DateUtil;
import io.vavr.Tuple2;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * 装饰器
 *
 * @author Administrator
 */
@Slf4j
@Component
public class TransactionDecorator extends EsDecorator {


    private EsService esService;

    @Qualifier("esRestClient")
    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Autowired
    private Web3jConfig web3jConfig;

    @Autowired
    StringRedisTemplate redisTemplate;

    private final Lock lock = new ReentrantLock(true);

    public TransactionDecorator(@Qualifier("esServiceImpl") EsService esService) {
        this.esService = esService;
    }

    @Override
    public List<Transactions> invoke(List<Transactions> transactionsList, String index, Integer type) {
        transactionsList.forEach(tx -> {
//            Tuple2<BigInteger, BigInteger> timeNode = stampNode(index);
            Boolean aBoolean = Optional.of(isContractAddress(tx.getFrom())).orElse(false);
            if (!aBoolean) {
                return;
            }
            BigInteger time = getTimeFromCache(tx.getBlockNumber());
            if (type.equals(RedisTime.HOUR.getCode())) {
                log.info("========= 计算24小时数据 =========");
                tx.setTimestamp(DateUtil.timeStampHour(time));
            } else if (type.equals(RedisTime.DAY.getCode())) {
                log.info("========= 计算一天数据 =========");
                tx.setTimestamp(DateUtil.timeStampDay(time));
            }
        });
        return transactionsList;
    }

    @Override
    public List<Transactions> getTxsByContract(String contract, String index, Integer type) {
        List<Transactions> txsByContract = Optional.ofNullable(esService.getTxsByContract(contract, index, type)).orElse(null);
        if (txsByContract == null || txsByContract.size() == 0) {
            return null;
        }
        return invoke(txsByContract, index, type);
    }

    @Override
    public BigInteger maxBlockNumber() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort("blockNumber", SortOrder.DESC);
        searchSourceBuilder.size(1);
        String[] include = {"blockNumber"};
        String[] exclude = {"logs", "input", "logsBloom", "cumulativeGasUsed", "blockHash", "gasPrice", "gas", "r"};
        searchSourceBuilder.fetchSource(include, exclude);
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.trackTotalHits(true);
        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.BLOCK_INFO_11000000_500000}, searchSourceBuilder);
        log.info(">>>>>>>>>>>构建的查询最大同步区块number DSL语句----{}<<<<<<<<<<<<<<<<", searchSourceBuilder.toString());
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
            SearchHits hits = response.getHits();
            if (hits != null && hits.getHits().length > 0) {
                Transactions transactions = JSON.parseObject(hits.getHits()[0].getSourceAsString(), Transactions.class);
                return getTimeFromCache(transactions.getBlockNumber());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("==========当前同步最大区块信息获取失败 ======");
        return null;
    }

    /**
     * <h1>从es获取时间戳</h1>
     * D
     *
     * @param blockNumber block
     * @return time
     */
    public BigInteger getTimeFromCache(BigInteger blockNumber) {
        Object txtime = redisTemplate.opsForHash().get("txtime", String.valueOf(blockNumber));
        if (txtime == null) {
            log.info("========== 缓存中没有区块时间戳信息，开始从es获取 =========");
            lock.lock();
            try {
                txtime = redisTemplate.opsForHash().get("txtime", String.valueOf(blockNumber));
                if (txtime == null) {
                    BigInteger timeStampFromEs = esService.getTimeStampFromEs(blockNumber);
                    if (Objects.nonNull(timeStampFromEs)) {
                        txtime = timeStampFromEs;
                        redisTemplate.opsForHash().put("txtime", String.valueOf(blockNumber), String.valueOf(txtime));
                    } else {
                        throw new EsException("es中没有" + blockNumber + "区块记录");
                    }
                }
            } finally {
                lock.unlock();
            }
        }
        return new BigInteger(txtime.toString());
    }

    /**
     * <h1>取整值区块号</h1>
     *
     * @param index es
     * @return math
     */
    private int roundNumber(String index) {
        int intIndex = new Integer(index.split("_")[2]);
        if (intIndex == 0) {
            return 0;
        }
        int remainder = intIndex % 500000;
        return remainder == 0 ? intIndex : intIndex - remainder;
    }

    /**
     * <h1>获取索引起止时间戳</h1>
     *
     * @param index es索引名
     * @return 元组
     */
    private Tuple2<BigInteger, BigInteger> stampNode(String index) {
        int roundNumber = this.roundNumber(index);
        if (roundNumber == 0) {
            return new Tuple2<>(this.getTimeFromCache(new BigInteger("0")), this.getTimeFromCache(new BigInteger(String.valueOf(EsConstant.STEP_SIZE_MARK_300W - EsConstant.CORRECT))));
        }
        BigInteger begin = this.getTimeFromCache(new BigInteger(Integer.toString(roundNumber)));
        BigInteger end = this.getTimeFromCache(new BigInteger(String.valueOf((roundNumber - EsConstant.CORRECT + EsConstant.STEP_SIZE_MARK_50W))));
        return new Tuple2<>(begin, end);
    }

    /**
     * 判断地址是否为合约地址 0x为普通地址
     *
     * @param address 地址
     * @return true 普通地址
     */
    private Boolean isContractAddress(String address) {
        boolean flag;
        try {
            Web3j web3j = web3jConfig.getWeb3j();

            String code = web3j.ethGetCode(address, DefaultBlockParameter.valueOf("latest")).send().getCode();
            if (Web3jConstants.ADDRESS_BEGIN.equals(code)) {
                flag = true;
            } else {
                flag = false;
            }
            return flag;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
