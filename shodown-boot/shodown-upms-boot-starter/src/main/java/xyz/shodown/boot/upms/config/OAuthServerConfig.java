package xyz.shodown.boot.upms.config;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import xyz.shodown.common.util.basic.ArrayUtil;
import xyz.shodown.common.util.basic.StringUtil;
import xyz.shodown.dynamicdb.context.DynamicDataSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName: AuthorizationServer
 * @Description: OAUTH2服务端配置
 * @Author: wangxiang
 * @Date: 2021/9/5 19:32
 */
@Configuration
@EnableAuthorizationServer
public class OAuthServerConfig extends AuthorizationServerConfigurerAdapter {

    /**
     * 使用的dynamicdb的动态数据源配置
     */
    private final static String DYNAMIC_DATASOURCE_NAME = "dynamicDataSource";

    @Resource
    private DataSource dataSource;

    @Resource
    private AdditionalProperties additionalProperties;

    /**
     * 授权模式专用对象，在 Security 配置中注入容器
     */
    @Resource
    private AuthenticationManager authManager;

    @Bean
    public ClientDetailsService jdbcClientDetailsService(){
        return new JdbcClientDetailsService(dataSource);
    }

    @Bean
    public TokenStore tokenStore(){
        // token 保存策略，指你生成的 Token 要往哪里存储
        return new JdbcTokenStore(dataSource);
    }

    /**
     * 令牌管理
     * @return AuthorizationServerTokenServices
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices tokenServices = new DefaultTokenServices();
        // token 保存策略
        tokenServices.setTokenStore(tokenStore());
        // 支持刷新模式，true时 refresh token 会同步刷新
        tokenServices.setSupportRefreshToken(false);
        // 客户端信息来源
        tokenServices.setClientDetailsService(jdbcClientDetailsService());
        // token 有效期自定义设置，默认 12 小时
        tokenServices.setAccessTokenValiditySeconds(60 * 60 * 12);
        // refresh token 有效期自定义设置，默认 30 天
        tokenServices.setRefreshTokenValiditySeconds(60 * 60 * 24 * 7);

        return tokenServices;
    }

    /**
     * 授权信息保存策略
     * @return {@link ApprovalStore}
     */
    @Bean
    public ApprovalStore approvalStore(){
        return new JdbcApprovalStore(dataSource);
    }

    /**
     * 授权码模式数据来源
     * @return {@link AuthorizationCodeServices}
     */
    @Bean
    public AuthorizationCodeServices authorizationCodeServices(){
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        // 用来配置令牌端点的安全约束,就是这个端点谁能访问，谁不能访问
        // tokenkey 这个 endpoint 当使用用 JwtToken 且使用非对称加密时，资源服务用于获取公钥而开放的，此时指 endpoint 完全公开
//        security.tokenKeyAccess("permitAll()");
        // checkToken 用于资源服务访问的令牌解析端点
//        security.checkTokenAccess("permitAll()");
        // 允许表单认证
        security.allowFormAuthenticationForClients();
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 用来配置客户端详情服务
        clients.withClientDetails(jdbcClientDetailsService());
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // 用来配置令牌（token）的访问端点和令牌服务,OAuth2 的主配置信息
        endpoints
                .approvalStore(approvalStore())
                .authenticationManager(authManager)
                .authorizationCodeServices(authorizationCodeServices())
                .tokenServices(tokenServices());
    }

    /**
     * 获取数据源配置
     * @return 明确的数据源
     */
    private DataSource determineDataSource(){
        DataSource ds = null;
        String[] beanNames = SpringUtil.getBeanNamesForType(DynamicDataSource.class);
        if(!ArrayUtil.isEmpty(beanNames)){
            List<String> beanNameList = Optional.ofNullable(ArrayUtil.toList(beanNames)).orElse(new ArrayList<>());
            if(beanNameList.contains(DYNAMIC_DATASOURCE_NAME)){
                DynamicDataSource dynamicDataSource = SpringUtil.getBean(DynamicDataSource.class);
//                String datasourceKey = additionalProperties.getAccess().getDatasource();
//                if(StringUtil.isEmpty(datasourceKey)){
//                    ds = dataSource;
//                }else {
//                    Map<Object,DataSource> map = dynamicDataSource.getResolvedDataSources();
//                    ds = map.get(datasourceKey);
//                }
            }else {
                ds = dataSource;
            }
        }else {
            ds = dataSource;
        }
        return ds;
    }
}
