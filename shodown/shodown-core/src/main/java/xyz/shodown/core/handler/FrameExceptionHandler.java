package xyz.shodown.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.consts.Symbols;
import xyz.shodown.common.enums.ResponseEnum;
import xyz.shodown.common.exception.BusinessErrorException;
import xyz.shodown.common.response.Result;

import java.util.List;

/**
 * @ClassName: FrameExceptionHandler
 * @Description: 异常处理
 * @Author: wangxiang
 * @Date: 2021/3/9 14:21
 */
@ControllerAdvice
@Slf4j(topic = LogCategory.EXCEPTION)
public class FrameExceptionHandler {

    /**
     * 业务异常处理,并记录error级别日志
     * @param e 业务异常
     * @return 业务信息返回
     */
    @ExceptionHandler(BusinessErrorException.class)
    @ResponseBody
    public Result<?> handleBusinessLogException(BusinessErrorException e){
        log.error(e.getMessage(),e);
        return Result.fail(ResponseEnum.BUSINESS_EXCP.getCode(),e.getMessage());
    }

    /**
     * 请求地址不正确404
     * @param e NoHandlerFoundException
     * @return 404提示消息
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public Result<?> handleNoFoundException(NoHandlerFoundException e) {
        return Result.fail(ResponseEnum.WRONG_URL);
    }

    /**
     * 主键冲突
     * @param e DuplicateKeyException
     * @return 冲突提示消息
     */
    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    public Result<?> handleDuplicateKeyException(DuplicateKeyException e){
        log.error(e.getMessage(), e);
        return Result.fail(ResponseEnum.DUPLICATE_KEY);
    }

    /**
     * 一般异常
     * @param e Exception
     * @return 异常提示
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result<?> handleException(Exception e){
        log.error(e.getMessage(), e);
        return Result.fail("There is an exception,"+e.getMessage());
    }

    /**
     * 请求方法不支持
     * @param e HttpRequestMethodNotSupportedException
     * @return 错误提示
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result<?> httpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e){
        StringBuilder sb = new StringBuilder();
        sb.append("The request method").append(Symbols.LEFT_SQUARE_BRACKET);
        sb.append(e.getMethod());
        sb.append(Symbols.RIGHT_SQUARE_BRACKET).append(" is not supported").append(Symbols.FULL_STOP);
        String [] methods = e.getSupportedMethods();
        if(methods!=null){
            sb.append("These given are supported").append(Symbols.COLON);
            for(String str:methods){
                sb.append(str);
                sb.append(Symbols.COMMA);
            }
        }
        return Result.fail(ResponseEnum.NOT_SUPPORT_METHOD.getCode(),sb.toString());
    }

    /**
     * 数据实体校验
     * @param e MethodArgumentNotValidException
     * @return 错误提示
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<?> methodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult result = e.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            for (int i = 0; i < errors.size(); i++) {
                FieldError fieldError = (FieldError) errors.get(i);
                if(i==errors.size()-1){
                    stringBuilder.append(fieldError.getDefaultMessage());
                }else {
                    stringBuilder.append(fieldError.getDefaultMessage()).append(Symbols.COMMA);
                }
            }
        }
        return Result.fail(stringBuilder.toString());
    }

}
