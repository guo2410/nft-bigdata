package com.tj.bigdata.analysis.interceptor;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author guoch
 */
@Slf4j
@Data
public class ClientServerContext {
    /**
     * 线程隔离：访问到的变量属于当前线程，即保证每个线程的变量不一样，而同一个线程在任何地方拿到的变量都是一致的
     */
    private static final ThreadLocal<ClientServerContext> LOCAL = ThreadLocal.withInitial(ClientServerContext::new);

    /**
     * get context.
     *
     * @return context
     */
    public static ClientServerContext getContext() {
        // 启动时执行一次
        return LOCAL.get();
    }

    /**
     * remove context. com.alibaba.dubbo.rpc.filter.ContextFilter (dubbo)
     */
    public static void removeContext() {
        LOCAL.remove();
    }

    /**
     * 请求的IP地址
     */
    private String ip;
}
