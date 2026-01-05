package com.example.parkinglot.common.utils;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtils {

    //计算停车时长（小时），不足1小时按1小时算

    public static long calculateParkingHours(LocalDateTime entryTime, LocalDateTime exitTime) {
        if (entryTime == null || exitTime == null) {
            return 0;
        }

        Duration duration = Duration.between(entryTime, exitTime);
        long minutes = duration.toMinutes();

        if (minutes <= 0) return 0;

        //相当于 ceil(分钟/60)
        return (minutes + 59) / 60;
    }
}