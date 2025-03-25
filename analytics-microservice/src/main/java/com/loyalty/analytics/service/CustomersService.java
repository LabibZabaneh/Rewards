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

        return getTotalStamps(customer, startOfWeek, endOfWeek);
    }

    public Integer getMonthlyStamps(Customer customer) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        return getTotalStamps(customer, startOfMonth, endOfMonth);
    }

    public Double getWeeklyAverageStamps(Customer customer) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        return getTotalStamps(customer, startOfWeek, endOfWeek) / 7.0;
    }

    public Double getMonthlyAverageStamps(Customer customer) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        return (double) (getTotalStamps(customer, startOfMonth, endOfMonth) / startOfMonth.lengthOfMonth());
    }

    public List<DateStampDTO> getWeeklyChartStamps(Customer customer) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        Map<LocalDate, Integer> stampCountMap = getChartStamps(customer, startOfWeek, endOfWeek);

        return sortChartStamps(stampCountMap, startOfWeek, endOfWeek);
    }

    public List<DateStampDTO> getMonthlyChartStamps(Customer customer) {
        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        Map<LocalDate, Integer> stampCountMap = getChartStamps(customer, startOfMonth, endOfMonth);

        return sortChartStamps(stampCountMap, startOfMonth, endOfMonth);
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

    private Integer getTotalStamps(Customer customer, LocalDate start, LocalDate end) {
        return customer.getDailyStampCounts().stream()
                .filter(s -> !s.getDate().isBefore(start) && !s.getDate().isAfter(end))
                .mapToInt(DailyStampCount::getStampCount)
                .sum();
    }

    private Map<LocalDate, Integer> getChartStamps(Customer customer, LocalDate start, LocalDate end) {
        return customer.getDailyStampCounts().stream()
                .filter(s -> !s.getDate().isBefore(start) && !s.getDate().isAfter(end))
                .collect(Collectors.toMap(
                        DailyStampCount::getDate,
                        DailyStampCount::getStampCount
                ));
    }

    private List<DateStampDTO> sortChartStamps(Map<LocalDate, Integer> stampCountMap, LocalDate start, LocalDate end) {
        List<DateStampDTO> chartData = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)){
            int count = stampCountMap.getOrDefault(date, 0);
            String day = date.getDayOfWeek().toString();
            chartData.add(new DateStampDTO(date, day, count));
        }
        return chartData;
    }



}
