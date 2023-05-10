package net.exotia.bridge.shared.services.entities;

public class Update {
    private boolean updatable;
    private long last;

    public Update() {
        this.updatable = true;
    }
    public Update(boolean needs, long last) {
        this.updatable = needs;
        this.last = last;
    }

    public boolean isUpdatable() {
        return updatable;
    }

    public void setUpdatable(boolean updatable) {
        this.updatable = updatable;
    }

    public long getLast() {
        return last;
    }

    public void setLast(long last) {
        this.last = last;
    }
}
