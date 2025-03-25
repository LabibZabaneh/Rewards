package com.loyalty.analytics.service;

import com.loyalty.analytics.domain.Customer;
import com.loyalty.analytics.domain.DailyStampCount;
import com.loyalty.analytics.dto.DateStampDTO;
import com.loyalty.analytics.repositories.CustomersRepository;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class CustomersService {

    @Inject
    CustomersRepository customersRepo;

    public Optional<Customer> findCustomerById(Long id) {
        return customersRepo.findById(id);
    }

    public Integer getTodayStamps(Customer customer) {
        LocalDate today = LocalDate.now();
        Set<DailyStampCount> stampCount = customer.getDailyStampCounts();

        return stampCount.stream()
                .filter(stamp -> stamp.getDate().equals(today))
                .mapToInt(DailyStampCount::getStampCount)
                .findFirst()
                .orElse(0);
    }

    public Integer getWeeklyStamps(Customer customer) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        return customer.getDailyStampCounts().stream()
                .filter(s -> !s.getDate().isBefore(startOfWeek) && !s.getDate().isAfter(endOfWeek))
                .mapToInt(DailyStampCount::getStampCount)
                .sum();
    }

    public Integer getMonthlyStamps(Customer customer) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        return customer.getDailyStampCounts().stream()
                .filter(s -> !s.getDate().isBefore(startOfMonth) && !s.getDate().isAfter(endOfMonth))
                .mapToInt(DailyStampCount::getStampCount)
                .sum();
    }

    public Double getWeeklyAverageStamps(Customer customer) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        int total = customer.getDailyStampCounts().stream()
                .filter(s -> !s.getDate().isBefore(startOfWeek) && !s.getDate().isAfter(endOfWeek))
                .mapToInt(DailyStampCount::getStampCount)
                .sum();

        return total / 7.0;
    }

    public Double getMonthlyAverageStamps(Customer customer) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        double total = customer.getDailyStampCounts().stream()
                .filter(s -> !s.getDate().isBefore(startOfMonth) && !s.getDate().isAfter(endOfMonth))
                .mapToInt(DailyStampCount::getStampCount)
                .sum();

        return total / startOfMonth.lengthOfMonth();
    }

    public List<DateStampDTO> getWeeklyChartStamps(Customer customer) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        Map<LocalDate, Integer> stampCountMap = customer.getDailyStampCounts().stream()
                .filter(s -> !s.getDate().isBefore(startOfWeek) && !s.getDate().isAfter(endOfWeek))
                .collect(Collectors.toMap(
                        DailyStampCount::getDate,
                        DailyStampCount::getStampCount
                ));

        List<DateStampDTO> chartData = new ArrayList<>();
        for (LocalDate date = startOfWeek; !date.isAfter(endOfWeek); date = date.plusDays(1)){
            int count = stampCountMap.getOrDefault(date, 0);
            String day = date.getDayOfWeek().toString();
            chartData.add(new DateStampDTO(date, day, count));
        }

        return chartData;
    }

    public List<DateStampDTO> getMonthlyChartStamps(Customer customer) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        Map<LocalDate, Integer> stampCountMap = customer.getDailyStampCounts().stream()
                .filter(s -> !s.getDate().isBefore(startOfMonth) && !s.getDate().isAfter(endOfMonth))
                .collect(Collectors.toMap(
                        DailyStampCount::getDate,
                        DailyStampCount::getStampCount
                ));

        List<DateStampDTO> chartData = new ArrayList<>();
        for (LocalDate date = startOfMonth; !date.isAfter(endOfMonth); date = date.plusDays(1)){
            int count = stampCountMap.getOrDefault(date, 0);
            String day = date.getDayOfWeek().toString();
            chartData.add(new DateStampDTO(date, day, count));
        }

        return chartData;
    }

    public DailyStampCount getTopDayMonthlyStamps(Customer customer) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        Optional<DailyStampCount> topDay = customer.getDailyStampCounts().stream()
                .filter(s -> !s.getDate().isBefore(startOfMonth) && !s.getDate().isAfter(endOfMonth))
                .max(Comparator.comparingInt(DailyStampCount::getStampCount));

        return topDay.orElse(null);
    }




}
