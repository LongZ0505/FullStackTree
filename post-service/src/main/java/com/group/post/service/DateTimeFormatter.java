package com.group.post.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class DateTimeFormatter {
    private Map<Long, Function<Instant, String>> formatter= new HashMap<>();
    public DateTimeFormatter(){
        formatter.put(60L,this::FormatBySecond);
        formatter.put(3600L,this::FormatByMinute);
        formatter.put(86400L,this::FormatByHour);
        formatter.put(Long.MAX_VALUE,this::FormatOverRange);
    }
    public String format(Instant instant){
        var time= ChronoUnit.SECONDS.between(instant,Instant.now());
        var result=formatter.entrySet().stream().filter
                (longFunctionEntry -> time<longFunctionEntry.getKey()).
                findFirst().get();
        return result.getValue().apply(instant);
    }
    public String FormatBySecond(Instant instant){
        var time= ChronoUnit.SECONDS.between(instant,Instant.now());
        return String.format("%s seconds(s) ago",time);
    }
    public String FormatByMinute(Instant instant){
        var time= ChronoUnit.MINUTES.between(instant,Instant.now());
        return String.format("%s minute(s) ago",time);
    }
    public String FormatByHour(Instant instant){
        var time= ChronoUnit.HOURS.between(instant,Instant.now());
        return String.format("%s hour(s) ago",time);
    }
    public String FormatOverRange(Instant instant){
        LocalDateTime localDateTime = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        java.time.format.DateTimeFormatter dateTimeFormatter = java.time.format.DateTimeFormatter.ISO_DATE;

        return localDateTime.format(dateTimeFormatter);
    }
}
