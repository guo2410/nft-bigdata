package com.tj.bigdata.analysis.service.impl;

import com.alibaba.fastjson.JSON;
import com.tj.bigdata.analysis.config.ElasticSearchConfig;
import com.tj.bigdata.analysis.constant.EsConstant;
import com.tj.bigdata.analysis.enmus.RedisTime;
import com.tj.bigdata.analysis.entity.BlockInfo;
import com.tj.bigdata.analysis.entity.Transactions;
import com.tj.bigdata.analysis.service.EsService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author  guoch
 */
@Service
@Slf4j
public class EsServiceImpl implements EsService {


    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Qualifier("esRestClient")
    @Autowired
    RestHighLevelClient restHighLevelClient;

//    @Autowired
//    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public List<Transactions> getTxsByContract(String contract, String index, Integer type) {
        Integer resType = Optional.ofNullable(type).orElse(1);
        List<Transactions> transactionsList = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.must(QueryBuilders.matchQuery("to", contract));
        if (resType.equals(RedisTime.HOUR.getCode())) {
            String startBlock = stringRedisTemplate.opsForValue().get("current_hour");
            if (startBlock == null) {
                startBlock = "11993001";
            }
//            BigInteger bigInteger = this.maxBlockNumberNew();
            String bigInteger = "12396409";
//            stringRedisTemplate.opsForValue().set("current_day", String.valueOf(bigInteger));
            log.info("============= ES查询24小时数据 ==============");
            RangeQueryBuilder blockNumber = QueryBuilders.rangeQuery("blockNumber");
            blockNumber.gte(startBlock);
            blockNumber.lte(bigInteger);
            boolQueryBuilder.filter(blockNumber);
        } else {
            String startBlock = stringRedisTemplate.opsForValue().get("current_day");
            if (startBlock == null) {
                startBlock = "11812005";
            }
//            BigInteger bigInteger = this.maxBlockNumberNew();
            String bigInteger = "12396409";
//            stringRedisTemplate.opsForValue().set("current_hour", String.valueOf(bigInteger));
            log.info("============= ES查询一天数据 ==============");
            RangeQueryBuilder blockNumber = QueryBuilders.rangeQuery("blockNumber");
            blockNumber.gte(startBlock);
            blockNumber.lte(bigInteger);
            boolQueryBuilder.filter(blockNumber);
        }
        String[] include = {"from", "to", "value", "blockNumber", "transactionIndex", "nonce", "value", "type", "hash"};
        String[] exclude = {"logs", "input", "logsBloom", "cumulativeGasUsed", "blockHash", "gasPrice", "gas", "r"};
        searchSourceBuilder.fetchSource(include, exclude);
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.sort("blockNumber", SortOrder.DESC);
        searchSourceBuilder.size(50000);

