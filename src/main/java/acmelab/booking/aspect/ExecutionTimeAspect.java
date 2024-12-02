package acmelab.booking.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class QueryExecutionTimeAspect {


    // Pointcut: match any method in repository that starts with "find" or "get"
    @Pointcut("execution(* acmelab.booking.repository.*.*(..))")
    public void repositoryMethods() {}

    // Around advice: will run before and after the repository method executes
    @Around("repositoryMethods()")
    public Object timeQueryExecution(org.aspectj.lang.ProceedingJoinPoint joinPoint) throws Throwable {
        // Record start time
        long startTime = System.nanoTime();

        // Proceed with the actual method execution
        Object result = joinPoint.proceed();

        // Record end time
        long endTime = System.nanoTime();

        // Calculate the time taken and log it
        long duration = endTime - startTime;
        log.info("Execution time of {} is {} ms", joinPoint.getSignature(), duration / 1_000_000);

        return result;
    }
}
