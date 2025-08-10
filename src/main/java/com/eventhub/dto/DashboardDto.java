package com.eventhub.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.List;

@Data
public class DashboardDto {
    @AllArgsConstructor
    public static class EventStatusCount {
        public int upcoming;
        public int ongoing;
        public int completed;
        public int cancelled;
    }

    public EventStatusCount eventStatusCount;
    public double venueUtilizationRate;
    public Map<String, Integer> eventTypeDistribution;
    public List<Integer> dailyCancellationCount;
    public List<Integer> dailyBookingCount;
    // TODO: add these fields
    // public double offeringProvidersUtilizationRate;
    // public Map<String, Double> revenuePerOrganizer;
    // public UserBreakdown userBreakdown;
}