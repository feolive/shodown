package xyz.shodown.crypto.enums;

/**
 * @ClassName: CryptoErr
 * @Description:
 * @Author: wangxiang
 * @Date: 2021/5/25 9:58
 */
public enum CryptoErr {
    /**
     * 签名校验错误
     */
    VERIFY_ERR("1001","签名与加密内容不对应,信息被篡改"),
    /**
     * 非密钥串接口子类错误
     */
    NOT_SUB_KEY_CHAIN("1002","当前密钥实现类不是KeyChain子类"),

    /**
     * 缺少@KeyRegister注解
     */
    WITHOUT_KEY_REGISTER("1003","缺少@KeyRegister注解于类上"),

    /**
     * 不是AlgorithmHandler子类
     */
    NOT_SUBCLASS_OF_ALGORITHM_HANDLER("1004","所需实例化的算法处理实体类不是AlgorithmHandler子类"),

    /**
     * 不是AlgorithmHandlerAdapter子类
     */
    NOT_SUBCLASS_OF_ALGORITHM_ADAPTER("1005","所需实例化的算法处理实体类不是AlgorithmHandlerAdapter子类"),

    /**
     * Algorithm算法未设置
     */
    ALGORITHM_NULL("1006","Algorithm算法未设置"),

    /**
     * shodown.crypto.switch赋值错误
     */
    SWITCH_VAL_LIMIT("1007","shodown.crypto.switcher配置赋值时必须是true/on/false/off中的一个"),

    /**
     * header头部需要有sign
     */
    NO_SIGN_IN_HEADER("1008","[解密处理],缺少签名,header中需要有[sign]字段用于验签,非对称加密必须带有签名"),

    /**
     * 对象赋值签名
     */
    NO_SIGN_FIELD_SET("1009","[解密处理],缺少签名,CryptoHelper对象未对[signForVerify]属性赋值用于验签,非对称加密必须带有签名")

    ;

    CryptoErr(String code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }

    /**
     * 错误码
     */
    private String code;

    /**
     * 错误信息
     */
    private String errMsg;

    public String getCode() {
        return code;
    }

    public String getErrMsg() {
        return errMsg;
    }
}
