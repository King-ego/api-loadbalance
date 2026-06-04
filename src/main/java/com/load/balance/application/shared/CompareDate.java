package com.load.balance.application.shared;

import java.time.LocalDateTime;

public class CompareDate {
    public static void after(LocalDateTime date1, LocalDateTime date2) {
        if (!date1.isAfter(date2)) {
            throw new RuntimeException("Date1 not is after Date2");
        }
    }

    public static void before(LocalDateTime date1, LocalDateTime date2) {
        if (!date1.isBefore(date2)) {
            throw new RuntimeException("Date1 not is before Date2");
        }
    }

    public static void equals(LocalDateTime date1, LocalDateTime date2) {
        if (!date1.isEqual(date2)) {
            throw new RuntimeException("Date1 not equals Date2");
        }
    }
}
