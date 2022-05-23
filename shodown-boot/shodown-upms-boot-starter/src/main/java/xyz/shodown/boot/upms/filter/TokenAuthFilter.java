package xyz.shodown.boot.upms.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import xyz.shodown.boot.upms.config.AdditionalProperties;
import xyz.shodown.boot.upms.entity.ShodownUser;
import xyz.shodown.boot.upms.handler.AuthEntryPoint;
import xyz.shodown.boot.upms.model.SecurityUser;
import xyz.shodown.boot.upms.service.UserService;
import xyz.shodown.boot.upms.util.ShodownUpmsUtil;
import xyz.shodown.common.consts.HttpConst;
import xyz.shodown.common.consts.LogCategory;
import xyz.shodown.common.request.RequestWrapper;
import xyz.shodown.common.response.ResponseWrapper;
import xyz.shodown.common.response.Result;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.common.util.io.ResponseUtil;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @ClassName: TokenAuthFilter
 * @Description: 验证token过滤器
 * @Author: wangxiang
 * @Date: 2021-10-15 14:15
 */
@Slf4j(topic = LogCategory.BUSINESS)
@Component
public class TokenAuthFilter extends OncePerRequestFilter {

    private final ThreadLocal<String> urlLocal = new ThreadLocal<>();

    @Resource
    private AuthEntryPoint authEntryPoint;

    @Resource
    private UserService userService;

    @Resource
    private AdditionalProperties additionalProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(ShodownUpmsUtil.shouldIgnoreUrl(additionalProperties,request.getRequestURI())){
            filterChain.doFilter(request,response);
            return;
        }
        boolean b1 = request.getContentType() == null && request.getContentLength() > 0;
        boolean b2 = request.getContentType() != null
                &&!request.getContentType().contains(HttpConst.ContentType.APPLICATION_JSON)
                &&!request.getContentType().contains(HttpConst.ContentType.TEXT_PLAIN)
                &&!request.getContentType().contains(HttpConst.ContentType.MULTIPART_FORM_DATA);
        if (b1||b2) {
            filterChain.doFilter(request, response);
            return;
        }
        RequestWrapper wrappedRequest = new RequestWrapper(request);
        ResponseWrapper wrappedResponse = new ResponseWrapper(response);
        StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            // 记录请求的消息体
            logReq(wrappedRequest);
            // 获取token
            String token = request.getHeader(HttpConst.Header.AUTH_TOKEN);
            log.debug("检查令牌:{}", token);
            if (StringUtil.isNotBlank(token)) {
                // 检查token
                SecurityUser securityUser = userService.getUserByToken(token);
                if (securityUser == null) {
                    log.info("TOKEN已过期，请重新登陆");
                    ResponseUtil.out(response,Result.fail(307,"TOKEN已过期，请重新登陆"));
                    return;
                }
                ShodownUser shodownUser = securityUser.getCurrentUserInfo();
                shodownUser.setPassword(null);
                shodownUser.setSalt(null);
                securityUser.setCurrentUserInfo(shodownUser);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(securityUser, securityUser.getPassword(), securityUser.getAuthorities());
                // 全局注入角色权限信息和登录用户基本信息
                SecurityContextHolder.getContext().setAuthentication(authentication);

            }
            filterChain.doFilter(wrappedRequest, response);
        }  catch (AuthenticationException e) {
            SecurityContextHolder.clearContext();
            log.error("令牌认证错误: "+e.getMessage(),e);
            this.authEntryPoint.commence(request, response, e);
        } catch (Exception ex){
            SecurityContextHolder.clearContext();
            logger.error(ex.getMessage(),ex);
            ResponseUtil.out(response, Result.fail(ex.getMessage()));
        }finally {
            stopWatch.stop();
            long usedTimes = stopWatch.getTotalTimeMillis();
            // 记录响应的消息体
            logResp(wrappedResponse, usedTimes);
        }
    }

    /**
     * 请求日志打印
     *
     * @param request 请求体
     */
    private void logReq(RequestWrapper request) throws IOException {
        if (request != null) {
            String bodyJson = request.getRequestBody();
            String url = request.getRequestURI().replace("//", "/");
            urlLocal.set(url);
            log.info("`{}` 接收到的参数: {}", url, bodyJson);
        }
    }

    /**
     * 响应日志打印
     * @param response 响应体
     * @param time     响应时间
     */
    private void logResp(ResponseWrapper response, long time) throws IOException {
        if (response != null) {
            byte[] buf = response.getResponseData();
            if (buf.length > 0) {
                String payload;
                try {
                    payload = new String(buf, 0, buf.length, response.getCharacterEncoding());
                } catch (UnsupportedEncodingException ex) {
                    payload = "[unknown]";
                }
                String url = urlLocal.get();
                log.info("`{}`  耗时:{}ms  返回的参数: {}", url, time, payload);
                urlLocal.remove();
            }
        }
    }

}
