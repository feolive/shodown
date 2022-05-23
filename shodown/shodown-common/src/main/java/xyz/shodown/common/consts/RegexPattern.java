package xyz.shodown.common.consts;

import javax.validation.constraints.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName: RegexEnum
 * @Description: 常用正则表达式
 * @Author: wangxiang
 * @Date: 2021/6/25 14:44
 */
public interface RegexPattern {

    /**
     * 整数
     */
    String INTEGER = "^(0|-*[1-9][0-9]*)$";
    /**
     * 0或正整数
     */
    String POSITIVE_INTEGER_OR_ZERO = "^(0|[1-9][0-9]*)$";
    /**
     * 正整数
     */
    String POSITIVE_INTEGER = "^[1-9]\\d*$";
    /**
     * 非负整数
     */
    String NON_NEGATIVE_INTEGER = "^\\d+$";
    /**
     * 负整数
     */
    String NEGATIVE_INTEGER = "^-[1-9]\\d*$";
    /**
     * 非正整数
     */
    String NON_POSITIVE_INTEGER = "^((-\\d+)|(0+))$";
    /**
     * 最多带2位小数的正数
     */
    String POSITIVE_NUMBER_TWO_DECIMALS_MOST = "^([1-9][0-9]*)+(.[0-9]{1,2})?$";
    /**
     * 实数
     */
    String REAL_NUMBER = "^(\\-|\\+)?\\d+(\\.\\d+)?$";
    /**
     * 最多带2位小数的实数
     */
    String REAL_NUMBER_2_DECIMALS_OPTIONAL = "^(\\-)?\\d+(\\.\\d{1,2})?$";
    /**
     * 非负实数,含有小数则小数位必须2位
     */
    String NON_NEGATIVE_REAL_NUMBER_2_DECIMALS_ONLY = "^[0-9]+(.[0-9]{2})?$";

    /**
     * 非负实数,含有小数则1至3位的范围
     */
    String NON_NEGATIVE_REAL_NUMBER_3_DECIMALS_OPTIONAL = "^[0-9]+(.[0-9]{1,3})?$";

    /**
     * 0或正浮点数
     */
    String NON_NEGATIVE_FLOAT = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0$";

    /**
     * 0或负浮点数
     */
    String NON_POSITIVE_FLOAT = "^(-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*))|0?\\.0+|0$";
    /**
     * 正浮点数
     */
    String POSITIVE_FLOAT = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";
    /**
     * 负浮点数
     */
    String NEGATIVE_FLOAT = "^-([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*)$";

    /**
     * 浮点数
     */
    String FLOAT = "^-?([1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*|0?\\.0+|0)$";

    /**
     * 汉字,中间不包含其他字符
     */
    String CHINESE = "^[\\u4e00-\\u9fa5]{0,}$";

    /**
     * 英文和数字,中间不包含其他字符
     */
    String ENGLISH_AND_NUMBER = "^[A-Za-z0-9]+$";

    /**
     * 英文字母
     */
    String ENGLISH = "^[A-Za-z]+$";

    /**
     * 大写英文字母
     */
    String ENGLISH_UPPERCASE = "^[A-Z]+$";

    /**
     * 小写英文字母
     */
    String ENGLISH_LOWERCASE = "^[a-z]+$";

    /**
     * 数字,英文,下划线组成的字符串
     */
    String ENGLISH_NUMBER_UNDERLINE = "^\\w+$";

    /**
     * 数字,英文,中文,下划线组成的字符串
     */
    String EN_CN_NUM_UNDERLINE = "^[\\u4E00-\\u9FA5A-Za-z0-9_]+$";

    /**
     * 数字,英文,中文,不包括下划线组成的字符串
     */
    String EN_CN_NUM = "^[\\u4E00-\\u9FA5A-Za-z0-9]+$";

    /**
     * 匹配邮箱
     */
    String EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 匹配域名
     */
    String DOMAIN = "^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$";

    /**
     * 匹配URL,必须http(s),ftp(s)开头
     */
    String HTTP_FTP_URL = "^((ht|f)tps?):\\/\\/[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-\\.,@?^=%&:\\/~\\+#]*[\\w\\-\\@?^=%&\\/~\\+#])?$";

    /**
     * 国内手机号
     */
    String CHINESE_MOBILE_NO = "^[1]([3-9])[0-9]{9}$";

    /**
     * 国内电话号码
     */
    String CHINESE_PHONE_NO = "[0-9-()（）]{7,18}";

