package xyz.shodown.boot.upms.handler;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import xyz.shodown.boot.upms.annotation.IgnoreGeneralCrypto;
import xyz.shodown.common.response.Result;
import xyz.shodown.common.util.io.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: NoAccessHandler
 * @Description: 无权访问处理
 * @Author: wangxiang
 * @Date: 2021/9/6 10:24
 */
@Component
public class NoAccessHandler implements AccessDeniedHandler {

    @Override
    @IgnoreGeneralCrypto
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        Result<String> result;
        if(e!=null){
            result = Result.success(HttpServletResponse.SC_FORBIDDEN,e.getMessage());
        }else {
            result = Result.success(HttpServletResponse.SC_FORBIDDEN,"没有权限操作");
        }
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ResponseUtil.out(httpServletResponse,result);
    }
}
