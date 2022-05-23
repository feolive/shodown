package xyz.shodown.boot.upms.constants;

/**
 * @description: 常量存储
 * @author: wangxiang
 * @date: 2022/5/11 14:33
 */
public interface UpmsConstants {

    /**
     * token的缓存名称前缀
     */
    String TOKEN_LOGIN_CACHE_PREFIX = "token_login_cache::";

    /**
     * 存储对称加密密钥缓存前缀
     */
    String DYNAMIC_SECRET_KEY_PREFIX = "dynamic_secret_key->";

    /**
     * 登陆失败次数缓存前缀
     */
    String FAILED_LOGIN_TIMES_PREFIX = "failed_login_times::";

    /**
     * 匿名访问角色
     */
    String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";

    /**
     * 已登陆用户角色
     */
    String ROLE_LOGIN = "ROLE_LOGIN";

}
