package net.exotia.bridge.shared.services.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
