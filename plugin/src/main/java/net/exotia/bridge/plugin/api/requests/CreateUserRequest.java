package net.exotia.bridge.plugin.api.requests;

import lombok.AllArgsConstructor;
import net.exotia.bridge.plugin.http.RequestObject;

@AllArgsConstructor
public class CreateUserRequest extends RequestObject {
    private String uuid;
    private String ip;
}
