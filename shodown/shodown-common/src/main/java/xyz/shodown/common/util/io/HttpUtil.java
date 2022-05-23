package xyz.shodown.common.util.io;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.servlet.ServletUtil;
import cn.hutool.http.ContentType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.shodown.common.consts.Symbols;
import xyz.shodown.common.util.json.JsonUtil;

import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: HttpUtil
 * @Description: http工具类
 * @Author: wangxiang
 * @Date: 2021/4/12 9:18
 */
public class HttpUtil extends cn.hutool.http.HttpUtil {

    /**
     * 获取get请求参数
     * @param request HttpServletRequest
     * @return 请求参数
     */
    public static String getRequestParams(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        Map<String,Object> json = new HashMap<>();
        for (String key : params.keySet()) {
            String[] values = params.get(key);
            if (values.length > 0) {
                json.put(key, values[0]);
            }
        }
        return JsonUtil.objectToJson(json);
    }

    /**
     * 结果写入ServletResponse
     * @param response ServletResponse
     * @param responseString 结果内容
     * @throws IOException IO异常
     */
    public static void writeResponse(ServletResponse response, String responseString)
            throws IOException {
        response.setCharacterEncoding(CharsetUtil.UTF_8);
        response.setContentType(ContentType.JSON.toString());
        PrintWriter out = response.getWriter();
        out.print(responseString);
        out.flush();
        out.close();
    }

    /**
     * 设定返回给客户端的Cookie
     * @param response {@link HttpServletResponse}
     * @param cookie {@link Cookie}
     */
    public static void addCookie(HttpServletResponse response, Cookie cookie){
        ServletUtil.addCookie(response,cookie);
    }

    /**
     * 设定返回给客户端的Cookie
     * @param response {@link HttpServletResponse}
     * @param name cookie名
     * @param value cookie值
     */
    public static void addCookie(HttpServletResponse response,String name,String value){
        ServletUtil.addCookie(response, name,value);
    }

    /**
     * 设定返回给客户端的Cookie
     * @param response        {@link HttpServletResponse}
     * @param name            cookie名
     * @param value           cookie值
     * @param maxAgeInSeconds -1: 关闭浏览器清除Cookie. 0: 立即清除Cookie. &gt;0 : Cookie存在的秒数.
     * @param path            Cookie的有效路径
     * @param domain          the domain name within which this cookie is visible; form is according to RFC 2109
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds, String path, String domain){
        ServletUtil.addCookie(response,name,value,maxAgeInSeconds,path,domain);
    }

    /**
     * 设定返回给客户端的Cookie
     * @param response        {@link HttpServletResponse}
     * @param name            cookie名
     * @param value           cookie值
     * @param maxAgeInSeconds -1: 关闭浏览器清除Cookie. 0: 立即清除Cookie. &gt;0 : Cookie存在的秒数.
     * @param domain          the domain name within which this cookie is visible; form is according to RFC 2109
     */
    public static void addCookie(HttpServletResponse response,String name, String value, int maxAgeInSeconds,String domain){
        ServletUtil.addCookie(response,name,value,maxAgeInSeconds, Symbols.SLASH,domain);
    }

    /**
     * 设定返回给客户端的Cookie
     * @param response {@link HttpServletResponse}
     * @param name cookie名
     * @param value cookie值
     * @param maxAgeInSeconds -1: 关闭浏览器清除Cookie. 0: 立即清除Cookie. &gt;0 : Cookie存在的秒数.
     */
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAgeInSeconds){
        ServletUtil.addCookie(response,name,value,maxAgeInSeconds);
    }

    /**
     * 获取请求中的cookie
     * @param request {@link HttpServletRequest}
     * @param name cookie名
     * @return
     */
    public static Cookie getCookie(HttpServletRequest request,String name){
        return ServletUtil.getCookie(request,name);
    }

    /**
     * 获取请求中cookie的内容
     * @param request {@link HttpServletRequest}
     * @param name cookie名
     * @return
     */
    public static String getCookieContent(HttpServletRequest request,String name){
        Cookie cookie = getCookie(request,name);
        if(cookie!=null){
           return cookie.getValue();
        }else {
            return null;
        }
    }

    /**
     * 删除请求中的cookie
     * @param name cookie名
     * @param domain the domain name within which this cookie is visible; form is according to RFC 2109
     * @param request {@link HttpServletRequest}
     * @param response {@link HttpServletResponse}
     */
    public static void removeCookie(String name, String domain, HttpServletRequest request, HttpServletResponse response){
        String cookieVal = getCookieContent(request,name);
        if(cookieVal!=null){
            addCookie(response,name,null,0,Symbols.SLASH,domain);
        }
    }

    /**
     * 获取当前请求的HttpServletRequest
     * @return HttpServletRequest
     */
    public static HttpServletRequest getHttpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 获取域名
     * @return 域名
     */
    public static String getDomain(){
        HttpServletRequest request = getHttpServletRequest();
        StringBuffer url = request.getRequestURL();
        return url.delete(url.length() - request.getRequestURI().length(), url.length()).toString();
    }

}
