package xyz.shodown.core.syslog.content;


import lombok.Data;

import java.util.Date;

/**
 * @ClassName: SysLogContent
 * @Description: 日志内容
 * @Author: wangxiang
 * @Date: 2021/8/23 11:33
 */
@Data
public class SysLogContent {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户操作
     */
    private String operationDesc;

    /**
     * 请求方法
     */
    private String method;

    /**
     * 请求参数
     */
    private String params;

    /**
     * 执行时长(毫秒)
     */
    private Long duration;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 创建时间
     */
    private Date createDate;

}
