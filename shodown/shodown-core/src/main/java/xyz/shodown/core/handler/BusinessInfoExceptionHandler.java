package xyz.shodown.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.consts.Symbols;
import xyz.shodown.common.enums.ResponseEnum;
import xyz.shodown.common.exception.BusinessInfoException;
import xyz.shodown.common.response.Result;
import xyz.shodown.common.util.basic.StringUtil;

import java.io.Serializable;

/**
 * @ClassName: BusinessExceptionHandler
 * @Description: 记录info级别的business日志
 * @Author: wangxiang
 * @Date: 2021/6/24 19:06
 */
@ControllerAdvice
@Slf4j(topic = LogCategory.BUSINESS)
public class BusinessInfoExceptionHandler {

    /**
     * 业务异常处理,记录至 business日志文件夹下,记录info级别日志
     * @param e BusinessException
     * @return 业务提示结果
     */
    @ExceptionHandler(BusinessInfoException.class)
    @ResponseBody
    public Result<?> handleBusinessException(BusinessInfoException e){
        Serializable code = ResponseEnum.BUSINESS_EXCP.getCode();
        String msg = e.getMessage();
        if(!StringUtil.isBlank(e.getCode())){
            code = e.getCode();
            msg = Symbols.LEFT_SQUARE_BRACKET+code+Symbols.RIGHT_SQUARE_BRACKET+e.getMessage();
        }
        log.info(msg);
        return Result.fail(code,e.getMessage());
    }
}
