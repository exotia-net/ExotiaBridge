package net.exotia.bridge.api.entities;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class CalendarUser {
    private int step;
    private List<Integer> notObtainedRewards;
    private long lastObtained;
    private int streakDays;

    public CalendarUser(int step, List<Integer> notObtainedRewards, long lastObtained, int streakDays) {
        this.step = step;
        this.notObtainedRewards = notObtainedRewards;
        this.lastObtained = lastObtained;
        this.streakDays = streakDays;
    }

    private boolean hasStreak() {
        if (this.lastObtained == 0) return true;
        return LocalDateTime.now().minusHours(40).isBefore(
                LocalDateTime.ofInstant(Instant.ofEpochMilli(this.lastObtained), ZoneId.systemDefault())
        );
    }

    public void reset() {
        this.step = 0;
        this.notObtainedRewards = new ArrayList<>();
        this.lastObtained = 0;
        this.streakDays = 0;
    }

    public void addStep(int size) {
        if (this.step >= size) return;
        if (this.hasStreak()) this.streakDays++;
        this.step++;
        this.lastObtained = Instant.now().toEpochMilli();
    }
    public void addStreak() {
        this.streakDays++;
    }
    public void addNotObtained(int step) {
        this.notObtainedRewards.add(step);
    }
    public void removeNotObtained(int index) {
        this.notObtainedRewards.remove(index);
    }
    public boolean canObtain(int size) {
        if (this.step >= size) return false;
        return LocalDateTime.now().isAfter(
                LocalDateTime.ofInstant(Instant.ofEpochMilli(this.lastObtained), ZoneId.systemDefault()).plusDays(1).withHour(6)
        );
    }

    public int getStep() {
        return this.step;
    }

    public List<Integer> getNotObtainedRewards() {
        return this.notObtainedRewards;
    }

    public long getLastObtained() {
        return this.lastObtained;
    }

    public int getStreakDays() {
        return this.streakDays;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setNotObtainedRewards(List<Integer> notObtainedRewards) {
        this.notObtainedRewards = notObtainedRewards;
    }

    public void setLastObtained(long lastObtained) {
        this.lastObtained = lastObtained;
    }

    public void setStreakDays(int streakDays) {
        this.streakDays = streakDays;
    }
}