    /**
     * 身份证号
     */
    String ID_CARD = "\\d{17}[\\d|x]|\\d{15}";

    /**
     * 帐号是否合法,字母开头，允许5-16字节，允许字母数字下划线
     */
    String ACCOUNT_CHECK = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";

    /**
     * yyyy-mm-dd日期格式校验
     */
    String YYYY_MM_DD = "^(?:(?!0000)[0-9]{4}-(?:(?:0[1-9]|1[0-2])-(?:0[1-9]|1[0-9]|2[0-8])|(?:0[13-9]|1[0-2])-(?:29|30)|(?:0[13578]|1[02])-31)|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)-02-29)$";

    /**
     * 月份数字范围
     */
    String MONTH_NUMBER = "^(0?[1-9]|1[0-2])$";

    /**
     * 一个月中天数的数字范围
     */
    String DAY_RANGE = "^((0?[1-9])|((1|2)[0-9])|30|31)$";

    /**
     * 国内邮政编码
     */
    String ZIP_CODE = "^[0-9]{6}$";

    /**
     * IPv4正则表达式
     */
    String IP_V4 = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

    /**
     * IPv6正则表达式
     */
    String IP_V6 = "^([\\da-fA-F]{1,4}:){7}[\\da-fA-F]{1,4}$";

    /**
     * 限定n位的数字
     * @param n 位数
     * @return n位长度的数字正则
     */
    static String limitNumber(int n){
        if(n<=0){
            throw new RuntimeException("n必须大于0");
        }
        return "^\\d{"+n+"}$";
    }

    /**
     * 至少n位的数字,n<=0时,结果与n=1一致
     * @param n 位数
     * @return 至少n位长度的数字正则
     */
    static String leastNumber(int n){
        if(n<=0){
            n = 1;
        }
        return "^\\d{"+n+",}$";
    }

    /**
     * m至n位范围的整数
     * @param m 至少m位
     * @param n 至多n位
     * @return m-n范围数字的正则
     */
    static String rangedNumber(int m,int n){
        if(m<=0){
            throw new RuntimeException("m必须大于0");
        }
        if(n<=0){
            throw new RuntimeException("n必须大于0");
        }
        if(m>n){
            throw new RuntimeException("m必须小于等于n");
        }
        return "^\\d{"+m+","+n+"}$";
    }

    /**
     * 限定n位整数
     * @param n 位数
     * @return n位长度整数正则表达式
     */
    static String limitInt(int n){
        if(n<=0){
            throw new RuntimeException("n必须大于0");
        }else if(n==1){
            return "^(0|[1-9]|-[1-9])$";
        }else{
            n--;
            return "^(-?[1-9]\\d{"+n+"})$";
        }
    }

    /**
     * 0或至少n位的整数,n<=0时,与n=1结果一致
     * @param n 位数
     * @return 最少n位的整数正则表达式
     */
    static String leastInt(int n){
        if(n<=0||n==1){
            return "^(0|-[1-9]\\d{0,}|[1-9]\\d{0,})$";
        }else{
            n--;
            return "^(0|-[1-9]\\d{"+n+",}|[1-9]\\d{"+n+",})$";
        }
    }

    /**
     * m至n位范围的整数
     * @param m 至少m位
     * @param n 至多n位
     * @return m-n范围的整数正则表达式
     */
    static String rangedInt(int m,int n){
        if(m>n){
            throw new RuntimeException("m必须小于等于n");
        }
        if(m<=0){
            throw new RuntimeException("m必须大于0");
        }else if(m==1&&n==1){
            return "^(0|[1-9]|-[1-9])$";
        }else{
            m--;
            n--;
            return "^(-?[1-9]\\d{"+m+","+n+"})$";
        }
    }

    /**
     * m至n位(至少4位)的强密码校验,至少包含数字,大小写字母
     * @param m 最少位密码
     * @param n 最多位密码
     * @return 强密码校验正则
     */
    static String strongPwdRegex(int m,int n){
        int least = 4;
        if(m<least){
            throw new RuntimeException("密码至少是4位,m>=4");
        }
        if(m>n){
            throw new RuntimeException("m必须小于等于n");
        }
        return "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{"+m+","+n+"}$";
    }

    /**
     * 校验字符串是否匹配正则
     * @param origin 待匹配的字符串
     * @param regex 正则
     * @return true匹配, false不匹配
     */
    static boolean isRegexMatch(String origin,@NotNull String regex){
        Pattern pat = Pattern.compile(regex);
        Matcher matcher = pat.matcher(origin);
        return matcher.matches();
    }

}
