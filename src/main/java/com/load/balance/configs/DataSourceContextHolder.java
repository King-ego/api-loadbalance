package com.load.balance.configs;

public class DataSourceContextHolder {
    private enum DataSourceType {
        WRITE, READ_1, READ_2
    }

    private static final ThreadLocal<DataSourceType> contextHolder = new ThreadLocal<>();
}
