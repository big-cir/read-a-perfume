package io.perfume.api.common.config.datasourece;

import static io.perfume.api.common.config.datasourece.DataSourceType.type.*;

import org.springframework.context.annotation.Configuration;

@Deprecated
@Configuration
public class DataSourceConfiguration {

  //    private final Logger log = LoggerFactory.getLogger(getClass());
  //
  //    @Bean
  //    @Qualifier(MASTER_NAME)
  //    @ConfigurationProperties(prefix = "spring.datasource.master")
  //    public DataSource masterDataSource() {
  //        return DataSourceBuilder.create().build();
  //    }
  //
  //    @Bean
  //    @Qualifier(SLAVE_NAME)
  //    @ConfigurationProperties(prefix = "spring.datasource.slave")
  //    public DataSource slaveDataSource() {
  //        return DataSourceBuilder.create().build();
  //    }
  //
  //    @Bean
  //    @Qualifier(ROUTING_NAME)
  //    public DataSource routingDataSource(
  //            @Qualifier(MASTER_NAME) DataSource masterDataSource,
  //            @Qualifier(SLAVE_NAME) DataSource slaveDataSource
  //    ) {
  //        RoutingDataSource routingDataSource = new RoutingDataSource();
  //        routingDataSource.setDefaultTargetDataSource(masterDataSource);
  //
  //        Map<Object, Object> targetDataSources = new HashMap<>();
  //        targetDataSources.put(MASTER, masterDataSource);
  //        targetDataSources.put(SLAVE, slaveDataSource);
  //        routingDataSource.setTargetDataSources(targetDataSources);
  //
  //        return routingDataSource;
  //    }
  //
  //    @Bean
  //    @Primary
  //    public DataSource dataSource(
  //            @Qualifier(value = ROUTING_NAME) DataSource routingDataSource
  //    ) {
  //        return new LazyConnectionDataSourceProxy(routingDataSource);
  //    }
  //
  //    @Bean
  //    public PlatformTransactionManager transactionManager(DataSource dataSource) {
  //        DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
  //        transactionManager.setDataSource(dataSource);
  //
  //        return transactionManager;
  //    }
}
