package example.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusLanguageDriverAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.autoconfigure.SpringBootVFS;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.scripting.LanguageDriver;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.function.Consumer;

@Configuration
@EnableConfigurationProperties({DynamicDataSourceProperties.class,MybatisPlusProperties.class})
@AutoConfigureAfter({DataSourceAutoConfiguration.class, MybatisPlusLanguageDriverAutoConfiguration.class})
public class MyBatisConfig {

    private final DynamicDataSourceProperties dynamicDataSourceProperties;

    private final MybatisPlusProperties mybatisPlusProperties;

    private final ApplicationContext applicationContext;

    public MyBatisConfig(DynamicDataSourceProperties dynamicDataSourceProperties,
                         MybatisPlusProperties mybatisPlusProperties,
                         ApplicationContext applicationContext) {
        this.dynamicDataSourceProperties = dynamicDataSourceProperties;
        this.mybatisPlusProperties = mybatisPlusProperties;
        this.applicationContext = applicationContext;
    }

//    @Primary
//    @Bean
//    public DataSource dataSource() {
//        DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
//        dataSource.setPrimary(dynamicDataSourceProperties.getPrimary());
//        dataSource.setStrict(dynamicDataSourceProperties.getStrict());
//        dataSource.setStrategy(dynamicDataSourceProperties.getStrategy());
//        dataSource.setP6spy(dynamicDataSourceProperties.getP6spy());
//        dataSource.setSeata(dynamicDataSourceProperties.getSeata());
//        return dataSource;
//    }
//
//
//    @Bean(name = "mysqlDataSource")
//    public DataSource mysqlDataSource() {
//        DruidDataSource dataSource = new DruidDataSource();
//        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
//        dataSource.setName("mysql_test");
//        dataSource.setUrl("jdbc:mysql://localhost:3306/test");
//        dataSource.setUsername("root");
//        dataSource.setPassword("");
//        return dataSource;
//    }
//
//    @Bean
//    public SqlSessionFactory sqlSessionFactory(@Qualifier("mysqlDataSource") DataSource dataSource,) throws Exception {
//        MybatisSqlSessionFactoryBean factory = new MybatisSqlSessionFactoryBean();
//        factory.setDataSource(dataSource);
//        factory.setVfs(SpringBootVFS.class);
//        if (StringUtils.hasText(this.mybatisPlusProperties.getConfigLocation())) {
//            factory.setConfigLocation(this.resourceLoader.getResource(this.mybatisPlusProperties.getConfigLocation()));
//        }
//        applyConfiguration(factory);
//        if (this.mybatisPlusProperties.getConfigurationProperties() != null) {
//            factory.setConfigurationProperties(this.mybatisPlusProperties.getConfigurationProperties());
//        }
//        if (!ObjectUtils.isEmpty(this.interceptors)) {
//            factory.setPlugins(this.interceptors);
//        }
//        if (this.databaseIdProvider != null) {
//            factory.setDatabaseIdProvider(this.databaseIdProvider);
//        }
//        if (StringUtils.hasLength(this.mybatisPlusProperties.getTypeAliasesPackage())) {
//            factory.setTypeAliasesPackage(this.mybatisPlusProperties.getTypeAliasesPackage());
//        }
//        if (this.mybatisPlusProperties.getTypeAliasesSuperType() != null) {
//            factory.setTypeAliasesSuperType(this.mybatisPlusProperties.getTypeAliasesSuperType());
//        }
//        if (StringUtils.hasLength(this.mybatisPlusProperties.getTypeHandlersPackage())) {
//            factory.setTypeHandlersPackage(this.mybatisPlusProperties.getTypeHandlersPackage());
//        }
//        if (!ObjectUtils.isEmpty(this.typeHandlers)) {
//            factory.setTypeHandlers(this.typeHandlers);
//        }
//        Resource[] mapperLocations = this.mybatisPlusProperties.resolveMapperLocations();
//        if (!ObjectUtils.isEmpty(mapperLocations)) {
//            factory.setMapperLocations(mapperLocations);
//        }
//        // TODO ???????????????????????? TransactionFactory
//        this.getBeanThen(TransactionFactory.class, factory::setTransactionFactory);
//
//        // TODO ??????????????????????????????(??????????????????????????????mybatis??????,????????????????????????)
//        Class<? extends LanguageDriver> defaultLanguageDriver = this.mybatisPlusProperties.getDefaultScriptingLanguageDriver();
//        if (!ObjectUtils.isEmpty(this.languageDrivers)) {
//            factory.setScriptingLanguageDrivers(this.languageDrivers);
//        }
//        Optional.ofNullable(defaultLanguageDriver).ifPresent(factory::setDefaultScriptingLanguageDriver);
//
//        // TODO ??????????????????
//        if (StringUtils.hasLength(this.mybatisPlusProperties.getTypeEnumsPackage())) {
//            factory.setTypeEnumsPackage(this.mybatisPlusProperties.getTypeEnumsPackage());
//        }
//        // TODO ??????????????? NULL
//        GlobalConfig globalConfig = this.mybatisPlusProperties.getGlobalConfig();
//        // TODO ???????????????
//        this.getBeanThen(MetaObjectHandler.class, globalConfig::setMetaObjectHandler);
//        // TODO ?????????????????????
//        this.getBeansThen(IKeyGenerator.class, i -> globalConfig.getDbConfig().setKeyGenerators(i));
//        // TODO ??????sql?????????
//        this.getBeanThen(ISqlInjector.class, globalConfig::setSqlInjector);
//        // TODO ??????ID?????????
//        this.getBeanThen(IdentifierGenerator.class, globalConfig::setIdentifierGenerator);
//        // TODO ?????? GlobalConfig ??? MybatisSqlSessionFactoryBean
//        factory.setGlobalConfig(globalConfig);
//        return factory.getObject();
//    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //?????????????????????????????????????????????DbType????????????????????????
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        // interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

    /**
     * ??????spring???????????????????????????bean,??????????????????
     *
     * @param clazz    class
     * @param consumer ??????
     * @param <T>      ??????
     */
    private <T> void getBeanThen(Class<T> clazz, Consumer<T> consumer) {
        if (this.applicationContext.getBeanNamesForType(clazz, false, false).length > 0) {
            consumer.accept(this.applicationContext.getBean(clazz));
        }
    }
}
