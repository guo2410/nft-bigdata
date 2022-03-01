package com.tj.bigdata.analysis.controller;

import com.tj.bigdata.analysis.entity.*;
import com.tj.bigdata.analysis.exception.IllegalArgumentsException;
import com.tj.bigdata.analysis.service.EthDashboardService;
import com.tj.bigdata.analysis.util.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

/**
 * @author guoch
 */
@Slf4j
@RequestMapping(value = "/api/eth/value")
@RestController
public class EthDashboardController {

    private EthDashboardService ethDashboardService;

    @Autowired
    public void setEthDashboardService(EthDashboardService ethDashboardService) {
        this.ethDashboardService = ethDashboardService;
    }

    @PostMapping("/statistics/chart/line")
    @ApiOperation("以太币数据折线统计图")
    public Result<EthDashboardValueLineChartRes> listPoint(@RequestBody @Valid EthDashboardValueReq ethDashboardValueReq) {
        if (ethDashboardValueReq.getChain() != 1){
            throw new IllegalArgumentsException("chain-type-error","链类型错误");
        }
        log.info("入参 {}===", ethDashboardValueReq);
        EthDashboardValueLineChartRes ethDashboardValueLineChartRes = new EthDashboardValueLineChartRes();
        EthDashboardDto ethDashboardDto = ethDashboardService.listLineChartRangeSync(ethDashboardValueReq);
        if (Objects.nonNull(ethDashboardDto)) {
            ethDashboardValueLineChartRes.setPoints(ethDashboardDto.getPoints()).setPlayers(ethDashboardDto.getPlayers())
                    .setTxTimes(ethDashboardDto.getTxTimes()).setTxValue(ethDashboardDto.getTxValue());
        }
        if (ethDashboardValueLineChartRes.getPoints().size() == 0){
            EthNullPointsReq ethNullPointsReq = new EthNullPointsReq();
            ethNullPointsReq.setRange(ethDashboardValueReq.getTimeRange());
            EthNullPointsRes ethNullPointsRes = ethDashboardService.listNullPointsModel(ethNullPointsReq);
            ethDashboardValueLineChartRes.setPoints(ethNullPointsRes.getPoints());
        }
        return Result.of(Result.Type.SUCCESS_ZH, ethDashboardValueLineChartRes);
    }

    @PostMapping("/statistics/chart/hour")
    @ApiOperation("以太币数据24小时统计")
    public Result<EthDashboardValueHourRes> listHours(@RequestBody @Valid EthDashboardValueHourReq ethDashboardValueReq) {
        if (ethDashboardValueReq.getChain() != 1){
            throw new IllegalArgumentsException("chain-type-error","链类型错误");
        }
        log.info("入参 {}===", ethDashboardValueReq);
        EthDashboardValueHourRes ethDashboardValueHourRes = new EthDashboardValueHourRes();
        EthDashboardHourDto ethDashboardHourDto = ethDashboardService.hourFromFactory(ethDashboardValueReq);
        ethDashboardValueHourRes.setPoints(ethDashboardHourDto.getPoints()).setTxTimes(ethDashboardHourDto.getTxTimes())
                .setPlayers(ethDashboardHourDto.getPlayers()).setTxValue(ethDashboardHourDto.getTxValue());
        return Result.of(Result.Type.SUCCESS_ZH, ethDashboardValueHourRes);
    }

    @PostMapping("/statistics/chart/balance")
    @ApiOperation("以太币数据24小时余额")
    public Result<EthDashboardHourBalanceRes> listHoursBalance(@RequestBody @Valid EthDashboardValueReq ethDashboardValueReq) {
        if (ethDashboardValueReq.getChain() != 1){
            throw new IllegalArgumentsException("chain-type-error","链类型错误");
        }
        log.info("入参 {}===", ethDashboardValueReq);
        EthDashboardHourBalanceRes hourBalanceRes = new EthDashboardHourBalanceRes();
        EthDashboardHourBalanceDto ethDashboardHourBalanceDto = ethDashboardService.listLineChartRangeBalance(ethDashboardValueReq);
        if (ethDashboardHourBalanceDto != null && ethDashboardHourBalanceDto.getPoints() != null) {
            hourBalanceRes.setPoints(ethDashboardHourBalanceDto.getPoints()).setBalance(ethDashboardHourBalanceDto.getBalance());
        }
        return Result.of(Result.Type.SUCCESS_ZH, hourBalanceRes);
    }

}
