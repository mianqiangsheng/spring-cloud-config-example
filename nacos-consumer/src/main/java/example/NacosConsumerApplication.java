package example;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Properties;

@SpringBootApplication
@EnableDiscoveryClient
public class NacosConsumerApplication {

    @RestController
    public class NacosController{

        @Autowired
        private LoadBalancerClient loadBalancerClient;
        @Autowired
        private RestTemplate restTemplate;
        @Autowired
        private NacosDiscoveryProperties nacosDiscoveryProperties;

        @Value("${spring.application.name}")
        private String appName;

        /**
         * we will use the most primitive way, that is, combining the LoadBalanceClient and RestTemolate explicitly to access the RESTful service.
         * You can also access the service by using RestTemplate and FeignClient with load balancing.
         * @return
         */
        @GetMapping("/echo/app-name")
        public String echoAppName() throws NacosException {
            //Access through the combination of LoadBalanceClient and RestTemplate
            /**
             * http://localhost:8882/echo/app-name
             * 1、使用LoadBalancerClient获取服务实例(本质上也借助于Nacos实现的服务发现机制，但是这种方式带有负载均衡功能)，并使用普通RestTemplate发送http请求到服务端
             */
            ServiceInstance serviceInstance = loadBalancerClient.choose("nacos-provider-config");
            String path = String.format("http://%s:%s/echo/%s",serviceInstance.getHost(),serviceInstance.getPort(),appName);
            System.out.println("request path:" +path);
            String result = restTemplate.getForObject(path, String.class);
//            restTemplate.getForObject(serviceInstance.getUri() + "/echo/" + appName, String.class);
            System.out.println(result);

            // Nacos 服务器地址
            String serverAddr = nacosDiscoveryProperties.getServerAddr();
            // 命名空间，非必须，根据实际情况填写
            String namespace = nacosDiscoveryProperties.getNamespace();
            // 设置 Nacos 的属性
            Properties properties = new Properties();
            properties.put("serverAddr", serverAddr);
            properties.put("namespace", namespace);

            /**
             * 2、直接使用Nacos服务发现获取服务实例(但是这种方式没有负载均衡功能),并使用普通RestTemplate发送http请求到服务端
             */
            NamingService namingService = NacosFactory.createNamingService(properties);
            List<Instance> allInstances = namingService.getAllInstances("nacos-provider-config");
            Instance instance = allInstances.get(0);
            String path1 = String.format("http://%s:%s/echo/%s",instance.getIp(),instance.getPort(),appName);
            result = restTemplate.getForObject(path1, String.class);
            System.out.println(result);


            /**
             * 3、通过对RestTemplate添加@LoadBalanced标记注解，使得LoadBalancerInterceptor介入使用LoadBalancerClient获取服务实例修改请求后向实际服务端发送请求
             * 3.1、由于gateway中不存在ribbon依赖，所以使用的LoadBalancerClient实现是BlockingLoadBalancerClient
             * 具体的探究参看gateway-test/gateway中的NacosController
             */
            if (restTemplate.getClass().getAnnotation(LoadBalanced.class) != null){
                ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://nacos-provider-config/echo/" + "hello", String.class);
                result = responseEntity.getBody();
                System.out.println(result);
            }


            return result;

        }

    }

    //Instantiate RestTemplate Instance
    @Bean
    /**
     * 第3种服务调用需要加该注解
     */
//    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    public static void main(String[] args) {

        SpringApplication.run(NacosConsumerApplication.class,args);
    }
}
