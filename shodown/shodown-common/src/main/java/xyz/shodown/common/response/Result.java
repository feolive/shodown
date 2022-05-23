package xyz.shodown.common.response;

import lombok.Data;
import xyz.shodown.common.enums.ResponseEnum;

import java.io.Serializable;

/**
 * @ClassName: Result
 * @Description: 接口返回结果
 * @Author: wangxiang
 * @Date: 2021/3/9 14:35
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功标识，true成功，false失败
     */
    private boolean success;
    /**
     * 返回码
     */
    private Serializable code;
    /**
     * 返回消息
     */
    private String msg;
    /**
     * 返回数据
     */
    private T data;

    public Result(){
    }

    public Result(Serializable code,boolean success,String msg,T data){
        this.code = code;
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功,并返回数据
     * @param t 数据内容
     * @return 处理成功结果
     */
    public static<T> Result<T> success(T t){
        Result<T> res = new Result<>();
        res.setSuccess(true);
        res.setCode(ResponseEnum.SUCCESS.getCode());
        res.setMsg(ResponseEnum.SUCCESS.getMsg());
        res.setData(t);
        return res;
    }

    /**
     * 成功,自定义code
     * @param code 码值
     * @param t 返回数据
     * @param <T> 数据类型
     * @return 处理成功结果
     */
    public static <T> Result<T> success(Serializable code,T t){
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setCode(code);
        r.setData(t);
        return r;
    }

    /**
     * 成功,并返回数据与自定义message
     * @param msg 消息内容
     * @param t 数据内容
     * @param <T> 泛型
     * @return 处理成功结果
     */
    public static <T> Result<T> success(String msg, T t) {
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setCode(ResponseEnum.SUCCESS.getCode());
        r.setMsg(msg);
        r.setData(t);
        return r;
    }

    /**
     * 成功,并返回数据与自定义code和message
     * @param code 码值
     * @param msg 消息
     * @param t 数据
     * @param <T> 数据类型
     * @return 处理成功结果
     */
    public static <T> Result<T> success(Serializable code,String msg,T t){
        Result<T> r = new Result<>();
        r.setSuccess(true);
        r.setCode(code);
        r.setMsg(msg);
        r.setData(t);
        return r;
    }

    /**
     * 成功,无数据返回
     * @return 处理成功结果
     */
    public static Result<?> success(){
        Result<?> res = new Result<>();
        res.setSuccess(true);
        res.setCode(ResponseEnum.SUCCESS.getCode());
        res.setMsg(ResponseEnum.SUCCESS.getMsg());
        return res;
    }

    /**
     * 失败,返回码值与消息
     * @param code 码值
     * @param msg 消息内容
     * @return 处理失败结果
     */
    public static <T> Result<T> fail(Serializable code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setMsg(msg);
        r.setSuccess(false);
        return r;
    }

    /**
     * 失败,自定义失败消息
     * @param msg 消息
     * @return 处理失败结果
     */
    public static Result<?> fail(String msg) {
        return fail(ResponseEnum.NORMAL_EXCP.getCode(), msg);
    }

    /**
     * 失败,枚举内容返回
     * @param respEnum 枚举
     * @return 处理失败结果
     */
    public static Result<?> fail(ResponseEnum respEnum) {
        return fail(respEnum.getCode(), respEnum.getMsg());
    }

}
