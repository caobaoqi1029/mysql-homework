package jz.cbq.backend.exception;

import jz.cbq.backend.vo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Logger;

/**
 * 异常拦截器
 *
 * @author caobaoqi
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<String> doException(Exception exception) {
        if (log.isDebugEnabled()) {
            log.warn("GlobalExceptionHandler " + exception.getMessage());
            log.warn("=========ExceptionINFO-Start===============");
            log.error("ERROR DETAILS " ,exception);
            log.warn("=========ExceptionINFO-END===============");

        }
        return Result.fail("服务器出错 | 或存在关联信息请联系 1203952894@qq.com");
    }
}
