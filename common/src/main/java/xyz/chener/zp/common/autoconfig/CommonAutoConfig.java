package xyz.chener.zp.common.autoconfig;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import xyz.chener.zp.common.config.*;
import xyz.chener.zp.common.config.antiShaking.aop.AntiShakingAop;
import xyz.chener.zp.common.config.ctx.ApplicationContextHolder;
import xyz.chener.zp.common.config.dynamicVerification.aop.DynamicVerAop;
import xyz.chener.zp.common.config.feign.FeignClientConfig;
import xyz.chener.zp.common.config.okhttpclient.HttpClientConfig;
import xyz.chener.zp.common.config.okhttpclient.HttpRequestInterfaceInject;
import xyz.chener.zp.common.config.opLog.aop.OpRecordAop;
import xyz.chener.zp.common.config.paramDecryption.ParamDecryAutoConfig;
import xyz.chener.zp.common.config.security.AccessDeniedProcess;
import xyz.chener.zp.common.config.security.AuthFilter;
import xyz.chener.zp.common.config.security.EntryPointProcess;
import xyz.chener.zp.common.config.unifiedReturn.UnifiedErrorReturn;
import xyz.chener.zp.common.config.unifiedReturn.UnifiedReturnConfig;
import xyz.chener.zp.common.config.writeList.WriteListAutoConfig;
import xyz.chener.zp.common.config.writeList.WriteListRegister;
import xyz.chener.zp.common.utils.Jwt;
import xyz.chener.zp.common.utils.NacosUtils;

/**
 * @Author: chenzp
 * @Date: 2023/02/07/14:55
 * @Email: chen@chener.xyz
 */

@Configuration
@EnableConfigurationProperties(CommonConfig.class)
@Import({FeignClientConfig.class
        , UnifiedErrorReturn.class
        , WriteListAutoConfig.class, WriteListRegister.class
        , UnifiedReturnConfig.class
        , ParamDecryAutoConfig.class
        , HttpRequestInterfaceInject.class
        , HttpClientConfig.class})
public class CommonAutoConfig {

    private final CommonConfig commonConfig;


    public CommonAutoConfig(CommonConfig commonConfig) {
        this.commonConfig = commonConfig;
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicVerAop dynamicVerAop() {
        return new DynamicVerAop(commonConfig);
    }

    @Bean
    @ConditionalOnMissingBean
    public Jwt jwt() {
        return new Jwt(commonConfig);
    }

    @Bean
    public AuthFilter authFilter() {
        return new AuthFilter(commonConfig);
    }

    @Bean
    public AccessDeniedProcess accessDeniedProcess() {
        return new AccessDeniedProcess();
    }

    @Bean
    public EntryPointProcess entryPointProcess() {
        return new EntryPointProcess();
    }


    @Bean
    public ApplicationContextHolder applicationContextHolder() {
        return new ApplicationContextHolder();
    }

    @Bean
    public NacosUtils nacosUtils() {
        return new NacosUtils();
    }

    @Bean
    @ConditionalOnMissingBean
    public OpRecordAop opRecordAop() {
        return new OpRecordAop();
    }

    @Bean
    @ConditionalOnMissingBean
    public AntiShakingAop antiShakingAop() {
        return new AntiShakingAop();
    }

}
