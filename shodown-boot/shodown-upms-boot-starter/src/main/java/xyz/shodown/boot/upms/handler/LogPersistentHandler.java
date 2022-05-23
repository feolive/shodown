package xyz.shodown.boot.upms.handler;

import org.springframework.stereotype.Component;
import xyz.shodown.boot.upms.entity.ShodownSysLog;
import xyz.shodown.boot.upms.model.UserBaseInfo;
import xyz.shodown.boot.upms.repository.ShodownSysLogRepository;
import xyz.shodown.core.syslog.aop.SysLogSaverHandler;
import xyz.shodown.core.syslog.content.SysLogContent;

import javax.annotation.Resource;
import java.util.function.Function;

/**
 * @description: 日志工具持久层实现
 * @author: wangxiang
 * @date: 2022/5/5 14:18
 */
@Component
public class LogPersistentHandler extends SysLogSaverHandler<UserBaseInfo> {

    @Resource
    private ShodownSysLogRepository shodownSysLogRepository;

    @Override
    public boolean save(SysLogContent content) throws Exception {
        ShodownSysLog sysLog = prepareSave(content);
        shodownSysLogRepository.save(sysLog);
        return true;
    }

    @Override
    public Class<UserBaseInfo> userInfoType() {
        return UserBaseInfo.class;
    }

    @Override
    public Function<UserBaseInfo, String> userIdentifyFunc() {
        return UserBaseInfo::getUserId;
    }

    private ShodownSysLog prepareSave(SysLogContent content){
        ShodownSysLog sysLog = new ShodownSysLog();
        sysLog.setDuration(content.getDuration());
        sysLog.setIp(content.getIp());
        sysLog.setMethod(content.getMethod());
        sysLog.setCreateDate(content.getCreateDate());
        sysLog.setParams(content.getParams());
        sysLog.setUserId(content.getUsername());
        sysLog.setOperationDesc(content.getOperationDesc());
        return sysLog;
    }
}
