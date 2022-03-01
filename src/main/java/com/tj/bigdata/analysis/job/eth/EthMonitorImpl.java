package com.tj.bigdata.analysis.job.eth;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tj.bigdata.analysis.config.Web3jConfig;
import com.tj.bigdata.analysis.constant.Constant;
import com.tj.bigdata.analysis.constant.EsConstant;
import com.tj.bigdata.analysis.constant.RedisConstant;
import com.tj.bigdata.analysis.constant.Web3jConstants;
import com.tj.bigdata.analysis.enmus.RedisTime;
import com.tj.bigdata.analysis.entity.OnlineDapps;
import com.tj.bigdata.analysis.entity.RedisCache;
import com.tj.bigdata.analysis.entity.Transactions;
import com.tj.bigdata.analysis.mapper.ContractBalanceMapper;
import com.tj.bigdata.analysis.mapper.DappMapper;
import com.tj.bigdata.analysis.pojo.ContractBalancePo;
import com.tj.bigdata.analysis.pojo.DappPo;
import com.tj.bigdata.analysis.util.DateUtil;
import com.tj.bigdata.analysis.util.HttpClientUtil;
import com.tj.bigdata.analysis.util.Web3Util;
import com.tj.bigdata.analysis.wrapper.TransactionDecorator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Slf4j
@Service
public class EthMonitorImpl implements EthMonitorStrategy {

    @Autowired
    private ThreadPoolExecutor executorService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private DappMapper dappMapper;

    @Autowired
    private ContractBalanceMapper contractBalanceMapper;

    @Autowired
    private Web3jConfig web3jConfig;

    @Autowired
    TransactionDecorator transactionDecorator;

    public void execute(Runnable r) {
        executorService.execute(r);
    }

    private Web3j web3j = null;

    @PostConstruct
    public void init() {
        web3j = web3jConfig.getWeb3j();
    }

    @Override
    public void processData(String contract, Integer type) {
        //直接按照时间戳进行检索 新增的区块添加时间戳的字段 暂时算了
//        BigInteger timeStamp = transactionDecorator.maxBlockNumber();
        List<Transactions> txsByContract = transactionDecorator.getTxsByContract(contract, EsConstant.TRANSACTION_INFO_11000000_500000, type);
        if (txsByContract == null) {
            return;
        }
        List<Transactions> collect = txsByContract.stream().filter(tx -> tx.getTimestamp() != null).collect(Collectors.toList());
        log.info("===== 交易信息获取成功,{},开始解析 =====", collect.size());
        Map<Long, List<Transactions>> groupList = collect.stream().collect(Collectors.groupingBy(Transactions::getTimestamp));
        for (Map.Entry<Long, List<Transactions>> str : groupList.entrySet()) {
            Long key = str.getKey();
            List<Transactions> value = str.getValue();
            int txTimes = value.size();
            double txValue = value.stream().mapToDouble(Transactions::getValue).sum();
            Set<String> fromList = value.stream().map(Transactions::getFrom).collect(Collectors.toSet());
            log.info("===== 时间节点内交易信息解析成功,{},{},{} =====", txTimes, txValue, fromList.size());
            RedisCache redisCache = new RedisCache();
            redisCache.setTxTimes(txTimes).setFromList(fromList).setValue(new BigDecimal(txValue));
            String s = JSONObject.toJSONString(redisCache);
            String redisKey = contract;
            if (type.equals(RedisTime.HOUR.getCode())) {
                redisKey = Constant.HOUR + Constant.UNDERLINE + contract;
                redisTemplate.opsForZSet().add(redisKey, s, key);
                Long count = Optional.ofNullable(redisTemplate.opsForZSet().count(redisKey, 0, RedisConstant.TIMEMAX)).orElse(0L);
                if (count > RedisConstant.SCORECOUNTDAY) {
                    redisTemplate.opsForZSet().removeRange(redisKey, 0, 0);
                }
            } else {
                redisTemplate.opsForZSet().add(redisKey, s, key);
                Long count = Optional.ofNullable(redisTemplate.opsForZSet().count(redisKey, 0, RedisConstant.TIMEMAX)).orElse(0L);
                if (count > RedisConstant.SCORECOUNTYEAR) {
                    redisTemplate.opsForZSet().removeRange(redisKey, 0, 0);
                }
            }
            log.info("redis缓存信息上传成功,{}", redisCache);
        }
    }

