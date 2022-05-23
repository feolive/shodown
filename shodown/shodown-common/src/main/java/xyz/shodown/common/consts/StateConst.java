package xyz.shodown.common.consts;

/**
 * @ClassName: StateConst
 * @Description: 状态码
 * @Author: wangxiang
 * @Date: 2021/6/21 15:37
 */
public interface StateConst {

    /**
     * 字符串true
     */
    String TRUE = "true";

    /**
     * 字符串false
     */
    String FALSE = "false";

    /**
     * 使用字符串"1"表示肯定状态
     */
    String ACTIVE = "1";

    /**
     * 使用字符串"0"表示否定状态
     */
    String INACTIVE = "0";

    /**
     * 使用整型1表示肯定状态
     */
    Integer ACTIVE_INT = 1;

    /**
     * 使用整型0表示否定状态
     */
    Integer INACTIVE_INT = 0;

    /**
     * on
     */
    String ON = "on";

    /**
     * off
     */
    String OFF = "off";
}
