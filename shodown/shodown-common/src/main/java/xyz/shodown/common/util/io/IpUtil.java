package xyz.shodown.common.util.io;


import xyz.shodown.common.consts.HttpConst;
import xyz.shodown.common.consts.Symbols;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @ClassName: IpUtil
 * @Description: IP工具类
 * @Author: wangxiang
 * @Date: 2021/2/4 14:02
 */
public class IpUtil {

    private final static int PORT_80 = 80;

    private final static int PORT_443 = 443;

    /**
     * 获取ip地址
     * @param request Servlet Http请求对象
     * @return IP地址
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader(HttpConst.Header.X_FORWARDED_FOR);
        if (ip == null || ip.length() == 0 || HttpConst.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HttpConst.Header.PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || HttpConst.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(HttpConst.Header.WL_PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || HttpConst.UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(Symbols.COMMA)) {
            ip = ip.split(Symbols.COMMA)[0];
        }
        if (HttpConst.LOCALHOST.equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    /**
     * 获取请求basePath
     * @param request HttpServletRequest
     * @return basePath
     */
    public static String getBasePath(HttpServletRequest request) {
        StringBuilder basePath = new StringBuilder();
        String scheme = request.getScheme();
        String domain = request.getServerName();
        int port = request.getServerPort();
        basePath.append(scheme);
        basePath.append(Symbols.COLON).append(Symbols.SLASH).append(Symbols.SLASH);
        basePath.append(domain);
        if (HttpConst.HTTP.equalsIgnoreCase(scheme) && PORT_80 != port) {
            basePath.append(Symbols.COLON).append(port);
        } else if (HttpConst.HTTPS.equalsIgnoreCase(scheme) && port != PORT_443) {
            basePath.append(Symbols.COLON).append(port);
        }
        return basePath.toString();
    }

    /**
     * 移除request指定参数
     * @param request HttpServletRequest
     * @param paramName 参数名称
     * @return 剩余参数
     */
    public String removeParam(HttpServletRequest request, String paramName) {
        String queryString = "";
        Enumeration keys = request.getParameterNames();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.equals(paramName)) {
                continue;
            }
            if ("".equals(queryString)) {
                queryString = key + Symbols.EQUAL + request.getParameter(key);
            } else {
                queryString += Symbols.AND + key + Symbols.EQUAL + request.getParameter(key);
            }
        }
        return queryString;
    }

}
