package xyz.shodown.crypto.keychain;

/**
 * @ClassName: GlobalKeyChain
 * @Description: 全局密钥
 * @Author: wangxiang
 * @Date: 2021/5/24 14:37
 */
public class GlobalKeyChain extends KeyChain {

    private boolean isInit = false;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 密钥,对称加密使用
     */
    private String secretKey;

    /**
     * 向量,对称加密使用
     */
    private String iv;


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
        return secretKey;
    }

    @Override
    public String getIv() {
        return iv;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public boolean isInit() {
        return isInit;
    }

    public void setInit(boolean init) {
        isInit = init;
    }
}
