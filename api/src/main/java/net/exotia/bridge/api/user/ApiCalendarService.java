package net.exotia.bridge.api.user;

import net.exotia.bridge.api.entities.CalendarUser;

import java.util.UUID;

public interface ApiCalendarService {
    void reset(UUID uuid);
    void add(UUID uuid, CalendarUser calendarUser);
    void add(ApiUser user, CalendarUser calendarUser);
    void save(ApiUser user, int step, int streak);
}
