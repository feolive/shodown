package xyz.shodown.common.consts;

/**
 * @ClassName: Symbols
 * @Description: 符号常量;(英文键盘下对应的符号)
 * @Author: wangxiang
 * @Date: 2021/2/4 14:08
 */
public interface Symbols {

    /**
     * 逗号
     */
    String COMMA = ",";

    /**
     * 句号,
     * 注意在正则表达式中匹配除换行符 \n 之外的任何单字符,正则匹配".",请使用"\\."转义
     */
    String FULL_STOP = ".";

    /**
     * 句号,
     * 注意在正则表达式中匹配除换行符 \n 之外的任何单字符,正则匹配".",请使用"\\."转义
     */
    String DOT = ".";

    /**
     * 冒号
     */
    String COLON = ":";

    /**
     * 分号
     */
    String SEMICOLON = ";";

    /**
     * 问号,
     * 注意在正则表达式中匹配前面的子表达式零次或一次,正则匹配"?",请使用"\\?"转义
     */
    String QUESTION_MARK = "?";

    /**
     * 左括号,
     * 注意在正则表达式中标记一个子表达式的开始位置,正则匹配"(",请使用"\\("转义
     */
    String LEFT_BRACKET = "(";

    /**
     * 右括号,
     * 注意在正则表达式中标记一个子表达式的结束位置,正则匹配")",请使用"\\)"转义
     */
    String RIGHT_BRACKET = ")";

    /**
     * 左方括号,
     * 注意在正则表达式中标记一个中括号表达式的开始,正则匹配"[",请使用"\\["转义
     */
    String LEFT_SQUARE_BRACKET = "[";

    /**
     * 右方括号
     */
    String RIGHT_SQUARE_BRACKET = "]";

    /**
     * 左花括号,
     * 注意在正则表达式中标记限定符表达式的开始,正则匹配"{",请使用"\\{"转义
     */
    String LEFT_CURLY_BRACKET = "{";

    /**
     * 右花括号
     */
    String RIGHT_CURLY_BRACKET = "}";

    /**
     * 左尖括号
     */
    String LEFT_ANGLE_BRACKET = "<";

    /**
     * 左尖括号
     */
    String RIGHT_ANGLE_BRACKET = ">";

    /**
     * 减号
     */
    String MINUS = "-";

    /**
     * 加号
     * 注意此符号在正则表达式中匹配前面的子表达式一次或多次,正则匹配"+",请使用"\\+"转义
     */
    String PLUS = "+";

    /**
     * 星号,
     * 注意此符号在正则表达式中匹配前面的子表达式零次或多次,正则匹配"*",请使用"\\*"转义
     */
    String ASTERISK = "*";

    /**
     * 脱字符,
     * 注意此符号在正则表达式中匹配输入字符串的开始位置,正则匹配"^",请使用"\\^"转义
     */
    String CARET = "^";

    /**
     *  斜线
     */
    String SLASH = "/";

    /**
     * 反斜线
     */
    String BACKSLASH = "\\";

    /**
     * 竖线,
     * 注意此符号在正则表达式中表示两项之间的一个选择,正则匹配"|",请使用"\\|"转义
     */
    String VERTICAL_BAR = "|";

    /**
     * 双竖线
     */
    String PARALLEL = "||";

    /**
     * 下划线
     */
    String UNDERSCORE = "_";

    /**
     * 等号
     */
    String EQUAL = "=";

    /**
     * & 符号
     */
    String AND = "&";

    /**
     * @ 符号
     */
    String AT = "@";

    /**
     * 感叹号
     */
    String EXCLAMATION = "!";

    /**
     * 空字符串
     */
    String EMPTY_STR = "";

    /**
     * 空格
     */
    String SPACE = " ";

    /**
     * 美元符,
     * 注意此符号在正则表达式中表达字符串的结尾位置,正则匹配"$",请使用"\\$"转义
     */
    String DOLLAR = "$";

    /**
     * 井号
     */
    String HASHTAG = "#";

    /**
     * 波浪号
     */
    String SWUNG_DASH = "~";

}
