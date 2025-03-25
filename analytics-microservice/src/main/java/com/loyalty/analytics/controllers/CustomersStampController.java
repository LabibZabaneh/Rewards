package com.loyalty.analytics.controllers;

import com.loyalty.analytics.domain.Customer;
import com.loyalty.analytics.domain.DailyStampCount;
import com.loyalty.analytics.dto.DateStampDTO;
import com.loyalty.analytics.repositories.CustomersRepository;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import jakarta.inject.Inject;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Controller("/customers")
public class CustomersStampController {

    @Inject
    CustomersRepository customersRepo;

    @Get("/{id}/stamps/total")
    public HttpResponse<Integer> getTotalStamps(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(customer.get().getTotalStamps());
    }

    @Get("/{id}/stamps/today")
    public HttpResponse<Integer> getDailyStamps(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        LocalDate today = LocalDate.now();
        Set<DailyStampCount> stampCount = customer.get().getDailyStampCounts();

        int countToday = stampCount.stream()
                .filter(stamp -> stamp.getDate().equals(today))
                .mapToInt(DailyStampCount::getStampCount)
                .findFirst()
                .orElse(0);

        return HttpResponse.ok(countToday);
    }

    @Get("/{id}/stamps/total/weekly")
    public HttpResponse<Integer> getWeeklyStamps(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        int total = customer.get().getDailyStampCounts().stream()
                .filter(s -> !s.getDate().isBefore(startOfWeek) && !s.getDate().isAfter(endOfWeek))
                .mapToInt(DailyStampCount::getStampCount)
                .sum();

        return HttpResponse.ok(total);
    }

    @Get("/{id}/stamps/average/weekly")
    public HttpResponse<Double> getWeeklyAverageStamps(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        double total = customer.get().getDailyStampCounts().stream()
                .filter(s -> !s.getDate().isBefore(startOfWeek) && !s.getDate().isAfter(endOfWeek))
                .mapToDouble(DailyStampCount::getStampCount)
                .average()
                .orElse(0.0);

        return HttpResponse.ok(total);
    }

    @Get("/{id}/stamps/chart/weekly")
    public HttpResponse<List<DateStampDTO>> getWeeklyChartStamps(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate endOfWeek = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        Map<LocalDate, Integer> stampCountMap = customer.get().getDailyStampCounts().stream()
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

        return HttpResponse.ok(chartData);

    }

    @Get("/{id}/stamps/total/monthly")
    public HttpResponse<Integer> getMonthlyStamps(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        int total = customer.get().getDailyStampCounts().stream()
                .filter(s -> !s.getDate().isBefore(startOfMonth) && !s.getDate().isAfter(endOfMonth))
                .mapToInt(DailyStampCount::getStampCount)
                .sum();

        return HttpResponse.ok(total);
    }

    @Get("/{id}/stamps/average/monthly")
    public HttpResponse<Double> getMonthlyAverageStamps(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        double total = customer.get().getDailyStampCounts().stream()
                .filter(s -> !s.getDate().isBefore(startOfMonth) && !s.getDate().isAfter(endOfMonth))
                .mapToInt(DailyStampCount::getStampCount)
                .average()
                .orElse(0.0);

        return HttpResponse.ok(total);
    }

    @Get("/{id}/stamps/top-day/monthly")
    public HttpResponse<DateStampDTO> getTopDayMonthlyStamps(@PathVariable Long id) {
        Optional<Customer> customer = customersRepo.findById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        LocalDate today = LocalDate.now();
        LocalDate startOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfMonth = today.with(TemporalAdjusters.lastDayOfMonth());

        Optional<DailyStampCount> topDay = customer.get().getDailyStampCounts().stream()
                .filter(s -> !s.getDate().isBefore(startOfMonth) && !s.getDate().isAfter(endOfMonth))
                .max(Comparator.comparingInt(DailyStampCount::getStampCount));

        if (topDay.isEmpty()) {
            return HttpResponse.ok(new DateStampDTO(null, null, 0));
        }

        DailyStampCount stamp = topDay.get();
        String dayName = stamp.getDate().getDayOfWeek().toString();

        return HttpResponse.ok(new DateStampDTO(stamp.getDate(), dayName, stamp.getStampCount()));
    }

}
