package org.example.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.AsyncLoadBalancerAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.client.loadbalancer.RestTemplateCustomizer;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author bojiangzhou
 */
@Configuration
@ConditionalOnClass(RestTemplate.class)
@AutoConfigureAfter(name = "org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration")
@AutoConfigureBefore({LoadBalancerAutoConfiguration.class, AsyncLoadBalancerAutoConfiguration.class})
@ConditionalOnProperty(name = "ribbon.httpclient.restTemplate.enabled", matchIfMissing = true)
public class ApacheClientHttpRequestFactoryConfiguration {

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnClass(HttpClient.class)
    @ConditionalOnProperty(name = "ribbon.httpclient.enabled", matchIfMissing = true)
    static class ClientHttpRequestFactoryConfiguration {

        @Autowired
        private SpringClientFactory springClientFactory;

        @Bean
        @ConditionalOnMissingBean
        public RestTemplateCustomizer restTemplateCustomizer(
                final ApacheClientHttpRequestFactory apacheClientHttpRequestFactory) {
            return restTemplate -> {
                // 设置 RequestFactory
                restTemplate.setRequestFactory(apacheClientHttpRequestFactory);

                // 添加移除 Content-Length 的拦截器，否则会报错
                ClientHttpRequestInterceptor removeHeaderLenInterceptor = (request, bytes, execution) -> {
                    request.getHeaders().remove(HTTP.CONTENT_LEN);
                    return execution.execute(request, bytes);
                };

                List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>(restTemplate.getInterceptors());
                // 添加移除Content-Length请求头的Interceptor
                interceptors.add(removeHeaderLenInterceptor);
                restTemplate.setInterceptors(interceptors);
            };
        }

        /**
         * 非重试
         * @return
         */
        @Bean
        @ConditionalOnMissingClass("org.springframework.retry.support.RetryTemplate")
        public ApacheClientHttpRequestFactory apacheClientHttpRequestFactory() {
            return new ApacheClientHttpRequestFactory(springClientFactory, false);
        }

        /**
         * 可重试
         * @return
         */
        @Bean
        @ConditionalOnClass(name = "org.springframework.retry.support.RetryTemplate")
        public ApacheClientHttpRequestFactory retryableApacheClientHttpRequestFactory() {
            return new ApacheClientHttpRequestFactory(springClientFactory, true);
        }


    }
}