package com.load.balance.application.shared;

import java.time.LocalDateTime;

public class CompareDate {
    public boolean after(LocalDateTime date1, LocalDateTime date2) {
        return date1.isAfter(date2);
    }

    public boolean before(LocalDateTime date1, LocalDateTime date2) {
        return date1.isBefore(date2);
    }

    public boolean equals(LocalDateTime date1, LocalDateTime date2) {
        return date1.isEqual(date2);
    }
}

