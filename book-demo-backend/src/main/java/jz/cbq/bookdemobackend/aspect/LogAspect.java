package jz.cbq.bookdemobackend.aspect;

import jz.cbq.bookdemobackend.annotation.CBQLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * LogAspect
 *
 * @author cbq
 * @date 2023/12/15 14:54
 * @since 1.0.0
 */
@Component
@Slf4j
@Aspect
public class LogAspect {
    @Pointcut("@annotation(jz.cbq.bookdemobackend.annotation.CBQLog)")
    public void pointCut() {
    }

    @Before("pointCut()")
    public void before(JoinPoint point) {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                String ipAddress = request.getRemoteAddr();
                MethodSignature signature = (MethodSignature) point.getSignature();
                CBQLog annotation = signature.getMethod().getAnnotation(CBQLog.class);
                String value = annotation.value();

                String methodName = signature.getMethod().getName();
                String className = signature.getDeclaringTypeName();
                String packageName = signature.getDeclaringType().getPackage().getName();

                String args = Arrays.toString(point.getArgs());
                log.info("| ========================== CBQ-LOG-Start(Before) ================================ |");
                log.info("| Package: {}", packageName);
                log.info("| Method: {}.{} - IP: {} - Args: {} ", className, methodName, ipAddress, args);
                log.info("| Method: {}.{} - Annotation Value: {}", className, methodName, value);
                log.info("| ========================== CBQ-LOG-End(Before) ================================== |");
            }
        } catch (Exception e) {
            log.error("LogAspect before advice error: {}", e.getMessage(), e);
        }
    }

    @AfterThrowing(pointcut = "pointCut()", throwing = "e")
    public void afterThrowing(JoinPoint point, Throwable e) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        String methodName = signature.getMethod().getName();
        log.error("Exception in method: {} - Message: {}", methodName, e.getMessage(), e);
    }
}