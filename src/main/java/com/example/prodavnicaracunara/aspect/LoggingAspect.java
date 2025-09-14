package com.example.prodavnicaracunara.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @After("execution(* com.example.prodavnicaracunara.controller.*.*(..)) && !execution(* com.example.prodavnicaracunara.controller.*.*Exception*(..))")
    public void logAfterControllerMethod(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        
        StringBuilder argsInfo = new StringBuilder();
        if (args != null && args.length > 0) {
            argsInfo.append(" with arguments: ");
            for (int i = 0; i < args.length; i++) {
                if (i > 0) argsInfo.append(", ");
                argsInfo.append(args[i] != null ? args[i].getClass().getSimpleName() : "null");
            }
        }
        
        logger.info("AOP LOG - Method execution completed: {}.{}{}", 
                className, methodName, argsInfo.toString());
    }

    @After("execution(* com.example.prodavnicaracunara.service.*.*(..)) && !execution(* com.example.prodavnicaracunara.service.*.*Exception*(..))")
    public void logAfterServiceMethod(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        logger.debug("AOP LOG - Service method completed: {}.{}", className, methodName);
    }

    @After("execution(* com.example.prodavnicaracunara.repository.*.*(..)) && !execution(* com.example.prodavnicaracunara.repository.*.*Exception*(..))")
    public void logAfterRepositoryMethod(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        logger.debug("AOP LOG - Repository method completed: {}.{}", className, methodName);
    }
}