package com.example.parkinglot.common.utils;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TimeUtilsTest {

    @Test
    void shouldReturnZeroWhenEntryTimeIsNull() {
        long hours = TimeUtils.calculateParkingHours(null, LocalDateTime.now());
        assertEquals(0, hours);
    }

    @Test
    void shouldReturnZeroWhenExitTimeIsNull() {
        long hours = TimeUtils.calculateParkingHours(LocalDateTime.now(), null);
        assertEquals(0, hours);
    }

    @Test
    void shouldReturnZeroWhenBothTimesAreNull() {
        long hours = TimeUtils.calculateParkingHours(null, null);
        assertEquals(0, hours);
    }

    @Test
    void shouldReturnZeroWhenExitTimeBeforeEntryTime() {
        LocalDateTime entryTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime exitTime = LocalDateTime.of(2024, 1, 1, 9, 0);
        long hours = TimeUtils.calculateParkingHours(entryTime, exitTime);
        assertEquals(0, hours);
    }

    @Test
    void shouldReturnZeroWhenSameTime() {
        LocalDateTime time = LocalDateTime.of(2024, 1, 1, 10, 0);
        long hours = TimeUtils.calculateParkingHours(time, time);
        assertEquals(0, hours);
    }

    @Test
    void shouldReturn1HourWhen59Minutes() {
        LocalDateTime entryTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime exitTime = LocalDateTime.of(2024, 1, 1, 10, 59);
        long hours = TimeUtils.calculateParkingHours(entryTime, exitTime);
        assertEquals(1, hours);
    }

    @Test
    void shouldReturn1HourWhen60Minutes() {
        LocalDateTime entryTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime exitTime = LocalDateTime.of(2024, 1, 1, 11, 0);
        long hours = TimeUtils.calculateParkingHours(entryTime, exitTime);
        assertEquals(1, hours);
    }

    @Test
    void shouldReturn2HoursWhen61Minutes() {
        LocalDateTime entryTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime exitTime = LocalDateTime.of(2024, 1, 1, 11, 1);
        long hours = TimeUtils.calculateParkingHours(entryTime, exitTime);
        assertEquals(2, hours);
    }

    @Test
    void shouldReturn2HoursWhen119Minutes() {
        LocalDateTime entryTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime exitTime = LocalDateTime.of(2024, 1, 1, 11, 59);
        long hours = TimeUtils.calculateParkingHours(entryTime, exitTime);
        assertEquals(2, hours);
    }

    @Test
    void shouldReturn3HoursWhen121Minutes() {
        LocalDateTime entryTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime exitTime = LocalDateTime.of(2024, 1, 1, 12, 1);
        long hours = TimeUtils.calculateParkingHours(entryTime, exitTime);
        assertEquals(3, hours);
    }
}