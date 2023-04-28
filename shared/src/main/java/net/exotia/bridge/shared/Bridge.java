package net.exotia.bridge.shared;

import net.exotia.bridge.shared.http.HttpService;
import net.exotia.bridge.shared.services.UserService;

public abstract class Bridge {
    private HttpService httpService;

    public abstract ApiConfiguration getApiConfiguration();
    public abstract void runAsync(Runnable runnable);

    public UserService getUserService() {
        return new UserService(this.getApiConfiguration(), this);
    }
    public HttpService getHttpService() {
        if (this.httpService == null) this.httpService = new HttpService();
        return this.httpService;
    }
    public void stopHttpService() {
        if (this.httpService == null) return;
        this.httpService.getHttpClient().dispatcher().executorService().shutdown();
    }
}
