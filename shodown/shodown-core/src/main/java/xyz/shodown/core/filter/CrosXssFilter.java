package xyz.shodown.core.filter;

import cn.hutool.core.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import xyz.shodown.common.consts.HttpConst;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.request.XssBodyRequestWrapper;
import xyz.shodown.common.request.XssHttpServletRequestWrapper;
import xyz.shodown.common.util.basic.MapUtil;
import xyz.shodown.common.util.io.HttpUtil;
import xyz.shodown.common.util.io.XssUtil;
import xyz.shodown.common.util.json.JsonUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebFilter
@Slf4j(topic = LogCategory.PLATFORM)
@Order(-1)
public class CrosXssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(CharsetUtil.CHARSET_UTF_8.name());
        HttpServletRequest httpServletRequest=(HttpServletRequest)request;
        resetCookies(httpServletRequest,(HttpServletResponse)response);
        //跨域设置
        if(response instanceof HttpServletResponse){
            HttpServletResponse httpServletResponse=(HttpServletResponse)response;
            //通过在响应 header 中设置 ‘*’ 来允许来自所有域的跨域请求访问。
            httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
            //通过对 Credentials 参数的设置，就可以保持跨域 Ajax 时的 Cookie
            //设置了Allow-Credentials，Allow-Origin就不能为*,需要指明具体的url域
            //httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            //请求方式
            httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
            //（预检请求）的返回结果（即 Access-Control-Allow-Methods 和Access-Control-Allow-Headers 提供的信息） 可以被缓存多久
            httpServletResponse.setHeader("Access-Control-Max-Age", "86400");
            //首部字段用于预检请求的响应。其指明了实际请求中允许携带的首部字段
            httpServletResponse.setHeader("Access-Control-Allow-Headers", "*");
        }
        //sql,xss过滤
        String reqMethod = httpServletRequest.getMethod();
        if(HttpConst.RequestMethod.GET.equals(reqMethod)){
            log.debug("CrosXssFilter-orignal url:{},ParameterMap:{}",httpServletRequest.getRequestURI(), JsonUtil.objectToJson(httpServletRequest.getParameterMap()));
            XssHttpServletRequestWrapper xssHttpServletRequestWrapper=new XssHttpServletRequestWrapper(
                    httpServletRequest);
            chain.doFilter(xssHttpServletRequestWrapper, response);
            log.debug("CrosXssFilter-doFilter url:{},ParameterMap:{}",xssHttpServletRequestWrapper.getRequestURI(), JsonUtil.objectToJson(xssHttpServletRequestWrapper.getParameterMap()));
        }else {
            log.debug("CrosXssFilter-start url:{}",httpServletRequest.getRequestURI());
            XssBodyRequestWrapper xssBodyRequestWrapper = new XssBodyRequestWrapper(httpServletRequest);
            chain.doFilter(xssBodyRequestWrapper, response);
            log.debug("CrosXssFilter-end url:{}",xssBodyRequestWrapper.getRequestURI());
        }
    }

    private Cookie[] resetCookies(HttpServletRequest request,
                                  HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        Map<String, String> map = new HashMap<>();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                String value = cookie.getValue();
                if (value != null) {
                    if(XssUtil.cleanXss(value)){
                        map.put(cookie.getName(), cookie.getDomain());
                    }
                }
            }
            if (MapUtil.isNotEmpty(map)) {
                for (Map.Entry<String, String> cookie : map.entrySet()) {
                    HttpUtil.removeCookie(cookie.getKey(),cookie
                            .getValue(),request,response);
                }
            }
        }
        return cookies;
    }

}
