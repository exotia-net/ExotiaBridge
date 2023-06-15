package net.exotia.bridge.shared.services;

import net.exotia.bridge.api.user.ApiCalendarService;
import net.exotia.bridge.api.user.ApiUser;
import net.exotia.bridge.shared.exceptions.UndefinedUserException;
import net.exotia.bridge.api.entities.CalendarUser;
import net.exotia.bridge.shared.services.entities.User;

import java.util.ArrayList;
import java.util.UUID;

@Deprecated
public class CalendarService implements ApiCalendarService {
    private final UserService userService;

    public CalendarService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void reset(UUID uuid) {
        User user = this.userService.getUser(uuid);
        if (user == null) throw new UndefinedUserException(uuid);
        user.setCalendar(new CalendarUser(0, new ArrayList<>(), 0, 0));
    }
    @Override
    public void add(UUID uuid, CalendarUser calendarUser) {
        User user = this.userService.getUser(uuid);
        if (user == null) throw new UndefinedUserException(uuid);
        this.add(user, calendarUser);
    }
    @Override
    public void add(ApiUser user, CalendarUser calendarUser) {
        user.setCalendar(calendarUser);
    }
}
