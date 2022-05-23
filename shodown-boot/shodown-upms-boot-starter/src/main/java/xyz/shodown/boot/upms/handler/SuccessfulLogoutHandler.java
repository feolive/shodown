package xyz.shodown.boot.upms.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import xyz.shodown.boot.upms.annotation.IgnoreGeneralCrypto;
import xyz.shodown.boot.upms.model.SecurityUser;
import xyz.shodown.boot.upms.model.UserBaseInfo;
import xyz.shodown.boot.upms.service.UserService;
import xyz.shodown.boot.upms.support.UserInfoProvider;
import xyz.shodown.common.response.Result;
import xyz.shodown.common.util.basic.UserInfoUtil;
import xyz.shodown.common.util.io.ResponseUtil;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: LogoutHandler
 * @Description: 完成登出操作的处理
 * @Author: wangxiang
 * @Date: 2021/9/6 10:57
 */
@Component
public class SuccessfulLogoutHandler implements LogoutSuccessHandler {

    @Resource
    private UserService userService;

    @Override
    @IgnoreGeneralCrypto
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Result<String> result = Result.success(303,"用户已退出登录");
        UserBaseInfo userBaseInfo = UserInfoUtil.getUserInfo(UserBaseInfo.class);
        if(userBaseInfo!=null){
            String token = userBaseInfo.getToken();
            userService.removeToken(token);
        }
        SecurityContextHolder.clearContext();
        ResponseUtil.out(httpServletResponse,result);
    }
}
