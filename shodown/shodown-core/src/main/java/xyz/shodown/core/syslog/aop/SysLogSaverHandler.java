package xyz.shodown.core.syslog.aop;

import lombok.extern.slf4j.Slf4j;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.entity.UserInfoDelegate;
import xyz.shodown.common.util.basic.UserInfoUtil;
import xyz.shodown.core.syslog.content.SysLogContent;

import java.util.function.Function;

/**
 * @ClassName: SysLogSaverHandler
 * @Description:
 * @Author: wangxiang
 * @Date: 2021/8/24 10:14
 */
@Slf4j(topic = LogCategory.PLATFORM)
public abstract class SysLogSaverHandler<T> implements SysLogSaver{

    /**
     * 用户信息类
     * @return 用户信息对应的类
     */
    public abstract Class<T> userInfoType();

    /**
     * 用于记录日志时能够区分用户的字段表达式
     * @return 表达式,如 {@code User::getUserName}
     */
    public abstract Function<T,String> userIdentifyFunc();

    /**
     * 执行保存操作
     * @param content 日志内容
     * @throws Exception Exception
     */
    protected void doSave(SysLogContent content) throws Exception {
        Class<T> clazz = userInfoType();
        Function<T,String> func = userIdentifyFunc();
        String userIdentify = UserInfoUtil.getUserDetail(clazz,func);
        content.setUsername(userIdentify);
        if(!save(content)){
            log.error("用户: "+content.getUsername() +"操作"+content.getOperationDesc()+" 方法:"+content.getMethod()+"的操作日志记录插入出错");
        }
    }

}
