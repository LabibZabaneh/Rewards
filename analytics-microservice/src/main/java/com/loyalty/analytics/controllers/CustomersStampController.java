package com.loyalty.analytics.controllers;

import com.loyalty.analytics.domain.Customer;
import com.loyalty.analytics.domain.DailyStampCount;
import com.loyalty.analytics.dto.DateStampDTO;
import com.loyalty.analytics.service.CustomersService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import jakarta.inject.Inject;

import java.util.*;

@Controller("/customers")
public class CustomersStampController {

    @Inject
    CustomersService service;

    @Get("/{id}/stamps/total")
    public HttpResponse<Integer> getTotalStamps(@PathVariable Long id) {
        Optional<Customer> customer = service.findCustomerById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }
        return HttpResponse.ok(customer.get().getTotalStamps());
    }

    @Get("/{id}/stamps/today")
    public HttpResponse<Integer> getDailyStamps(@PathVariable Long id) {
        Optional<Customer> customer = service.findCustomerById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        return HttpResponse.ok(service.getTodayStamps(customer.get()));
    }

    @Get("/{id}/stamps/total/weekly")
    public HttpResponse<Integer> getWeeklyStamps(@PathVariable Long id) {
        Optional<Customer> customer = service.findCustomerById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        return HttpResponse.ok(service.getWeeklyStamps(customer.get()));
    }

    @Get("/{id}/stamps/total/monthly")
    public HttpResponse<Integer> getMonthlyStamps(@PathVariable Long id) {
        Optional<Customer> customer = service.findCustomerById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        return HttpResponse.ok(service.getMonthlyStamps(customer.get()));
    }

    @Get("/{id}/stamps/average/weekly")
    public HttpResponse<Double> getWeeklyAverageStamps(@PathVariable Long id) {
        Optional<Customer> customer = service.findCustomerById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        return HttpResponse.ok(service.getWeeklyAverageStamps(customer.get()));
    }

    @Get("/{id}/stamps/average/monthly")
    public HttpResponse<Double> getMonthlyAverageStamps(@PathVariable Long id) {
        Optional<Customer> customer = service.findCustomerById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        return HttpResponse.ok(service.getMonthlyAverageStamps(customer.get()));
    }


    @Get("/{id}/stamps/charts/weekly")
    public HttpResponse<List<DateStampDTO>> getWeeklyChartStamps(@PathVariable Long id) {
        Optional<Customer> customer = service.findCustomerById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        return HttpResponse.ok(service.getWeeklyChartStamps(customer.get()));
    }

    @Get("/{id}/stamps/charts/monthly")
    public HttpResponse<List<DateStampDTO>> getMonthlyChartStamps(@PathVariable Long id) {
        Optional<Customer> customer = service.findCustomerById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        return HttpResponse.ok(service.getMonthlyChartStamps(customer.get()));
    }

    @Get("/{id}/stamps/top-day/monthly")
    public HttpResponse<DateStampDTO> getTopDayMonthlyStamps(@PathVariable Long id) {
        Optional<Customer> customer = service.findCustomerById(id);
        if (customer.isEmpty()) {
            return HttpResponse.notFound();
        }

        DailyStampCount topDay = service.getTopDayMonthlyStamps(customer.get());

        if (topDay == null) {
            return HttpResponse.ok(new DateStampDTO(null, null, 0));
        }

        String dayName = topDay.getDate().getDayOfWeek().toString();

        return HttpResponse.ok(new DateStampDTO(topDay.getDate(), dayName, topDay.getStampCount()));
    }

}
