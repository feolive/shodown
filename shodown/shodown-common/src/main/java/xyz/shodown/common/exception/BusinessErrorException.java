package xyz.shodown.common.exception;

/**
 * @ClassName: BusinessLogException
 * @Description: 业务错误异常,日志记录于exception目录下,级别为error
 * @Author: wangxiang
 * @Date: 2021/3/30 10:34
 */
public class BusinessErrorException extends RuntimeException{

    public BusinessErrorException(String message) {
        super(message);
    }

    public BusinessErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessErrorException(Throwable cause) {
        super(cause);
    }
}
