package org.example.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author ：li zhen
 * @description:
 * @date ：2024/4/17 15:35
 */
@Configuration
@RestController
public class NacosController {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/echo/app-name")
    public String echoAppName() {
        /**
         * 3、通过对RestTemplate添加@LoadBalanced标记注解，使得LoadBalancerInterceptor介入使用LoadBalancerClient获取服务实例修改请求后向实际服务端发送请求
         * 3.1、由于gateway中存在ribbon依赖，所以使用的LoadBalancerClient实现是RibbonLoadBalancerClient
         * 3.2、通过自定义ApacheClientHttpRequest、ApacheClientHttpRequestFactory、ApacheClientHttpRequestFactoryConfiguration，改写RestTemplate(@LoadBalanced)的底层逻辑，底层改用CloseableHttpClient进行服务调用
         */
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://nacos-provider-config/echo/" + "hello", String.class);
        System.out.println(responseEntity.getBody());
        return responseEntity.getBody();
    }
}
