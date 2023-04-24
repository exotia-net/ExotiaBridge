package net.exotia.bridge.api;

import net.exotia.bridge.api.user.ApiUserService;

public interface ExotiaBridgeInstance {
    ApiUserService getUserService();
}
