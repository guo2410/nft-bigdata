package com.tj.bigdata.analysis.aop.monitor;

import com.tj.bigdata.analysis.constant.Constant;
import com.tj.bigdata.analysis.exception.EsException;
import com.tj.bigdata.analysis.util.TimerHandler;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * <h1>控制器 - 切面 - 监控</h1>
 *
 * @author guoch
 * @version 1.0
 * @since JDK 1.8
 */
@Slf4j
@Aspect
@Component
public class ControllerAspectMonitor {

    @Pointcut("execution(* com.tj.bigdata..*controller..*.*(..))")
    private void declareJoinPointerExpression(){
    }

    @Before("declareJoinPointerExpression()")
    private void before(JoinPoint joinPoint) throws Throwable{
        MDC.put("uri",(joinPoint.getSignature().getDeclaringTypeName()
        .concat(Constant.ROUND_DOT+joinPoint.getSignature().getName())
        .concat(Arrays.toString(joinPoint.getArgs()))));

        TimerHandler.start();
    }

    @After("declareJoinPointerExpression()")
    private void after(JoinPoint joinPoint){
        TimerHandler.end();
    }

    @AfterReturning(pointcut = "declareJoinPointerExpression()", returning = "returning")
    private void afterReturning(JoinPoint joinPoint, Object returning) {

    }

    @AfterThrowing(pointcut = "declareJoinPointerExpression()", throwing = "throwing")
    private void afterThrowing(JoinPoint joinPoint, Throwable throwing) throws Throwable {
        if (EsException.class.equals(throwing.getClass())) {
            throw new EsException(EsException.class.cast(throwing).getMessage());
        } else {
            throw throwing;
        }
    }
}
