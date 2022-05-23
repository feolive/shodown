package xyz.shodown.core.syslog.exception;

/**
 * @ClassName: SysLogException
 * @Description: 操作日志异常
 * @Author: wangxiang
 * @Date: 2021/8/23 14:35
 */
public class SysLogException extends RuntimeException{

    public SysLogException(String message) {
        super(message);
    }
}
