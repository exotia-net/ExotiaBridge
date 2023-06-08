package net.exotia.bridge.shared.services.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import net.exotia.bridge.api.entities.CalendarUser;
import net.exotia.bridge.api.user.ApiUser;

import java.util.UUID;

@Getter
@Setter
@Builder
public class User implements ApiUser {
    private UUID uuid;
    private String nickname;
    private String firstIp;
    private String lastIp;

    /**
     * Player balance (This value is different on every server)
     */
    private int balance;

    /**
     * Player wallet balance
     */
    private float coins;

    /**
     * Calendar module
     */
    private CalendarUser calendar;

    public ExotiaPlayer getExotiaPlayer() {
        return new ExotiaPlayer(this.uuid, this.nickname, this.lastIp);
    }

    @Override
    public UUID getUniqueId() {
        return this.uuid;
    }

    @Override
    public void setCalendar(CalendarUser calendarUser) {
        this.calendar = calendarUser;
    }
}
