package xyz.shodown.flow.enums;

import xyz.shodown.common.consts.Symbols;

/**
 * @ClassName: ShodownFlowError
 * @Description:
 * @Author: wangxiang
 * @Date: 2021/6/1 14:58
 */
public enum ShodownFlowError {

    /**
     * Direction的eval和nav只能二选一
     */
    NEITHER_EVAL_NOR_NAV("1001","Direction的eval和nav只能二选一"),

    /**
     * 只能设置一个起始direction,请检查@Direction注解的类上多余的entrance=true
     */
    ALREADY_SET_ENTRANCE_DIRECTION("1002","只能设置一个起始direction,请检查@Direction注解的类上多余的entrance=true"),

    /**
     * 没有实例对象
     */
    NULL_INSTANCE("1003","没有该实例对象"),

    /**
     * 实例对象不是所指定的类型
     */
    NOT_EXPECTED_CLASS("1004","实例对象不是所指定的类型")
    ;

    private String code;

    private String errMsg;

    ShodownFlowError(String code, String errMsg) {
        this.code = code;
        this.errMsg = errMsg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }


    @Override
    public String toString() {
        return Symbols.LEFT_SQUARE_BRACKET + getCode() + Symbols.RIGHT_SQUARE_BRACKET + getErrMsg();
    }
}
