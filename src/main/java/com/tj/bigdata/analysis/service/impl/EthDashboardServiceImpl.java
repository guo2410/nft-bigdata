package com.tj.bigdata.analysis.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.tj.bigdata.analysis.constant.Constant;
import com.tj.bigdata.analysis.constant.RedisConstant;
import com.tj.bigdata.analysis.enmus.TimeRange;
import com.tj.bigdata.analysis.entity.*;
import com.tj.bigdata.analysis.factory.DataSource;
import com.tj.bigdata.analysis.factory.MapDataSourceFactory;
import com.tj.bigdata.analysis.mapper.ContractBalanceMapper;
import com.tj.bigdata.analysis.pojo.ContractBalancePo;
import com.tj.bigdata.analysis.service.EthDashboardService;
import com.tj.bigdata.analysis.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author guoch
 */
@Slf4j
@Service
public class EthDashboardServiceImpl implements EthDashboardService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ThreadPoolExecutor executorService;

    @Autowired
    private ContractBalanceMapper contractBalanceMapper;

    @Autowired
    private MapDataSourceFactory mapDataSourceFactory;

    @Autowired
    private EthDashboardService ethDashboardService;

    public void execute(Runnable r) {
        executorService.execute(r);
    }

    @Override
    public EthDashboardDto listLineChartRangeSync(EthDashboardValueReq ethDashboardValueReq) {
        String fromCache = null;
        if (!ethDashboardValueReq.getTimeRange().equals(TimeRange.DAY.getCode())) {
            fromCache = stringRedisTemplate.opsForValue().get(RedisConstant.DAPP + Constant.UNDERLINE + ethDashboardValueReq.getTimeRange() + Constant.UNDERLINE + ethDashboardValueReq.getTimeRange() + Constant.UNDERLINE + ethDashboardValueReq.getAppId());
        }
        if (fromCache != null) {
            log.info("从缓存获取成功，{}", fromCache);
            return Objects.requireNonNull(JSON.parseObject(fromCache, EthDashboardDto.class));
        }
        CountDownLatch countDownLatch = new CountDownLatch(ethDashboardValueReq.getToAddressArray().size());
        EthDashboardDto valueRes = new EthDashboardDto();
        Map<BigDecimal, LineChartResultValueList> pointsList = new ConcurrentHashMap<>();
        Integer timeRange = TimeRange.get(ethDashboardValueReq.getTimeRange()).getInterval();
        int finalTimeRange = timeRange;
        if (timeRange.equals(TimeRange.DAY.getInterval())) {
            finalTimeRange = timeRange << 1;
        }

        int finalTimeRange1 = finalTimeRange;
        ethDashboardValueReq.getToAddressArray().forEach(contract -> execute(() -> {
                    try {
                        String redisKey = timeRange.equals(TimeRange.DAY.getInterval()) ? Constant.HOUR + Constant.UNDERLINE + contract : contract;
                        //从redis获取合约计算结果信息
                        Set<ZSetOperations.TypedTuple<String>> dayValue = stringRedisTemplate.opsForZSet().reverseRangeWithScores(redisKey, 0, finalTimeRange1 - 1);
                        //如果合约没有值退出循环
                        if (dayValue == null || dayValue.size() == 0) {
                            return;
                        }
                        log.info("从redis获取合约，{}，数据成功，{}，开始封装", contract, dayValue.size());
                        Map<BigDecimal, LineChartResultValue> points = new HashMap<>(finalTimeRange1);
                        dayValue.forEach(stringTypedTuple -> {
                            BigDecimal score = new BigDecimal(stringTypedTuple.getScore());
                            log.info("时间戳key为，{}", score);
                            RedisCache redisCache = Objects.requireNonNull(JSON.parseObject(stringTypedTuple.getValue(), RedisCache.class));
                            LineChartResultValue lineChartResultValue = new LineChartResultValue();
                            lineChartResultValue.setPlayersSet(redisCache.getFromList()).setTxTimes(redisCache.getTxTimes()).setTxValue(redisCache.getValue());
                            points.put(score, lineChartResultValue);
                        });
                        //points 单个合约的点线图
                        Set<Map.Entry<BigDecimal, LineChartResultValue>> entries = points.entrySet();
                        for (Map.Entry<BigDecimal, LineChartResultValue> entry : entries) {
                            BigDecimal key = entry.getKey();
                            LineChartResultValue lineChartResultValueTemporary = entry.getValue();
                            LineChartResultValueList lineChartResultValueList = pointsList.get(key);
                            //如果容器中存在时间节点
                            if (lineChartResultValueList != null) {
                                if (lineChartResultValueList.getPlayersSet() != null && lineChartResultValueList.getPlayersSet().size() > 0) {
                                    lineChartResultValueList.getPlayersSet().addAll(lineChartResultValueTemporary.getPlayersSet());
                                }
                                lineChartResultValueList.getTxTime().add(lineChartResultValueTemporary.getTxTimes());
                                lineChartResultValueList.getTxValue().add(lineChartResultValueTemporary.getTxValue());
                            } else {
                                //如果不存在，创建一个再放进去
                                LineChartResultValueList lineChartResultValueList1 = new LineChartResultValueList();
                                lineChartResultValueList1.getPlayersSet().addAll(lineChartResultValueTemporary.getPlayersSet());
                                lineChartResultValueList1.getTxTime().add(lineChartResultValueTemporary.getTxTimes());
                                lineChartResultValueList1.getTxValue().add(lineChartResultValueTemporary.getTxValue());
                                pointsList.put(key, lineChartResultValueList1);
                            }
                        }
                    } finally {
                        countDownLatch.countDown();
                    }
                }
        ));
        try {
            countDownLatch.await();
            log.info("===线程池处理完毕===");
        } catch (InterruptedException ignored) {
        }
        //临时容器
        Map<BigDecimal, LineChartResultValue> points = valueRes.getPoints();
        for (Map.Entry<BigDecimal, LineChartResultValueList> bigDecimalLineChartResultValueListEntry : pointsList.entrySet()) {
            BigDecimal key = bigDecimalLineChartResultValueListEntry.getKey();
            LineChartResultValueList value = bigDecimalLineChartResultValueListEntry.getValue();
            LineChartResultValue lineChartResultValue = new LineChartResultValue();
            Optional<BigDecimal> reduceVal = value.getTxValue().stream().reduce(BigDecimal::add);
            Optional<Integer> reduceTimes = value.getTxTime().stream().reduce(Integer::sum);
            BigDecimal totalValue = reduceVal.orElseGet(() -> new BigDecimal(0));
            lineChartResultValue.setTxValue(totalValue.movePointLeft(18).setScale(6, BigDecimal.ROUND_HALF_UP));
            lineChartResultValue.setTxTimes(reduceTimes.orElse(0));
            lineChartResultValue.setPlayers(value.getPlayersSet().size());
            lineChartResultValue.setPlayersSet(value.getPlayersSet());
            points.put(key, lineChartResultValue);
        }
        if (points.size() == 0) {
            return valueRes;
        }
        valueRes.setPoints(keepRangePoints(points, timeRange));
        if (!timeRange.equals(TimeRange.DAY.getInterval())) {
            BigDecimal txValue = new BigDecimal(0);
            int txTimes = 0;
            Set<String> playersSet = new HashSet<>();
            for (Map.Entry<BigDecimal, LineChartResultValue> doubleLineChartResultValueEntry : valueRes.getPoints().entrySet()) {
                LineChartResultValue value = doubleLineChartResultValueEntry.getValue();
                if (value != null) {
                    txValue = value.getTxValue().add(txValue);
                    txTimes += value.getTxTimes();
                    playersSet.addAll(value.getPlayersSet());
                    value.getPlayersSet().clear();
                }
            }
            valueRes.setPlayers(playersSet.size()).setTxTimes(txTimes).setTxValue(txValue);
        }
        if (!ethDashboardValueReq.getTimeRange().equals(TimeRange.DAY.getCode())) {
            stringRedisTemplate.opsForValue().set(RedisConstant.DAPP + Constant.UNDERLINE + ethDashboardValueReq.getTimeRange() + Constant.UNDERLINE + ethDashboardValueReq.getTimeRange() + Constant.UNDERLINE + ethDashboardValueReq.getAppId(), JSONObject.toJSONString(valueRes), 6, TimeUnit.HOURS);
        }
        return valueRes;
    }

    @Override
    public EthDashboardHourDto hourFromFactory(EthDashboardValueHourReq ethDashboardValueReq) {
        EthDashboardHourDto ethDashboardHourDto = new EthDashboardHourDto();
        DataSource dataSource = mapDataSourceFactory.getDataSource(ethDashboardValueReq.getType());
        EthDashboardDto ethDashboardDto = dataSource.structPoints(ethDashboardValueReq);
        ethDashboardHourDto.setPoints(ethDashboardDto.getPoints());
        ethDashboardHourDto.setPlayers(ethDashboardDto.getPlayers());
        ethDashboardHourDto.setTxTimes(ethDashboardDto.getTxTimes());
        ethDashboardHourDto.setTxValue(ethDashboardDto.getTxValue());
        int size = ethDashboardHourDto.getPoints().size();
        if (size == 0) {
            EthNullPointsReq ethNullPointsReq = new EthNullPointsReq();
            ethNullPointsReq.setRange(ethDashboardValueReq.getTimeRange());
            EthNullPointsRes ethNullPointsRes = ethDashboardService.listNullPointsModel(ethNullPointsReq);
            ethDashboardHourDto.setPoints(ethNullPointsRes.getPoints());
        }
        return ethDashboardHourDto;
    }

    @Override
    public EthDashboardHourBalanceDto listLineChartRangeBalance(EthDashboardValueReq ethDashboardValueReq) {
        String balanceFromCache = stringRedisTemplate.opsForValue().get(RedisConstant.DAPP + Constant.UNDERLINE + RedisConstant.BALANCE + Constant.UNDERLINE + ethDashboardValueReq.getTimeRange() + Constant.UNDERLINE + ethDashboardValueReq.getAppId());
        if (StringUtils.isEmpty(balanceFromCache)) {
            log.info("==== 缓存中没有 查询数据库====");
            return getBalanceFromDbWithLock(ethDashboardValueReq);
        }
        log.info("缓存命中");
        return JSON.parseObject(balanceFromCache, new TypeReference<EthDashboardHourBalanceDto>() {
        });
    }

    private EthDashboardHourBalanceDto getBalanceFromDbWithLock(EthDashboardValueReq ethDashboardValueReq) {
        String uuid = UUID.randomUUID().toString();
        Boolean lock = Optional.ofNullable(stringRedisTemplate.opsForValue().setIfAbsent(RedisConstant.LOCK, uuid, 300, TimeUnit.SECONDS)).orElse(false);
        if (lock) {
            log.info("分布式锁获取成功");
            EthDashboardHourBalanceDto ethDashboardHourBalanceDto;
            try {
                ethDashboardHourBalanceDto = getFromDb(ethDashboardValueReq);
            } finally {
                String script = "if redis.call(\"get\",KEYS[1]) == ARGV[1]\n" +
                        "then\n" +
                        "    return redis.call(\"del\",KEYS[1])\n" +
                        "else\n" +
                        "    return 0\n" +
                        "end";
                stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class), Collections.singletonList(RedisConstant.LOCK), uuid);
            }
            return ethDashboardHourBalanceDto;
        } else {
            log.info("分布式锁获取失败");
            try {
                Thread.sleep(200);
            } catch (Exception ignored) {
            }
            return getBalanceFromDbWithLock(ethDashboardValueReq);
        }
    }

    private EthDashboardHourBalanceDto getFromDb(EthDashboardValueReq ethDashboardValueReq) {
        String balanceFromCache = stringRedisTemplate.opsForValue().get(RedisConstant.DAPP + Constant.UNDERLINE + RedisConstant.BALANCE + Constant.UNDERLINE + ethDashboardValueReq.getTimeRange() + Constant.UNDERLINE + ethDashboardValueReq.getAppId());
        if (!StringUtils.isEmpty(balanceFromCache)) {
            return JSON.parseObject(balanceFromCache, new TypeReference<EthDashboardHourBalanceDto>() {
            });
        }
        log.info("查询数据库中");
        EthDashboardHourBalanceDto ethDashboardHourBalanceDto = new EthDashboardHourBalanceDto();
        Map<BigDecimal, CopyOnWriteArrayList<BigDecimal>> points = new ConcurrentHashMap<>(Constant.DAYHOURSIZEINT);
        CountDownLatch countDownLatch = new CountDownLatch(ethDashboardValueReq.getToAddressArray().size());
        ethDashboardValueReq.getToAddressArray().forEach(ct -> execute(() -> {
            try {
                Example example = new Example(ContractBalancePo.class);
                example.createCriteria().andEqualTo("contract", ct);
                List<ContractBalancePo> contractBalancePos = contractBalanceMapper.selectByExample(example);
                for (ContractBalancePo contractBalancePo : contractBalancePos) {
                    Long recordTimeLong = contractBalancePo.getRecordTime();
                    BigDecimal recordTime = new BigDecimal(recordTimeLong);
                    if (points.containsKey(recordTime)) {
                        points.get(recordTime).add(contractBalancePo.getBalance());
                    } else {
                        CopyOnWriteArrayList<BigDecimal> objects = new CopyOnWriteArrayList<>();
                        objects.add(contractBalancePo.getBalance());
                        points.put(recordTime, objects);
                    }
                }
            } finally {
                countDownLatch.countDown();
            }
        }));
        try {
            countDownLatch.await();
            log.info("===余额汇总线程池处理完毕===");
        } catch (InterruptedException ignored) {
        }
        if (points.size() == 0) {
            return ethDashboardHourBalanceDto;
        }
        HashMap<BigDecimal, BigDecimal> res = new HashMap<>();
        for (BigDecimal aLong : points.keySet()) {
            Optional<BigDecimal> reduce = points.get(aLong).stream().reduce(BigDecimal::add);
            res.put(aLong, reduce.orElseGet(() -> new BigDecimal(0)));
        }
        BigDecimal aLong = res.keySet().stream().max(Comparator.comparing(BigDecimal::abs)).get();
        ethDashboardHourBalanceDto.setPoints(res);
        ethDashboardHourBalanceDto.setBalance(res.get(aLong));
        String s = JSON.toJSONString(ethDashboardHourBalanceDto);
        stringRedisTemplate.opsForValue().set(RedisConstant.DAPP + Constant.UNDERLINE + RedisConstant.BALANCE + Constant.UNDERLINE + ethDashboardValueReq.getTimeRange() + Constant.UNDERLINE + ethDashboardValueReq.getAppId(), s, 30, TimeUnit.MINUTES);
        return ethDashboardHourBalanceDto;
    }

    @Override
    public EthNullPointsRes listNullPointsModel(EthNullPointsReq ethNullPointsReq) {
        EthNullPointsRes ethNullPointsRes = new EthNullPointsRes();
        Map<BigDecimal, LineChartResultValue> points = ethNullPointsRes.getPoints();
        long maxTimeStamp;
        long imeStampRange;
        LineChartResultValue lineChartResultValue = new LineChartResultValue();
        TimeRange timeRange = TimeRange.get(ethNullPointsReq.getRange());
        Integer times = timeRange.getInterval();
        if (times.equals(TimeRange.DAY.getInterval())) {
            maxTimeStamp = DateUtil.getCurrHourTime();
            imeStampRange = Constant.HOURSTAMPTIMELONG;

        } else {
            maxTimeStamp = DateUtil.getCurrDayTime();
            imeStampRange = Constant.DAYSTAMPTIMELONG;
        }
        for (int i = 0; i < times; i++) {
            points.put(new BigDecimal(maxTimeStamp), lineChartResultValue);
            maxTimeStamp = maxTimeStamp - imeStampRange;
        }
        ethNullPointsRes.setPoints(points);
        return ethNullPointsRes;
    }

    private Map<BigDecimal, LineChartResultValue> keepRangePoints(Map<BigDecimal, LineChartResultValue> points, Integer timeRange) {
        //最新时间戳
        BigDecimal maxTimeStamp = points.keySet().stream().max(Comparator.comparing(BigDecimal::abs)).get();
        int timeRangeOld = timeRange;
        LineChartResultValue lineChartResultValue = new LineChartResultValue();
        //24小时 昨天+实时
        if (timeRange.equals(TimeRange.DAY.getInterval())) {
            Long aLong = DateUtil.distanceZeroLength(maxTimeStamp.toBigInteger());
            timeRange = Math.toIntExact(timeRange + aLong);
        }
        Map<BigDecimal, LineChartResultValue> resMap = new HashMap<>(timeRange);
        for (int i = 0; i < timeRange; i++) {
            resMap.put(maxTimeStamp, points.getOrDefault(maxTimeStamp, lineChartResultValue));
            if (timeRangeOld == TimeRange.DAY.getInterval()) {
                maxTimeStamp = maxTimeStamp.subtract(Constant.HOURSTAMPTIME);
            } else {
                maxTimeStamp = maxTimeStamp.subtract(Constant.DAYSTAMPTIME);
            }
        }

        return resMap;
    }
}
