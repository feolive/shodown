package xyz.shodown.flow.exception;

import xyz.shodown.common.consts.Symbols;
import xyz.shodown.flow.enums.ShodownFlowError;

/**
 * @ClassName: ShodownFlowException
 * @Description: shodown flow异常类
 * @Author: wangxiang
 * @Date: 2021/6/1 15:01
 */
public class ShodownFlowException extends RuntimeException{

    private String message;

    public ShodownFlowException(ShodownFlowError error){
        String err = error.getCode();
        String errMsg = error.getErrMsg();
        message = Symbols.LEFT_SQUARE_BRACKET + err + Symbols.RIGHT_SQUARE_BRACKET + errMsg;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
