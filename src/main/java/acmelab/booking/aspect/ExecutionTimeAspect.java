package acmelab.booking.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {

    @Pointcut("execution(* acmelab.booking.repository.*.*(..))")
    public void repositoryMethods() {}

    @Around("repositoryMethods()")
    public Object timeExecution(org.aspectj.lang.ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.nanoTime();

        Object result = joinPoint.proceed();

        long endTime = System.nanoTime();

        long duration = endTime - startTime;
        log.info("Execution time of {} is {} ms", joinPoint.getSignature(), duration / 1_000_000);

        return result;
    }
}