        searchSourceBuilder.trackTotalHits(true);
        SearchRequest searchRequest = new SearchRequest(new String[]{index}, searchSourceBuilder);
        searchRequest.scroll(TimeValue.timeValueMinutes(10));
        log.info(">>>>>>>>>>>构建的查询指定合约交易 DSL语句----{}<<<<<<<<<<<<<<<<", searchSourceBuilder.toString());
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
            log.info("response查询成功");
            SearchHits hits = response.getHits();
            String scrollId = null;
            if (hits != null && hits.getHits().length > 0) {
                for (SearchHit hit : hits) {
                    Transactions transactions = JSON.parseObject(hit.getSourceAsString(), Transactions.class);
                    transactionsList.add(transactions);
                }
                scrollId = response.getScrollId();
            }
            while (true) {
                if (scrollId == null) {
                    break;
                }
                SearchScrollRequest searchScrollRequest = new SearchScrollRequest(scrollId).scroll(TimeValue.timeValueMinutes(5));
                response = restHighLevelClient.scroll(searchScrollRequest, ElasticSearchConfig.COMMON_OPTIONS);
                if (response != null && response.getHits().getHits().length > 0) {
                    for (SearchHit hit : response.getHits().getHits()) {
                        Transactions transactions = JSON.parseObject(hit.getSourceAsString(), Transactions.class);
                        transactionsList.add(transactions);
                    }
                } else {
                    ClearScrollRequest clearScrollRequest = new ClearScrollRequest();
                    clearScrollRequest.addScrollId(scrollId);
                    ClearScrollResponse clearScrollResponse = restHighLevelClient.clearScroll(clearScrollRequest, ElasticSearchConfig.COMMON_OPTIONS);
                    log.info("======score删除 {}====", clearScrollResponse.isSucceeded());
                    break;
                }
            }
            log.info("es获取交易信息成功，开始返回");
            return transactionsList;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BigInteger getTimeStampFromEs(BigInteger blockNumber) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.termQuery("_id", blockNumber));
        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.BLOCK_INFO}, searchSourceBuilder);
        log.info(">>>>>>>>>>>构建的查询区块时间戳 DSL语句----{}<<<<<<<<<<<<<<<<", searchSourceBuilder.toString());
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
            SearchHits hits = response.getHits();
            if (hits != null && hits.getHits().length > 0) {
                for (SearchHit hit : hits) {
                    BlockInfo transactions = JSON.parseObject(hit.getSourceAsString(), BlockInfo.class);
                    return transactions.getTimestamp();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Boolean batchContract(List<Transactions> transactionsList) throws IOException {

        BulkRequest bulkRequest = new BulkRequest();
        String contract = transactionsList.get(0).getTo();
        for (Transactions transactions : transactionsList) {
            IndexRequest indexRequest = new IndexRequest(contract);
            indexRequest.id(transactions.getHash());
            String s = JSON.toJSONString(transactions);
            indexRequest.source(s, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);
        return !bulk.hasFailures();
    }

    public BigInteger maxBlockNumberNew() {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.sort("number", SortOrder.DESC);
        searchSourceBuilder.size(1);
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchSourceBuilder.trackTotalHits(true);
        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.BLOCK_INFO_11000000_500000}, searchSourceBuilder);
        log.info(">>>>>>>>>>>构建的查询最大同步区块number DSL语句----{}<<<<<<<<<<<<<<<<", searchSourceBuilder.toString());
        try {
            SearchResponse response = restHighLevelClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
            SearchHits hits = response.getHits();
            if (hits != null && hits.getHits().length > 0) {
                return BigInteger.valueOf(hits.getTotalHits().value);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        log.info("==========当前同步最大区块信息获取失败 ======");
        return null;
    }

    @Override
    public void createContractIndex(String contract) throws IOException {
//        CreateIndexRequest request = new CreateIndexRequest(contract);

//        Map<String, Object> message = new HashMap<>();
//        message.put("type", "text");
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("message", message);
//        Map<String, Object> mapping = new HashMap<>();
//        mapping.put("properties", properties);
//        request.mapping(String.valueOf(mapping));

//        request.source("{\n" +
//                "    \"settings\" : {\n" +
//                "        \"number_of_shards\" : 1,\n" +
//                "        \"number_of_replicas\" : 0\n" +
//                "    },\n" +
//                "    \"mappings\" : {\n" +
//                "        \"properties\" : {\n" +
//                "            \"message\" : { \"type\" : \"text\" }\n" +
//                "        }\n" +
//                "    },\n" +
//                "    \"aliases\" : {\n" +
//                "        \"twitter_alias\" : {}\n" +
//                "    }\n" +
//                "}", XContentType.JSON);

//        request.source("{\n" +
//                "  \"settings\": {\n" +
//                "    \"number_of_shards\": 3,\n" +
//                "    \"number_of_replicas\": 1\n" +
//                "  }, \n" +
//                "  \"mappings\": {\n" +
//                "    \"properties\": {\n" +
//                "      \"blockHash\": { \"type\": \"keyword\"\n" +
//                "      }\n" +
//                "    }\n" +
//                "  }\n" +
//                "}", XContentType.JSON);
//        restHighLevelClient.indices().create(request, ElasticSearchConfig.COMMON_OPTIONS);
    }

    @Override
    public BigInteger maxBlockNumber() {
        return null;
    }


//    @Override
//    public void oneTxFromEs(Integer blockNum) {
//        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        searchSourceBuilder.query(QueryBuilders.matchQuery("blockNumber", blockNum));
//        searchSourceBuilder.size(1000);
//        String[] include = {"blockHash", "from", "to", "value", "blockNumber"};
//        String[] exclude = {"logs", "input", "logsBloom", "cumulativeGasUsed"};
//        searchSourceBuilder.fetchSource(include, exclude);
//        SearchRequest searchRequest = new SearchRequest(new String[]{EsConstant.TRANSACTION_INFO_10500001_500000}, searchSourceBuilder);
//        log.info(">>>>>>>>>>>构建的查询blockNumber的tx DSL语句----{}<<<<<<<<<<<<<<<<", searchSourceBuilder.toString());
//        try {
//            SearchResponse response = restHighLevelClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
//            SearchHits hits = response.getHits();
//            if (hits != null && hits.getHits().length > 0) {
//                for (SearchHit hit : hits) {
//                    AnalysisTxEntity analysisTxEntity = JSON.parseObject(hit.getSourceAsString(), AnalysisTxEntity.class);
//                    analysisTxEntity.setTimestamp(getTimeFromCache(new BigInteger(String.valueOf(analysisTxEntity.getBlockNumber()))));
//                    kafkaTemplate.send(KafkaConstant.FLINK_ES_TX, JSON.toJSONString(analysisTxEntity));
//                    log.info("发送Kafka成功,{}", analysisTxEntity.getTimestamp());
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}
