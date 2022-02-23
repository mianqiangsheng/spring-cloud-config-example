package example;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableDiscoveryClient
public class NacosProviderConfigApplication {

    public static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) throws NacosException, InterruptedException {
        applicationContext = SpringApplication.run(NacosProviderConfigApplication.class, args);

        Environment environment = applicationContext.getBean(Environment.class);
        StringEncryptor codeSheepEncryptorBean = applicationContext.getBean("codeSheepEncryptorBean",StringEncryptor.class);

        // 首先获取配置文件里的原始明文信息
        String password = environment.getProperty("password");

        // 加密
        String encryptedPassword = codeSheepEncryptorBean.encrypt(password);

        System.out.println("password明文：" + password);
        System.out.println("password密文：" + encryptedPassword);

        /**
         * http://localhost:8881/actuator/nacosconfig
         *
         * 需要在bootstrap.yml(.properties)配置nacos config的相关配置，并且引入spring-cloud-starter-bootstrap依赖开启bootstrap context
         * 才能让spring boot启动的时候去读取远程上的配置信息并放入到Environment中去
         *
         * bootstrap.yml （启动bootstrap context，读取bootstrap.yml上的配置放到当前的Environment中）->
         * NacosPropertySourceLocator/NacosConfigManager/NacosConfigProperties（刷新spring上下文初始化这些类） ->
         * BootstrapApplicationListener监听到事件 ->
         * application.yml（读取application.yml放入当前上下文的Environment中）->
         * 将所有上下文读取到的Environment合并到spring boot的上下文中 ->
         * 执行SpringApplication.prepareContext方法 ->
         * 执行SpringApplication.applyInitializers方法 ->
         * 执行PropertySourceBootstrapConfiguration.initialize方法 ->
         * 执行NacosPropertySourceLocator.locateCollection方法 ->
         * NacosPropertySourceLocator.loadApplicationConfiguration ->
         * NacosConfigService.getConfig ->
         * ClientWorker.getServerConfig（根据配置的nacos服务器信息发送http请求获取配置信息，将这些远程获取到的配置信息放入Environment）
         */
//        String userName = applicationContext.getEnvironment().getProperty("name");
//        String userAge = applicationContext.getEnvironment().getProperty("age");
//        System.out.println("name :" +userName+"; age: "+userAge);

        /**
         * RefreshEventListener在服务启动时实例化这个Listener bean;
         * NacosContextRefresher监听ApplicationReadyEvent事件，在服务启动时注册这个响应nacos发布修改配置事件时发布RefreshEvent事件的Listener;
         * 当nacos服务器刷新配置时，调用NacosContextRefresher的innerReceive方法发布RefreshEvent事件，然后由RefreshEventListener处理该事件
         * 最终调用ContextRefresher的refreshEnvironment方法，更新Environment，并且会发布一个EnvironmentChangeEvent事件
         *
         * When configurations are refreshed dynamically, they will be updated in the Enviroment, therefore here we retrieve configurations from Environment every other 3 seconds.
         * You can disable automatic refresh with this setting`spring.cloud.nacos.config.refresh-enabled=false`.
         */
//        while(true) {
//            String userName = applicationContext.getEnvironment().getProperty("name");
//            String userAge = applicationContext.getEnvironment().getProperty("age");
//            System.err.println("name :" + userName + "; age: " + userAge);
//            TimeUnit.SECONDS.sleep(3);
//        }


        /**
         * 手动读取nacos上的配置信息，无需通过spring boot提供的自动读取放入Environment，直接读取
         */
//        String serverAddr = "localhost:8848";
//        String dataId = "nacos-provider-config.properties";
//        String group = "DEFAULT_GROUP";
//        Properties properties = new Properties();
//        properties.put(PropertyKeyConst.SERVER_ADDR, serverAddr);
//        properties.setProperty(PropertyKeyConst.NAMESPACE,"84824bfd-2abb-4731-b281-b8063b16fa3b");
//        ConfigService configService = NacosFactory.createConfigService(properties);
//        String content = configService.getConfig(dataId, group, 5000);
//        System.out.println(content);
//        configService.addListener(dataId, group, new Listener() {
//            @Override
//            public void receiveConfigInfo(String configInfo) {
//                System.out.println("recieve:" + configInfo);
//            }
//
//            @Override
//            public Executor getExecutor() {
//                return null;
//            }
//        });

        /**
         * 手动发布配置信息
         */
//        boolean isPublishOk = configService.publishConfig(dataId, group, "content");
//        System.out.println(isPublishOk);

//        Thread.sleep(3000);
//        content = configService.getConfig(dataId, group, 5000);
//        System.out.println(content);

        /**
         * 手动移除配置信息
         */
//        boolean isRemoveOk = configService.removeConfig(dataId, group);
//        System.out.println(isRemoveOk);
//        Thread.sleep(3000);

//        content = configService.getConfig(dataId, group, 5000);
//        System.out.println(content);
//        Thread.sleep(300000);

    }

    @RestController
    public class EchoController {

        @Value("${name}")
        private String name;

        @GetMapping(value = "/echo/{string}")
        public String echo(@PathVariable String string) {
            return "Hello Nacos Discovery " + string;
        }

        @GetMapping(value = "/get/name")
        public String getName() {
            return "name: " + name;
        }

    }
}
