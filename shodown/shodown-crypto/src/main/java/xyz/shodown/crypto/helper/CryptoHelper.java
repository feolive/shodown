package xyz.shodown.crypto.helper;

import cn.hutool.extra.spring.SpringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.consts.Symbols;
import xyz.shodown.common.enums.EncodingEnum;
import xyz.shodown.common.util.basic.ArrayUtil;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.crypto.entity.EncryptRes;
import xyz.shodown.crypto.enums.Algorithm;
import xyz.shodown.crypto.enums.CharSet;
import xyz.shodown.crypto.enums.CryptoErr;
import xyz.shodown.crypto.exception.ShodownCryptoException;
import xyz.shodown.crypto.exception.VerifySignException;
import xyz.shodown.crypto.factory.AlgorithmFactory;
import xyz.shodown.crypto.factory.KeyChainFactory;
import xyz.shodown.crypto.handler.AlgorithmHandlerAdapter;
import xyz.shodown.crypto.keychain.KeyChain;

/**
 * @ClassName: CryptoHelper
 * @Description: 加解密工具封装
 * @Author: wangxiang
 * @Date: 2021/7/9 9:00
 */
@Data
@Slf4j(topic = LogCategory.BUSINESS)
public class CryptoHelper {

    /**
     * 加密算法
     */
    private Algorithm algorithm;

    /**
     * <p>
     *     使用的密钥class,如果未指定则使用全局KeyChain,全局KeyChain未指定则使用默认KeyChain
     *     优先级低于privateKey,publicKey,secretKey
     * </p>
     */
    private Class<? extends KeyChain> keyChain;

    /**
     * 私钥(非对称加密使用)
     */
    private String privateKey;

    /**
     * 公钥(非对称加密使用)
     */
    private String publicKey;

    /**
     * 密钥(对称加密使用)
     */
    private String secretKey;

    /**
     * iv向量
     */
    private String iv;

    /**
     * 字节数组编码字符串使用的编码
     */
    private EncodingEnum encodingEnum = EncodingEnum.BASE64_URL_SAFE;

    /**
     * 字符编码
     */
    private CharSet charSet = CharSet.UTF8;

    /**
     * 用于描述加解密过程的业务,debug日志级别
     */
    private String topic="";

    /**
     * 非对称加密签名
     */
    private String signForVerify;

    public CryptoHelper(){
        super();
    }

    /**
     * 实例化
     * @param algorithm 加密算法
     * @param keyChain 加解密钥匙串
     * @param encodingEnum 字符与字节转换编码格式
     * @param charSet 字符编码类型
     * @param topic debug日志记录名称
     * @param signForVerify 签名
     */
    public CryptoHelper(Algorithm algorithm, Class<? extends KeyChain> keyChain, EncodingEnum encodingEnum, CharSet charSet, String topic, String signForVerify) {
        this.algorithm = algorithm;
        this.keyChain = keyChain;
        this.encodingEnum = encodingEnum;
        this.charSet = charSet;
        this.topic = topic;
        this.signForVerify = signForVerify;
    }

    /**
     * 实例化
     * @param algorithm 加密算法
     * @param keyChain 加密钥匙串
     * @param topic debug日志记录名称
     * @param signForVerify 签名
     */
    public CryptoHelper(Algorithm algorithm, Class<? extends KeyChain> keyChain, String topic, String signForVerify) {
        this.algorithm = algorithm;
        this.keyChain = keyChain;
        this.topic = topic;
        this.signForVerify = signForVerify;
    }

    /**
     * 实例化
     * @param algorithm 加密算法
     * @param keyChain 加密钥匙串
     * @param topic debug日志记录名称
     */
    public CryptoHelper(Algorithm algorithm,Class<? extends KeyChain> keyChain,String topic){
        this.algorithm = algorithm;
        this.keyChain = keyChain;
        this.topic = topic;
    }

