package net.exotia.bridge.shared.http;

import lombok.AllArgsConstructor;
import okhttp3.Response;

@AllArgsConstructor
public class HttpResponse<T> {
    private Response response;
    private T object;

    public T get() {
        return this.object;
    }
    public Response getResponse() {
        return this.response;
    }
}
