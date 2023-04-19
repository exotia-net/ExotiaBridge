package net.exotia.bridge.plugin.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import okhttp3.Response;

@Getter
@AllArgsConstructor
public class HttpResult {
    private Object object;
    private Response response;
}