    @Override
    public void syncDappHour() {
        OnlineDapps onlineDapps = listDappFromHttp();
        List<DappPo> result = onlineDapps.getResult();
        log.info("dapp共{}条", result.size());
        result.stream().filter(res -> !"".equals(res.getContract())).forEach(dApp -> {
            String[] contractList = dApp.getContract().split(",");
            for (String s : contractList) {
                processData(s, RedisTime.HOUR.getCode());
                if (dappMapper.isExist(s) == 0) {
                    log.info("新加入的合约 {}", s);
                    dappMapper.insertSelective(new DappPo(s));
                }
            }
        });
    }

    @Override
    public void syncDapp() {
        OnlineDapps onlineDapps = listDappFromHttp();
        List<DappPo> result = onlineDapps.getResult();
        log.info("dapp共{}条", result.size());
        result.stream().filter(res -> !"".equals(res.getContract())).forEach(dApp -> {
            String[] contractList = dApp.getContract().split(",");
            for (String s : contractList) {
                if (s.startsWith(Web3jConstants.ADDRESS_BEGIN)) {
                    if (dappMapper.isExist(s) == 0) {
                        log.info("新加入的合约 {}", s);
                        dappMapper.insertSelective(new DappPo(s));
                    }
                }

            }
        });
    }

    @Override
    public void syncDappDay() {
        OnlineDapps onlineDapps = listDappFromHttp();
        List<DappPo> result = onlineDapps.getResult();
        log.info("dapp共{}条", result.size());
        result.stream().filter(res -> !"".equals(res.getContract())).forEach(dApp -> {
            String[] contractList = dApp.getContract().split(",");
            for (String s : contractList) {
                processData(s, RedisTime.DAY.getCode());
                if (dappMapper.isExist(s) == 0) {
                    log.info("新加入的合约 {}", s);
                    dappMapper.insertSelective(new DappPo(s));
                }
            }
        });
    }

        @Override
    public void syncBalance() throws Exception {
        //获取最新区块 如果区块在数据库已经存在
        final BigInteger blockNumber = web3j.ethBlockNumber().send().getBlockNumber();
        BigInteger maxNumber = contractBalanceMapper.maxNumberFromDb();
        if (maxNumber != null && maxNumber.equals(blockNumber)) {
            return;
        }
        OnlineDapps onlineDapps = listDappFromHttp();
        List<DappPo> result = onlineDapps.getResult();
        if (result == null || result.size() == 0) {
            return;
        }
        log.info("dapp共{}条", result.size());
        result.stream().filter(res -> !"".equals(res.getContract())).forEach(dApp -> {
            List<String> contractList = Arrays.asList(dApp.getContract().split(","));
            contractList.stream().filter(ct -> ct.startsWith("0x")).forEach(s -> execute(() -> {
                log.info("合约的地址是 {}", s);
                BigDecimal balanceEther;
                try {
                    balanceEther = Web3Util.getBalanceEther(web3j, s);
                    ContractBalancePo contractBalancePo = new ContractBalancePo();
                    contractBalancePo.setBalance(balanceEther).setContract(s).setRecordTime(DateUtil.getNowTimeStampHour()).setBlockNumber(blockNumber);
                    contractBalanceMapper.insertSelective(contractBalancePo);
                    while (contractBalanceMapper.contractNum(s) > Constant.DAYHOURSIZEINT) {
                        log.info("该合约记录大于24 {}", s);
                        ContractBalancePo balancePo = contractBalanceMapper.oneOldestPoint(s);
                        contractBalanceMapper.delete(balancePo);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }));
        });
    }

    /**
     * 发送http请求获取dapp列表
     *
     * @return
     */
    private OnlineDapps listDappFromHttp() {
        HashMap<String, String> map = new HashMap<>();
        map.put("responseType", "43");
        map.put("chain_type", "1");
        String s = HttpClientUtil.sendPost("http://api.dapponline.io/dappapi/wit", map);
        return JSON.parseObject(s, OnlineDapps.class);
    }
}
