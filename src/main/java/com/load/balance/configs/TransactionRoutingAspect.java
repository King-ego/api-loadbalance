package com.load.balance.configs;

import com.load.balance.enums.DataSourceType;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.atomic.AtomicInteger;

@Aspect
@Component
public class TransactionRoutingAspect {
    private final AtomicInteger counter = new AtomicInteger(0);

    @Around("@annotation(transactional)")
    public Object route(ProceedingJoinPoint pjp, Transactional transactional) throws Throwable {
        try{
            if (transactional.readOnly()){
                int index  = counter.incrementAndGet() % 2;
                DataSourceContextHolder.setDataSourceType(
                        index == 0
                         ? DataSourceType.READ_1
                         : DataSourceType.READ_2
                );
            } else {
                DataSourceContextHolder.setDataSourceType(DataSourceType.WRITE);
            }

            return pjp.proceed();
        }
        finally {
            DataSourceContextHolder.clear();
        }
    }
}
