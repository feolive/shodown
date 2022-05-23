package xyz.shodown.boot.upms.config;


import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CharacterEncodingFilter;
import xyz.shodown.boot.upms.filter.AuthProcessingFilter;
import xyz.shodown.boot.upms.filter.TokenAuthFilter;
import xyz.shodown.boot.upms.handler.AuthEntryPoint;
import xyz.shodown.boot.upms.handler.NoAccessHandler;
import xyz.shodown.boot.upms.support.DynamicAccessDecisionManager;
import xyz.shodown.boot.upms.support.DynamicSecurityMetadataSource;
import xyz.shodown.common.consts.Charsets;
import xyz.shodown.common.util.basic.ListUtil;

import javax.annotation.Resource;
import javax.servlet.Filter;

/**
 * @ClassName: WebSecurityConfig
 * @Description: 登陆配置
 * @Author: wangxiang
 * @Date: 2021/9/5 20:19
 */
@Configuration
@EnableOAuth2Client
@EnableGlobalMethodSecurity(prePostEnabled = true,securedEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AdditionalProperties additionalProperties;

    /**
     * 未登录时处理
     */
    @Resource
    private AuthEntryPoint authEntryPoint;

    /**
     * 无权访问处理
     */
    @Resource
    private NoAccessHandler noAccessHandler;

    @Resource
    private AuthProcessingFilter authProcessingFilter;

    @Resource
    private TokenAuthFilter tokenAuthFilter;

    @Resource
    private DynamicSecurityMetadataSource dynamicSecurityMetadataSource;

    @Resource
    private DynamicAccessDecisionManager dynamicAccessDecisionManager;

    @Resource
    private OAuth2ClientContext oAuth2ClientContext;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.antMatcher("/**").authorizeRequests();

        CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding(Charsets.UTF8.name());
        characterEncodingFilter.setForceEncoding(true);

        // 禁用CSRF
        http.csrf().disable();
        // 未登录认证异常
        http.exceptionHandling().authenticationEntryPoint(authEntryPoint);
        // 登录过后访问无权限的接口时自定义403响应内容
        http.exceptionHandling().accessDeniedHandler(noAccessHandler);
        // 自定义过滤器在登录时认证用户名、密码
        http.addFilterBefore(characterEncodingFilter,UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(authProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(tokenAuthFilter, BasicAuthenticationFilter.class);
        Filter oauth2ClientFilter = oauth2ClientFilter();
        if(oauth2ClientFilter!=null){
            http.addFilterBefore(oauth2ClientFilter,BasicAuthenticationFilter.class);
        }
        // 不创建会话 - 即通过前端传token到后台过滤器中验证是否存在访问权限
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // url权限认证处理
        registry.withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O o) {
                o.setSecurityMetadataSource(dynamicSecurityMetadataSource);
                o.setAccessDecisionManager(dynamicAccessDecisionManager);
                return o;
            }
        });

        // 允许匿名的url - 可理解为放行接口 - 除配置文件忽略url以外，其它所有请求都需经过认证和授权
        if(additionalProperties!=null&& !ListUtil.isEmpty(additionalProperties.getAccess().getIgnoreUrls())){
            for (String url : additionalProperties.getAccess().getIgnoreUrls()) {
                registry.antMatchers(url).permitAll();
            }
        }
        // OPTIONS(选项)：查找适用于一个特定网址资源的通讯选择。 在不需执行具体的涉及数据传输的动作情况下， 允许客户端来确定与资源相关的选项以及 / 或者要求， 或是一个服务器的性能
        registry.antMatchers(HttpMethod.OPTIONS, "/**").denyAll();
        // 登陆页面允许所有访问
        registry.antMatchers("/login").permitAll();
        // 其余所有请求都需要认证
        registry.anyRequest().authenticated();
        // 防止iframe 造成跨域
        registry.and().headers().frameOptions().disable();
    }

    private Filter oauth2ClientFilter(){
        if(additionalProperties.getOauth2()==null){
            return null;
        }
        // 配置oauth2授权服务器上注册的redirect_url,即认证完成后的回调接口
        OAuth2ClientAuthenticationProcessingFilter processingFilter = new OAuth2ClientAuthenticationProcessingFilter(additionalProperties.getOauth2().getRedirectUrl());
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(authCodeResourceDetails(), oAuth2ClientContext);
        processingFilter.setRestTemplate(restTemplate);
        UserInfoTokenServices tokenServices = new UserInfoTokenServices(resServerProperties().getUserInfoUri(),
                authCodeResourceDetails().getClientId());
        // 可配置用户授权后的回调业务，比如将用户的信息保存在服务中
        //tokenServices.setPrincipalExtractor();
        tokenServices.setRestTemplate(restTemplate);
        processingFilter.setTokenServices(tokenServices);
        return processingFilter;
    }

    @Bean
    @ConfigurationProperties("shodown.upms.oauth2.resource")
    public ResourceServerProperties resServerProperties(){
        return new ResourceServerProperties();
    }

    @Bean
    @ConfigurationProperties("shodown.upms.oauth2.client")
    public AuthorizationCodeResourceDetails authCodeResourceDetails(){
        return new AuthorizationCodeResourceDetails();
    }

}
