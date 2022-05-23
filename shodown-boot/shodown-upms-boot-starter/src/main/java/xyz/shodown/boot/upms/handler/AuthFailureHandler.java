package xyz.shodown.boot.upms.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
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
 * @ClassName: AuthFailureHandler
 * @Description: 登录失败处理
 * @Author: wangxiang
 * @Date: 2021/9/6 10:21
 */
@Slf4j(topic = LogCategory.EXCEPTION)
@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {

    @Override
    @IgnoreGeneralCrypto
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        log.error(e.getMessage(),e);
        Result<?> result = Result.fail(HttpServletResponse.SC_FORBIDDEN,e.getMessage());
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ResponseUtil.out(httpServletResponse,result);
    }
}
