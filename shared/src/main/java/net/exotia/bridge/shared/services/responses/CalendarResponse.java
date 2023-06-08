package net.exotia.bridge.shared.services.responses;

import lombok.Getter;
import net.exotia.bridge.api.entities.CalendarUser;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Getter
public class CalendarResponse {
    private int id;
    private int userId;
    private int step;
    private int streak;
    private String lastObtained;
    private String createdAt;
    private String updatedAt;

    public CalendarUser toCalendarUser() {
        LocalDateTime localDateTime = LocalDateTime.parse(this.lastObtained, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneOffset.UTC);
        return new CalendarUser(this.step, null, zonedDateTime.toInstant().toEpochMilli(), this.streak);
    }
}
