package com.epam.esm.logger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class AppLogger {
    private static final Logger logger = LogManager.getLogger();

    private static final int PACKAGE_NAME_PREFIX_LENGTH = 13;
    private static final String POINT = ".";
    private static final String BRACKETS = "(%s)";
    private static final String HYPHEN = " - ";
    private static final String STARTED = "started";
    private static final String ENDED = "ended";
    private static final String RETURN_VALUE = "return value = ";

    @Pointcut("execution(* com.epam.esm..*.*(..))")
    public void projectPointcut() {}

    @Pointcut("execution(* com.epam.esm.exception.handler.*.*(..))")
    public void exceptionHandlerPointcut() {}

    @Pointcut("this(org.springframework.web.filter.GenericFilterBean)")
    public void genericFilterBeanPointcut() {}

    @Around("projectPointcut() && !exceptionHandlerPointcut() && !genericFilterBeanPointcut()")
    public Object mainLogger(ProceedingJoinPoint pjp) throws Throwable {
        Signature signature = pjp.getSignature();
        String args = pjp.getArgs().length != 0 ? Arrays.toString(pjp.getArgs()) : "";
        infoLogger(signature, args, STARTED);
        Object proceed = pjp.proceed();
        if (proceed == null) {
            infoLogger(signature, args, ENDED);
        } else {
            infoLogger(signature, args, RETURN_VALUE + proceed.toString());
        }
        return proceed;
    }

    @Around("exceptionHandlerPointcut() && args(exception,..)")
    public Object exceptionLogger(ProceedingJoinPoint pjp, Exception exception) throws Throwable {
        Signature signature = pjp.getSignature();
        String args = pjp.getArgs().length != 0 ? Arrays.toString(pjp.getArgs()) : "";
        errorLogger(signature, args, exception);
        return pjp.proceed();
    }

    private void infoLogger(Signature signature, String args, String message) {
        logger.info(
                signature.getDeclaringTypeName().substring(PACKAGE_NAME_PREFIX_LENGTH) +
                POINT +
                signature.getName() +
                String.format(BRACKETS, args) +
                HYPHEN +
                message
        );
    }

    private void errorLogger(Signature signature, String args, Exception exception) {
        logger.error(
        signature.getDeclaringTypeName().substring(PACKAGE_NAME_PREFIX_LENGTH) +
                POINT +
                signature.getName() +
                String.format(BRACKETS, args),
                exception
        );
    }
}
