package com.tj.bigdata.analysis.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

/**
 * @author guoch
 */
@Component
public class Web3jConfig {

    @Value("${eth.chain.rpc}")
    private String rpc;

    private static Web3j web3j = null;

    private synchronized Web3j init() {
        if (!ObjectUtils.isEmpty(web3j)) {
            return web3j;
        }
        // 实例化web3j
        web3j = Web3j.build(new HttpService(rpc));
        return web3j;
    }

    public Web3j getWeb3j() {
        // 获取web3
        if (!ObjectUtils.isEmpty(web3j)) {
            return web3j;
        }
        return init();
    }
}

