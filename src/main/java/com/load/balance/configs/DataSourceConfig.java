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

    private HikariDataSource buildDataSource(String url) {
        HikariDataSource ds = new HikariDataSource();
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setDriverClassName("org.postgresql.Driver");
        return ds;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        String[] urls = readUrls.split(",");

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DataSourceType.WRITE, buildDataSource(writeUrl));
        targetDataSources.put(DataSourceType.READ_1, buildDataSource(urls[0].trim()));
        targetDataSources.put(DataSourceType.READ_2, buildDataSource(urls[1].trim()));

        RoutingDataSource routing =  new RoutingDataSource();
        routing.setTargetDataSources(targetDataSources);
        routing.setDefaultTargetDataSource(buildDataSource(writeUrl));
        return routing;
    }
}
