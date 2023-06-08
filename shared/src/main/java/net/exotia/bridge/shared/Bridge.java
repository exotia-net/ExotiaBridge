package net.exotia.bridge.shared;

import net.exotia.bridge.shared.http.HttpService;
import net.exotia.bridge.shared.services.CalendarService;
import net.exotia.bridge.shared.services.EconomyService;
import net.exotia.bridge.shared.services.UserService;
import java.util.List;
import java.util.logging.Logger;

public abstract class Bridge {
    private HttpService httpService;
    private UserService userService;

    public abstract ApiConfiguration getApiConfiguration();

    public UserService getUserService() {
        this.userService = new UserService(this.getApiConfiguration(), this);
        return this.userService;
    }
    public EconomyService getEconomyService() {
        return new EconomyService(this.userService);
    }
    public CalendarService getCalendarService() {
        return new CalendarService(this.userService);
    }
    public HttpService getHttpService() {
        if (this.httpService == null) this.httpService = new HttpService();
        return this.httpService;
    }
    public void stopHttpService() {
        if (this.httpService == null) return;
        this.httpService.getHttpClient().dispatcher().executorService().shutdown();
    }

    public void pluginLoadedMessage(Logger logger) {
        List.of(
                " _____          _   _      ______      _     _            ",
                "|  ___|        | | (_)     | ___ \\    (_)   | |           ",
                "| |____  _____ | |_ _  __ _| |_/ /_ __ _  __| | __ _  ___ ",
                "|  __\\ \\/ / _ \\| __| |/ _` | ___ \\ '__| |/ _` |/ _` |/ _ \\",
                "| |___>  < (_) | |_| | (_| | |_/ / |  | | (_| | (_| |  __/",
                "\\____/_/\\_\\___/ \\__|_|\\__,_\\____/|_|  |_|\\__,_|\\__, |\\___|",
                "                                                __/ |     ",
                "                                               |___/      ",
                "Plugin has been enabled!",
                "Version for: " + (this.getApiConfiguration().isProxyServer() ? "Proxy Server" : "Spigot Server")
        ).forEach(logger::info);
    }
}
