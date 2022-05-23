package xyz.shodown.boot.upms.filter;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import xyz.shodown.boot.upms.handler.AuthFailureHandler;
import xyz.shodown.boot.upms.handler.AuthSuccessHandler;
import xyz.shodown.boot.upms.keychain.LoginKeyChain;
import xyz.shodown.boot.upms.model.LoginDto;
import xyz.shodown.boot.upms.support.AuthManager;
import xyz.shodown.boot.upms.util.ShodownUpmsUtil;
import xyz.shodown.common.consts.HttpConst;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.request.RequestWrapper;
import xyz.shodown.common.util.json.JsonUtil;
import xyz.shodown.crypto.enums.Algorithm;
import xyz.shodown.crypto.helper.CryptoHelper;
import xyz.shodown.crypto.properties.CryptoProperties;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @ClassName: AuthProcessingFilter
 * @Description: 用户登录认证过滤
 * @Author: wangxiang
 * @Date: 2021-10-21 11:23
 */
@Slf4j(topic = LogCategory.EXCEPTION)
@Component
public class AuthProcessingFilter extends AbstractAuthenticationProcessingFilter {

    @Resource
    private CryptoProperties cryptoProperties;

    /**
     * 认证管理器
     */
    @Resource
    private AuthManager authManager;

    /**
     * 成功后的处理
     */
    @Resource
    private AuthSuccessHandler authSuccessHandler;

    /**
     * 失败的处理
     */
    @Resource
    private AuthFailureHandler authFailureHandler;

    public AuthProcessingFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (request.getContentType() == null ||
                (!request.getContentType().contains(HttpConst.ContentType.APPLICATION_JSON)
                        && !request.getContentType().contains(HttpConst.ContentType.TEXT_PLAIN)
                        && !request.getContentType().contains(HttpConst.ContentType.MULTIPART_FORM_DATA))) {
            throw new AuthenticationServiceException("请求头类型不支持: " + request.getContentType());
        }
        UsernamePasswordAuthenticationToken authRequest = null;
        // 将前端传递的数据转换成jsonBean数据格式
        RequestWrapper wrappedRequest = new RequestWrapper(request);
        // 解析请求参数
        LoginDto user = resolveLoginDto(wrappedRequest);
        authRequest = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), null);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    public void afterPropertiesSet() {
        this.setAuthenticationManager(authManager);
        this.setAuthenticationSuccessHandler(authSuccessHandler);
        this.setAuthenticationFailureHandler(authFailureHandler);
    }

    /**
     * 解析出登陆请求内容
     *
     * @param request 登陆请求内容
     * @return 请求内容
     * @throws IOException io异常
     */
    private LoginDto resolveLoginDto(RequestWrapper request) throws Exception {
        if(!ShodownUpmsUtil.shouldCrypto(cryptoProperties)){
            return JsonUtil.jsonToBean(request.getRequestBody(), LoginDto.class);
        }
        // 采用非对称加密
        String sign = request.getHeader(HttpConst.Header.SIGN);
        CryptoHelper cryptoHelper = new CryptoHelper(Algorithm.RSA, LoginKeyChain.class, "登陆开始:", sign);
        String content = cryptoHelper.decrypt(request.getRequestBody());
        return JsonUtil.jsonToBean(content, LoginDto.class);
    }
}
