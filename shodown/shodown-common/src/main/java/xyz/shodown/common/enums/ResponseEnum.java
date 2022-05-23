package xyz.shodown.common.enums;


/**
 * @ClassName: ResponseEnum
 * @Description: 响应枚举,也可使用org.springframework.http.HttpStatus
 * @Author: wangxiang
 * @Date: 2021/3/8 18:25
 */

public enum ResponseEnum {

    /**
     * 成功
     */
    SUCCESS(200,"Success"),

    /**
     * 业务异常
     */
    BUSINESS_EXCP(204,""),

    /**
     * 重定向
     */
    TEMPORARY_REDIRECT(307, "Temporary Redirect"),

    /**
     * 请求地址不正确
     */
    WRONG_URL(404,"URL is wrong,no interface mapped"),
    /**
     * 不支持以下请求方式
     */
    NOT_SUPPORT_METHOD(405,""),
    /**
     * 一般异常
     */
    NORMAL_EXCP(500,""),
    /**
     * 数据库主键冲突
     */
    DUPLICATE_KEY(500,"Unique key conflicts.Database has already got a record with the same unique key distinct from each one"),
    /**
     * 加密异常
     */
    ENCRYPT_EXCP(500,"Encrypt exception happened."),
    /**
     * 字段长度太长
     */
    DATA_TOO_LONG(500,"Data too long,beyond the limited range"),
    /**
     * 没有权限
     */
    NO_AUTH(510,"No authentication")

    ;


    private final int code;

    private final String msg;

    ResponseEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
