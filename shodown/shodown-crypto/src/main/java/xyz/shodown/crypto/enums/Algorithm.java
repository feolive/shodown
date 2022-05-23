package xyz.shodown.crypto.enums;


import xyz.shodown.crypto.handler.AesAlgorithmHandler;
import xyz.shodown.crypto.handler.RsaAlgorithmHandler;
import xyz.shodown.crypto.handler.Sm2AlgorithmHandler;
import xyz.shodown.crypto.handler.Sm4AlgorithmHandler;

/**
 * @ClassName: Algorithm
 * @Description: 加密算法枚举
 * @Author: wangxiang
 * @Date: 2021/4/19 10:04
 */
public enum Algorithm {

    /**
     * AES对称加密算法
     */
    AES("AES", AesAlgorithmHandler.class,true),

    /**
     * SM4对称加密算法(国密算法)
     */
    SM4("SM4", Sm4AlgorithmHandler.class,true),

    /**
     * RSA非对称加密算法
     */
    RSA("RSA", RsaAlgorithmHandler.class,false),

    /**
     * SM2非对称加密算法(国密算法)
     */
    SM2("SM2", Sm2AlgorithmHandler.class,false)

    ;


    /**
     * 算法码值
     */
    private String type;

    /**
     * 解释说明
     */
    private Class<?> clazz;

    /**
     * 是否对称加密;true是,false否
     */
    private boolean isSymmetric;

    Algorithm(String code, Class<?> clazz,boolean isSymmetric) {
        this.type = code;
        this.clazz = clazz;
        this.isSymmetric = isSymmetric;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public boolean isSymmetric() {
        return isSymmetric;
    }

    public void setSymmetric(boolean symmetric) {
        isSymmetric = symmetric;
    }
}