    /**
     * 实例化
     * @param algorithm 加密算法
     */
    public CryptoHelper(Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    /**
     * 实例化
     * @param algorithm 对称加密算法
     * @param secretKey 对称加密密钥
     */
    public CryptoHelper(Algorithm algorithm,String secretKey){
        this.algorithm = algorithm;
        this.secretKey = secretKey;
    }

    /**
     * 实例化
     * @param algorithm 加密算法
     * @param privateKey 私钥
     * @param publicKey 公钥
     */
    public CryptoHelper(Algorithm algorithm,String privateKey,String publicKey){
        this.algorithm = algorithm;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
    }

    /**
     * 实例化
     * @param algorithm 非对称加密算法
     * @param privateKey 私钥
     * @param publicKey 公钥
     * @param signForVerify 签名
     */
    public CryptoHelper(Algorithm algorithm,String privateKey,String publicKey,String signForVerify){
        this.algorithm = algorithm;
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.signForVerify = signForVerify;
    }

    /**
     * 解密
     * @param encryptData 加密数据
     * @return 解密明文
     * @throws Exception 异常
     */
    public String decrypt(String encryptData) throws Exception {
        basicValidate();
        if(StringUtil.isBlank(encryptData)){
            return "";
        }
        AlgorithmHandlerAdapter handler = prepareHandler();
        String t = StringUtil.isBlank(topic)?topic:Symbols.LEFT_SQUARE_BRACKET+topic+Symbols.RIGHT_SQUARE_BRACKET+Symbols.MINUS;
        log.debug("[解密处理],"+t+"解密开始,接收数据为:{}",encryptData);
        // 总开关
        if(!handler.fetchSwitch()){
            log.debug("[解密处理],"+t+"加解密总开关关闭,保持原始数据:{}",encryptData);
            return encryptData;
        }
        handler.setData(encryptData);
        String res = handler.decrypt();
        // 判断验签
        if(shouldVerify()){
            if(StringUtil.isBlank(this.signForVerify)) {
                throw new VerifySignException(t, CryptoErr.NO_SIGN_FIELD_SET);
            }
            // 此处验签是对方使用privateKey对明文数据生成数字签名,所以需要赋值明文数据
            handler.setData(res);
            if(handler.verify(signForVerify)){
                log.debug("[解密处理],"+t+"解密结束,解密后结果:{}",res);
                return res;
            }else{
                throw new VerifySignException(CryptoErr.VERIFY_ERR);
            }
        }
        return res;
    }

    /**
     * 加密
     * @param data 明文数据
     * @return 加密数据
     * @throws Exception 异常
     */
    public EncryptRes encrypt(String data) throws Exception{
        basicValidate();
        AlgorithmHandlerAdapter handler = prepareHandler();
        String t = StringUtil.isBlank(topic)?topic:Symbols.LEFT_SQUARE_BRACKET+topic+Symbols.RIGHT_SQUARE_BRACKET+Symbols.MINUS;
        EncryptRes res = new EncryptRes();
        log.debug("[加密处理],"+t+"加密开始,接收数据为:{}",data);
        // 总开关
        if(!handler.fetchSwitch()){
            log.debug("[加密处理],"+t+"加解密总开关关闭,保持原始数据:{}",data);
            res.setEncryptData(data);
            return res;
        }
        handler.setData(data);
        String encryptData = handler.encrypt();
        // 判断生成签名
        if(shouldVerify()){
            String sign = handler.sign();
            res.setSign(sign);
            log.debug("[加密处理],"+t+"签名生成:{}",sign);
        }
        res.setEncryptData(encryptData);
        log.debug("[加密处理],"+t+"加密结束,加密后结果:{}",encryptData);
        return res;
    }

    /**
     * 基础属性校验
     */
    private void basicValidate(){
        if(algorithm==null){
            throw new ShodownCryptoException(CryptoErr.ALGORITHM_NULL);
        }
    }

    /**
     * 准备handler
     * @return handler
     * @throws Exception 异常
     */
    private AlgorithmHandlerAdapter prepareHandler() throws Exception {
        AlgorithmHandlerAdapter handler = AlgorithmFactory.createAdapter(algorithm);
        handler.setCharSet(charSet);
        handler.setEncoding(encodingEnum);
        if(!StringUtil.isBlank(secretKey)&&!StringUtil.isBlank(privateKey)&&!StringUtil.isBlank(publicKey)){
            throw new RuntimeException("secretKey不可以和privateKey+publicKey同时存在");
        }
        if(!StringUtil.isBlank(secretKey)){
            KeyChain keyChain = secretKeyChain(secretKey,iv);
            handler.setKeyChain(keyChain);
            return handler;
        }else if(!StringUtil.isBlank(privateKey)&&!StringUtil.isBlank(publicKey)){
            KeyChain keyChain = privateAndPublicKeyChain(privateKey,publicKey);
            handler.setKeyChain(keyChain);
            return handler;
        }else {
            if(keyChain==null|| ArrayUtil.isEmpty(SpringUtil.getBeanNamesForType(keyChain))){
                handler.setKeyChain(KeyChainFactory.getGlobalKeyChain());
                return handler;
            }
            KeyChain kc = SpringUtil.getBean(keyChain);
            if(!StringUtil.isBlank(kc.getPrivateKey())&&!StringUtil.isBlank(kc.getPublicKey())){
                handler.setKeyChain(kc);
            }else if(!StringUtil.isBlank(kc.getSecretKey())){
                handler.setKeyChain(kc);
            }else {
                handler.setKeyChain(KeyChainFactory.getGlobalKeyChain());
            }
            return handler;
        }
    }

    /**
     * 判断是否需要进行验签,以及生成签名
     * @return true需要 false不需要
     */
    private boolean shouldVerify(){
        return !algorithm.isSymmetric();
    }

    private KeyChain secretKeyChain(String secretKey,String iv){
        return new KeyChain() {
            @Override
            public String getPrivateKey() {
                return null;
            }

            @Override
            public String getPublicKey() {
                return null;
            }

            @Override
            public String getSecretKey() {
                return secretKey;
            }

            @Override
            public String getIv() {
                return iv;
            }
        };
    }

    private KeyChain privateAndPublicKeyChain(String privateKey,String publicKey){
        return new KeyChain() {
            @Override
            public String getPrivateKey() {
                return privateKey;
            }

            @Override
            public String getPublicKey() {
                return publicKey;
            }

            @Override
            public String getSecretKey() {
                return null;
            }

            @Override
            public String getIv() {
                return null;
            }
        };
    }
}
