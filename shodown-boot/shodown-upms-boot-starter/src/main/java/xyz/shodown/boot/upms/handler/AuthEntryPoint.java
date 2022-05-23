package xyz.shodown.boot.upms.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import xyz.shodown.boot.upms.annotation.IgnoreGeneralCrypto;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.response.Result;
import xyz.shodown.common.util.io.ResponseUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: AuthEntryPoint
 * @Description: 登录入口
 * @Author: wangxiang
 * @Date: 2021/9/6 10:23
 */
@Slf4j(topic = LogCategory.EXCEPTION)
@Component
public class AuthEntryPoint implements AuthenticationEntryPoint {

    @Override
    @IgnoreGeneralCrypto
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        Result<String> result;
        if(e!=null){
            log.error(e.getMessage(),e);
            result = Result.fail(HttpServletResponse.SC_UNAUTHORIZED,e.getMessage());
        }else{
            result = Result.fail(HttpServletResponse.SC_UNAUTHORIZED,"未登录,请先进行登录");
        }
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ResponseUtil.out(httpServletResponse,result);
    }
}
