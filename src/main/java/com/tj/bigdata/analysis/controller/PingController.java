package com.tj.bigdata.analysis.controller;

import com.tj.bigdata.analysis.interceptor.ClientServerContext;
import com.tj.bigdata.analysis.job.eth.EthMonitorStrategy;
import com.tj.bigdata.analysis.util.DateUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * test
 *
 * @author guoch
 */
@RestController
@RequestMapping(value = "/api")
public class PingController {


    @Autowired
    private EthMonitorStrategy ethMonitorStrategy;

    @Autowired
    StringRedisTemplate redisTemplate;

    @ApiOperation(value = "测接口是否通")
    @RequestMapping(value = "/ping", method = {RequestMethod.GET})
    public String ping(HttpServletRequest request) {
        return "admin，访问ip:" + ClientServerContext.getContext().getIp() + ",访问时间：" + DateUtil.format(new Date(), DateUtil.DEFAULT_DATE_TIME_RFGFX);
    }

    @ApiOperation(value = "测试")
    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index() {
        redisTemplate.opsForValue().set("name", "guoch");
        return "ok";
    }
}
