package com.tj.bigdata.analysis.task.schedule;

import com.tj.bigdata.analysis.job.eth.EthMonitorStrategy;
import com.tj.bigdata.analysis.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author guoch
 */
@Slf4j
@Component
public class TaskSchedule {

    private EthMonitorStrategy ethMonitorStrategy;

    private ThreadPoolExecutor executorService;

    private RedisUtil redisUtil;

    @Autowired
    public void setRedisUtil(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Autowired
    public void setExecutorService(ThreadPoolExecutor executorService) {
        this.executorService = executorService;
    }

    @Autowired
    public void setEthMonitorStrategy(EthMonitorStrategy ethMonitorStrategy) {
        this.ethMonitorStrategy = ethMonitorStrategy;
    }

    public void execute(Runnable r) {
        executorService.execute(r);
    }

    /**
     * 定时任务 清除redis缓存
     */
    @Async("threadPoolExecutor")
    @Scheduled(cron = "0 0 0 */2 * ?")
    public void deleteRedisCache() {
        execute(() -> {
            redisUtil.deleteSortedSetDay();
            redisUtil.deleteSortedSetHour();
        });
    }

    @Async("threadPoolExecutor")
    @Scheduled(cron = "0 0 */1 * * ?")
    public void syncDapp() {
        execute(() -> {
            ethMonitorStrategy.syncDapp();
        });
    }

    /**
     * 定时任务 每小时执行
     * 记录每个合约的时间节点余额
     */
    @Async("threadPoolExecutor")
    @Scheduled(cron = "0 0 */1 * * ?")
    public void ethBalanceHour() {
        execute(() -> {
            try {
                ethMonitorStrategy.syncBalance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
