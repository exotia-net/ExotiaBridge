package net.exotia.bridge.api.user;

import net.exotia.bridge.api.entities.CalendarUser;

import java.util.UUID;

@Deprecated
public interface ApiCalendarService {
    void reset(UUID uuid);
    void add(UUID uuid, CalendarUser calendarUser);
    void add(ApiUser user, CalendarUser calendarUser);
}
