[![Build Status](https://travis-ci.org/ericdahl/spring-cloud-config-example.svg)](https://travis-ci.org/ericdahl/spring-cloud-config-example)

# spring-cloud-config-example
basic example of using spring-cloud-config to retrieve configs from a git-backed server

## Quick Start

### Build code

```bash
git clone git@github.com:ericdahl/spring-cloud-config-example.git
cd spring-cloud-config-example
mvn clean package
```

### Start Config Server

```bash
java -jar server/target/spring-cloud-config-example-server-1.0-SNAPSHOT.jar
```

Load [http://localhost:8888/master/development](http://localhost:8888/master/development). 
This displays the config properties which are being retrieved from the git repo defined 
in bootstrap.yml. This currently is the [`server-config` directory in this repository](https://github.com/ericdahl/spring-cloud-config-example/tree/master/server-config).

Note: keep the server running in backround. The client app in the next step needs to connect to it.

### Start Client App
```bash
java -jar client/target/*jar
```

Load [http://localhost:8080](http://localhost:8080) to see the property from the server. 
Alternatively, you can inspect the properties and their sources from the spring-boot-actuator
endpoint at [http://localhost:8080/env](http://localhost:8080/env)

### Reload configuration from server (at runtime)

Spring Cloud Config has a `@RefreshScope` mechanism to allow beans to be reinitialized
on demand to fetch updated configuration values. The AppController on the client
has this annotation, so it will display the new config value once the refresh
endpoint is called.

```bash
curl -X POST 'http://localhost:8080/actuator/refresh'
```


server[spring-cloud-config-example-server]：作为spring cloud config server，对外提供配置中心功能。
native-server-config:提供作为配置中心的本地配置存储目录。
git-server-config:提供作为配置中心的git配置存储目录。
client[spring-cloud-config-example-client]：作为微服务读取配置中心服务提供的配置信息，注意不需要注册中心来发现，直接通过ip端口前缀获取。

nacos-provider-config：利用nacos作为配置中心和注册中心，学习使用多数据源@DS
nacos-consumer：从nacos获取服务，并使用原生方法RestTemplate调用nacos-provider-config的接口

distributed-transaction：nacos微服务+seata分布时事务，
