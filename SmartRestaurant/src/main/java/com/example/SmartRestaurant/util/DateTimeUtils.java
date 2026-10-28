package com.example.SmartRestaurant.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class DateTimeUtils {

    public static LocalDateTime getStartDateTime(LocalDate date, LocalTime time) {
        return date.atTime(time);
    }

    public static LocalDateTime getEndDateTime(LocalDate date, LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) {
            return date.plusDays(1).atTime(endTime);
        } else {
            return date.atTime(endTime);
        }
    }
}
