package org.springframework.cloud.endpoint.event;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.ds.ItemDataSource;
import com.baomidou.dynamic.datasource.enums.SeataMode;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.zaxxer.hikari.HikariDataSource;
import example.NacosProviderConfigApplication;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.SmartApplicationListener;
import org.springframework.core.env.MutablePropertySources;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 自定义覆盖RefreshEventListener，加入对RefreshEvent事件的处理逻辑，即读取最新的配置信息后重新配置DynamicRoutingDataSource
 * 实现实时根据配置信息创建DataSource实例。
 */
public class RefreshEventListener implements SmartApplicationListener {

    @Autowired
    private DynamicRoutingDataSource dataSource;

    @Autowired
    private DynamicDataSourceProperties dynamicDataSourceProperties;

    private static Log log = LogFactory.getLog(org.springframework.cloud.endpoint.event.RefreshEventListener.class);

    private ContextRefresher refresh;

    private AtomicBoolean ready = new AtomicBoolean(false);

    public RefreshEventListener(ContextRefresher refresh) {
        this.refresh = refresh;
    }

    @Override
    public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
        return ApplicationReadyEvent.class.isAssignableFrom(eventType)
                || RefreshEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ApplicationReadyEvent) {
            handle((ApplicationReadyEvent) event);
        } else if (event instanceof RefreshEvent) {
            handle((RefreshEvent) event);
        }
    }

    public void handle(ApplicationReadyEvent event) {
        this.ready.compareAndSet(false, true);
    }

    public void handle(RefreshEvent event) {
        if (this.ready.get()) { // don't handle events before app is ready
            log.debug("Event received " + event.getEventDesc());
            Set<String> keys = this.refresh.refresh();
            log.info("Refresh keys changed: " + keys);

            Map<String, DataSource> dataSources = dataSource.getDataSources();
            Map<String, DataSourceProperty> datasource = dynamicDataSourceProperties.getDatasource();

            /**
             * 应该动态读取当前的dataSources和dynamicDataSourceProperties，然后全量更新dataSources
             */

            ConfigurableApplicationContext applicationContext = NacosProviderConfigApplication.applicationContext;
            String url = applicationContext.getEnvironment().getProperty("spring.datasource.dynamic.datasource.master.url");
            String username = applicationContext.getEnvironment().getProperty("spring.datasource.dynamic.datasource.master.username");
            String password = applicationContext.getEnvironment().getProperty("spring.datasource.dynamic.datasource.master.password");
            String driverClassName = applicationContext.getEnvironment().getProperty("spring.datasource.dynamic.datasource.master.driver-class-name");

            MutablePropertySources propertySources = applicationContext.getEnvironment().getPropertySources();

            try {

                HikariDataSource hikariDataSource = new HikariDataSource();
                hikariDataSource.setJdbcUrl(url);
                hikariDataSource.setUsername(username);
                hikariDataSource.setPassword(password);
                hikariDataSource.setDriverClassName(driverClassName);

                ItemDataSource itemDataSource = new ItemDataSource("master", hikariDataSource, hikariDataSource, false, false, SeataMode.AT);

                dataSource.addDataSource("master", itemDataSource);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
