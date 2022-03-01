package com.tj.bigdata.analysis.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tj.bigdata.analysis.constant.Constant;
import com.tj.bigdata.analysis.constant.RedisConstant;
import com.tj.bigdata.analysis.entity.EthDashboardDto;
import com.tj.bigdata.analysis.entity.EthDashboardValueHourReq;
import com.tj.bigdata.analysis.entity.EthDashboardValueReq;
import com.tj.bigdata.analysis.entity.LineChartResultValue;
import com.tj.bigdata.analysis.service.EthDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 昨天 总计
 *
 * @author guoch
 */
@Service
@Slf4j
public class LastDayPoints implements DataSource {

    private EthDashboardService ethDashboardService;

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Autowired
    public void setEthDashboardService(EthDashboardService ethDashboardService) {
        this.ethDashboardService = ethDashboardService;
    }

    @Override
    public EthDashboardDto structPoints(EthDashboardValueHourReq ethDashboardValueHourReq) {
        String redisKey = RedisConstant.DAPP + Constant.UNDERLINE + ethDashboardValueHourReq.getTimeRange() + Constant.UNDERLINE + ethDashboardValueHourReq.getTimeRange() + Constant.UNDERLINE + ethDashboardValueHourReq.getType() + Constant.UNDERLINE + ethDashboardValueHourReq.getAppId();
        String fromCache = stringRedisTemplate.opsForValue().get(redisKey);
        if (fromCache != null) {
            log.info("从缓存获取成功，{}", fromCache);
            return Objects.requireNonNull(JSON.parseObject(fromCache, EthDashboardDto.class));
        }
        EthDashboardValueReq ethDashboardValueReq = new EthDashboardValueReq();
        ethDashboardValueReq.setAppId(ethDashboardValueHourReq.getAppId());
        ethDashboardValueReq.setChain(ethDashboardValueHourReq.getChain());
        ethDashboardValueReq.setTimeRange(ethDashboardValueHourReq.getTimeRange());
        ethDashboardValueReq.setToAddressArray(ethDashboardValueHourReq.getToAddressArray());
        EthDashboardDto ethDashboardDto = ethDashboardService.listLineChartRangeSync(ethDashboardValueReq);
        Map<BigDecimal, LineChartResultValue> pointsOld = ethDashboardDto.getPoints();
        if (pointsOld.size() == 0){
            return ethDashboardDto;
        }
        BigDecimal size = new BigDecimal(ethDashboardDto.getPoints().size());
        BigDecimal moreSize = size.subtract(Constant.DAYHOURSIZE);
        BigDecimal maxTimeStamp = pointsOld.keySet().stream().max(Comparator.comparing(BigDecimal::abs)).get();
        maxTimeStamp = maxTimeStamp.subtract(Constant.HOURSTAMPTIME.multiply(moreSize));
        Map<BigDecimal, LineChartResultValue> pointsNew = new HashMap<>(Constant.DAYHOURSIZEINT);
        for (int i = 0; i < Constant.DAYHOURSIZEINT; i++) {
            pointsNew.put(maxTimeStamp, pointsOld.getOrDefault(maxTimeStamp, null));
            maxTimeStamp = maxTimeStamp.subtract(Constant.HOURSTAMPTIME);
        }
        ethDashboardDto.setPoints(pointsNew);
        BigDecimal txValue = new BigDecimal(0);
        int txTimes = 0;
        Set<String> playersSet = new HashSet<>();
        for (Map.Entry<BigDecimal, LineChartResultValue> doubleLineChartResultValueEntry : ethDashboardDto.getPoints().entrySet()) {
            LineChartResultValue value = doubleLineChartResultValueEntry.getValue();
            if (value != null){
                txValue = value.getTxValue().add(txValue);
                txTimes += value.getTxTimes();
                playersSet.addAll(value.getPlayersSet());
                playersSet.clear();
            }
        }
        ethDashboardDto.setPlayers(playersSet.size()).setTxTimes(txTimes).setTxValue(txValue);
        stringRedisTemplate.opsForValue().set(redisKey, JSONObject.toJSONString(ethDashboardDto), 10, TimeUnit.MINUTES);
        return ethDashboardDto;
    }
}
