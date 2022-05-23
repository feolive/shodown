package xyz.shodown.boot.upms.keychain;

import xyz.shodown.boot.upms.config.AdditionalProperties;
import xyz.shodown.boot.upms.model.UserBaseInfo;
import xyz.shodown.boot.upms.util.ShodownUpmsUtil;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.common.util.basic.UserInfoUtil;

import javax.annotation.Resource;

/**
 * @description: 与登陆用户绑定的动态对称加密密钥存储
 * @author: wangxiang
 * @date: 2022/5/11 10:21
 */
public abstract class UserSecretKeyStorage implements DynamicSecretKeyGetter{

    @Resource
    private AdditionalProperties additionalProperties;

    /**
     * <p>保存对称密钥,与用户绑定;每次登陆会更新</p>
     * @param userId 用户id
     * @param secretKey 密钥
     */
    public abstract void saveUserSecretKey(String userId,String secretKey);

    /**
     * <p>获取对称密钥;</p>
     * @param userId 用户id
     * @return 密钥
     */
    public abstract String getUserSecretKey(String userId);

    /**
     * 保存iv
     * @param iv iv向量
     */
    public abstract void storeIv(String iv);

    /**
     * 删除iv
     */
    public abstract void removeIv();

    @Override
    public String getSecretKey() {
        UserBaseInfo userBaseInfo = UserInfoUtil.getUserInfo(UserBaseInfo.class);
        if(userBaseInfo==null){
            throw new RuntimeException("用户未登陆,请先登陆");
        }
        String userId = userBaseInfo.getUserId();
        if(StringUtil.isBlank(userId)){
            return null;
        }else {
            return getUserSecretKey(userId);
        }
    }

}
