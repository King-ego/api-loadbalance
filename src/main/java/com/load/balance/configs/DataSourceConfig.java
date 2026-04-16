package com.load.balance.configs;

import com.load.balance.enums.DataSourceType;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {
    @Value("${db.write.url}")
    private String writeUrl;

    @Value("${db.read.urls}")
    private String readUrls;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${db.pool.write.max-size:50}")
    private int writeMaxPoolSize;

    @Value("${db.pool.write.min-idle:5}")
    private int writeMinIdle;

    @Value("${db.pool.read.max-size:20}")
    private int readMaxPoolSize;

    @Value("${db.pool.read.min-idle:5}")
    private int readMinIdle;

    private HikariDataSource buildDataSource(String url, String poolName, int maxSize, int minIdle) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("org.postgresql.Driver");
        ds.setPoolName(poolName);
        ds.setMaximumPoolSize(maxSize);
        ds.setMinimumIdle(minIdle);
        ds.setConnectionTimeout(30_000);
        ds.setIdleTimeout(600_000);
        ds.setMaxLifetime(1_800_000);

        ds.addDataSourceProperty("preparedStatementCacheQueries", "0");
        ds.setConnectionInitSql("DEALLOCATE ALL");

        return ds;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        String[] urls = readUrls.split(",");

        Map<Object, Object> targetDataSources = new HashMap<>();

        targetDataSources.put(DataSourceType.WRITE,
                buildDataSource(writeUrl, "pool-write", writeMaxPoolSize, writeMinIdle));

        targetDataSources.put(DataSourceType.READ_1,
                buildDataSource(urls[0].trim(), "pool-read-1", readMaxPoolSize, readMinIdle));

        targetDataSources.put(DataSourceType.READ_2,
                buildDataSource(urls[1].trim(), "pool-read-2", readMaxPoolSize, readMinIdle));

        RoutingDataSource routing = new RoutingDataSource();
        routing.setTargetDataSources(targetDataSources);
        routing.setDefaultTargetDataSource(
                buildDataSource(writeUrl, "pool-write-default", writeMaxPoolSize, writeMinIdle));

        return routing;
    }
}