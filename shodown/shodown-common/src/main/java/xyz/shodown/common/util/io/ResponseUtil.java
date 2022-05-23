package xyz.shodown.common.util.io;

import lombok.extern.slf4j.Slf4j;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.response.Result;
import xyz.shodown.common.util.json.JsonUtil;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @ClassName: ResponseUtil
 * @Description: 使用response输出JSON
 * @Author: wangxiang
 * @Date: 2021/10/25 10:35
 */
@Slf4j(topic = LogCategory.EXCEPTION)
public class ResponseUtil {

    /**
     * 直接返回字符串
     * @param response response
     * @param result 结果字符串
     */
    public static void out(HttpServletResponse response,String result){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            out.println(result);
            out.flush();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 使用response输出JSON
     *
     * @param response {@link ServletResponse}
     * @param result {@link Result}
     */
    public static void out(HttpServletResponse response, Result<?> result) {
        out(response,JsonUtil.objectToJson(result));
    }

    /**
     * 响应内容
     * @param response {@link HttpServletResponse}
     * @param data 响应数据
     * @param code 状态码
     */
    public static void out(HttpServletResponse response, String data, Integer code){
        out(response,Result.success(code,data));
    }

}
