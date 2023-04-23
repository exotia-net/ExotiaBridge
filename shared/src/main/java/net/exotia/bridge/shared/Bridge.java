package net.exotia.bridge.shared;

import net.exotia.bridge.shared.services.UserService;

public abstract class Bridge {
    public abstract ApiConfiguration getApiConfiguration();
    public abstract void async(Runnable runnable);

    public UserService getUserService() {
        return new UserService(this.getApiConfiguration(), this);
    }
}
