package xyz.shodown.boot.upms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xyz.shodown.boot.upms.entity.ShodownSysLog;

/**
 * @description: 日志持久化操作
 * @author: wangxiang
 * @date: 2022/5/5 15:23
 */
@Repository
public interface ShodownSysLogRepository extends JpaRepository<ShodownSysLog,Long> {

}
