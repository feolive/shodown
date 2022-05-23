package xyz.shodown.common.util.date;

import cn.hutool.core.date.ChineseDate;

import java.util.Date;

/**
 * @Author : caodaohua
 * @Date: 2021/6/17 15:20
 * @Description : 时间工具类
 * 包含返回农历日期 ChineseDate对象,获取方式:{@link DateUtil#getChineseDate(Date),DateUtil#getChineseDate(int, int, int)}
 * 如需使用jdk1.8 LocalDateTime可以直接使用hutool工具类{@link cn.hutool.core.date.LocalDateTimeUtil}
 */
public class DateUtil extends cn.hutool.core.date.DateUtil {

    /**
     * 通过公历日期构造
     *
     * @param date 公历日期
     */
    public static ChineseDate getChineseDate(Date date){
        return new ChineseDate(date);
    };


    /**
     * 构造方法传入日期
     *
     * @param chineseYear  农历年
     * @param chineseMonth 农历月，1表示一月（正月）
     * @param chineseDay   农历日，1表示初一
     * @since 5.2.4
     */
    public static ChineseDate getChineseDate(int chineseYear, int chineseMonth, int chineseDay){
        return new ChineseDate(chineseYear,chineseMonth,chineseDay);
    };

}
