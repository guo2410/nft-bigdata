package com.tj.bigdata.analysis.task.schedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author guoch
 */
@Slf4j
@Component
public class HelloSchedule {

    @Async
    @Scheduled(cron = "*/1 * * * * ?")
    public void hello() throws InterruptedException {
//        log.info("hello");
//        Thread.sleep(3000);
    }



    @Async
    @Scheduled(cron = "0 0 */1 * * ?")
    public void hello2() throws InterruptedException {
//        log.info("hello");
//        Thread.sleep(3000);
    }
}
