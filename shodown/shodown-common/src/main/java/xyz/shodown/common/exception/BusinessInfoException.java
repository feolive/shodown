package xyz.shodown.common.exception;

/**
 * @ClassName: BusinessException
 * @Description: 业务异常,记录在business目录下的日志,info级别.一般用于不重要的提示返回
 * @Author: wangxiang
 * @Date: 2021/3/8 18:26
 */
public class BusinessInfoException extends RuntimeException{

    private String code;

    public BusinessInfoException(String message) {
        super(message);
    }

    public BusinessInfoException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessInfoException(Throwable cause) {
        super(cause);
    }

    public BusinessInfoException(String code,String message){
        super(message);
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

}
