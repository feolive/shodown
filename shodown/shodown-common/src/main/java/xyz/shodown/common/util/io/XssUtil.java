package xyz.shodown.common.util.io;

import lombok.extern.slf4j.Slf4j;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.consts.Symbols;
import xyz.shodown.common.util.basic.StringUtil;

import java.util.*;
import java.util.regex.Pattern;

/**
 * @ClassName: XssUtil
 * @Description: XSS工具类
 * @Author: wangxiang
 * @Date: 2021/5/24 15:38
 */
@Slf4j(topic = LogCategory.PLATFORM)
public class XssUtil {


    /**
     * 过滤不允许的sql关键词
     * @param paramValue 入参
     * @return 是否包含非法内容 true是 false否
     */
    public static boolean cleanSqlKeyWords(String paramValue) {
        if(StringUtil.isEmpty(paramValue)){
            return false;
        }
        String value = paramValue;
        for (String keyword : SqlIllegalWords.NOT_ALLOWED_KEYWORDS) {
            boolean flag = paramValue.length() > keyword.length() + 2
                    && (StringUtil.containsIgnoreCase(paramValue," "+keyword)
                    ||StringUtil.containsIgnoreCase(paramValue,keyword+" ")
                    ||StringUtil.containsIgnoreCase(paramValue," "+keyword+" "));
            if (flag) {
                paramValue = StringUtil.replaceIgnoreCase(paramValue,keyword,SqlIllegalWords.INVALID);
                log.error("sql已过滤，因为参数中包含不允许sql的关键词(" + keyword
                        + ")"+";参数："+value+";过滤后的参数："+paramValue);
                return true;
            }
        }
        return false;
    }

    /**
     * 清除非法内容
     * @param value 原始输入
     * @return 是否包含非法内容 true是 false否
     */
    public static boolean cleanXss(String value){

        if(StringUtil.isNotEmpty(value)){
            String origin = value;
            StringBuilder sb = new StringBuilder();
            for (Pattern pattern : XssIllegalPattern.PATTERNS) {
                value = pattern.matcher(value).replaceAll(Symbols.MINUS);
            }
            if(!origin.equals(value)){
                log.error("xss已被过滤,请求包含不允许的非法内容("+sb.toString()+"),内容:"+origin+"过滤后的内容:"+value);
                return true;
            }
        }

        return false;
    }

    /**
     * 清除xss和sql非法内容
     * @param value 输入
     * @return 是否包含非法内容 true是 false否
     */
    public static boolean cleanXssAndSqlIllegals(String value){
        boolean res = false;
        res = XssUtil.cleanXss(value);
        if(!res){
            res = XssUtil.cleanSqlKeyWords(value);
        }
        return res;
    }

    private static class SqlIllegalWords{
        private final static String KEY = "and|exec|execute|insert|where|sleep|like|select|delete|update|count" +
                "|*|%|chr|mid|create|master|truncate|char|declare|;|" +
                "or|//|/|-|--|+";
        private final static Set<String> NOT_ALLOWED_KEYWORDS = new HashSet<String>(0);

        private final static String INVALID="INVALID_";

        static {
            String[] keyStr = KEY.split("\\|");
            NOT_ALLOWED_KEYWORDS.addAll(Arrays.asList(keyStr));
        }
    }

    private static class XssIllegalPattern{
        private final static Pattern SCRIPT_PATTERN = Pattern.compile("<script>(\\s*.*?)</script>",
                Pattern.CASE_INSENSITIVE);

        private final static Pattern SCRIPT_PATTERN_END = Pattern.compile("</script(\\s*.*?)>",
        Pattern.CASE_INSENSITIVE);

        private final static Pattern SCRIPT_PATTERN_START = Pattern.compile("<script(\\s*.*?)>",
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
                            | Pattern.DOTALL);

        private final static Pattern EVAL_PATTERN = Pattern.compile("eval\\((.*?)\\)",
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
                            | Pattern.DOTALL);

        private final static Pattern EXPRESSION_PATTERN = Pattern.compile("e­xpression\\((.*?)\\)",
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
                            | Pattern.DOTALL);

        private final static Pattern JAVASCRIPT_PATTERN = Pattern.compile("javascript:",
        Pattern.CASE_INSENSITIVE);

        private final static Pattern VB_SCRIPT_PATTERN = Pattern.compile("vbscript:",
        Pattern.CASE_INSENSITIVE);

        private final static Pattern ONLOAD_PATTERN = Pattern.compile("onload(.*?)=",
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
                            | Pattern.DOTALL);

        private final static Pattern OTHER_PATTERN = Pattern.compile("<+.*(oncontrolselect|oncopy|oncut|ondataavailable|ondatasetchanged|ondatasetcomplete|ondblclick|ondeactivate|ondrag|ondragend|ondragenter|ondragleave|ondragover|ondragstart|ondrop|onerror|onerroupdate|onfilterchange|onfinish|onfocus|onfocusin|onfocusout|onhelp|onkeydown|onkeypress|onkeyup|onlayoutcomplete|onload|onlosecapture|onmousedown|onmouseenter|onmouseleave|onmousemove|onmousout|onmouseover|onmouseup|onmousewheel|onmove|onmoveend|onmovestart|onabort|onactivate|onafterprint|onafterupdate|onbefore|onbeforeactivate|onbeforecopy|onbeforecut|onbeforedeactivate|onbeforeeditocus|onbeforepaste|onbeforeprint|onbeforeunload|onbeforeupdate|onblur|onbounce|oncellchange|onchange|onclick|oncontextmenu|onpaste|onpropertychange|onreadystatechange|onreset|onresize|onresizend|onresizestart|onrowenter|onrowexit|onrowsdelete|onrowsinserted|onscroll|onselect|onselectionchange|onselectstart|onstart|onstop|onsubmit|onunload)+.*=+",
        Pattern.CASE_INSENSITIVE | Pattern.MULTILINE
                            | Pattern.DOTALL);

        private final static List<Pattern> PATTERNS = new ArrayList<>();

        static {
            PATTERNS.add(SCRIPT_PATTERN);
            PATTERNS.add(SCRIPT_PATTERN_END);
            PATTERNS.add(SCRIPT_PATTERN_START);
            PATTERNS.add(EVAL_PATTERN);
            PATTERNS.add(EXPRESSION_PATTERN);
            PATTERNS.add(JAVASCRIPT_PATTERN);
            PATTERNS.add(VB_SCRIPT_PATTERN);
            PATTERNS.add(ONLOAD_PATTERN);
            PATTERNS.add(OTHER_PATTERN);
        }
    }

}
