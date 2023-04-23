package net.exotia.bridge.plugin.http;

import com.google.gson.Gson;
import net.exotia.bridge.plugin.api.requests.CreateUserRequest;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class HttpService {
    private static final MediaType JSON = MediaType.parse("application/json");
    private OkHttpClient httpClient = new OkHttpClient();
    private Gson gson = new Gson();

    public OkHttpClient getHttpClient() {
        return this.httpClient;
    }
    public <T> void get(String uri, Class<T> tClass, BiConsumer<T, Response> function, Map<String, String> headers){
        HttpResult result = this.sendRequest("GET", uri, null, tClass, headers);
        function.accept((T) result.getObject(), result.getResponse());
    }
    public <T> void post(String uri, Class<T> tClass, BiConsumer<T, Response> function, Map<String, String> headers) {
        HttpResult result = this.sendRequest("POST", uri, null, tClass, headers);
        function.accept((T) result.getObject(), result.getResponse());
    }
    public <T1, T2 extends RequestObject> void post(String uri, T2 json, Class<T1> tClass, BiConsumer<T1, Response> function, Map<String, String> headers) {
        HttpResult result = this.sendRequest("POST", uri, json, tClass, headers);
        function.accept((T1) result.getObject(), result.getResponse());
    }

    private  <T1, T2 extends RequestObject> HttpResult sendRequest(String method, String uri, T2 json, Class<T1> tClass, Map<String, String> headers) {
        RequestBody body = RequestBody.create(this.gson.toJson(json), JSON);
        Request.Builder builder = new Request.Builder()
                .url(uri)
                .method(method, json == null ? null : body)
                .addHeader("Content-Type", "application/json");
        if (headers != null) headers.forEach(builder::addHeader);
        Request request = builder.build();
        try (Response response = this.httpClient.newCall(request).execute()) {
            assert response.body() != null;
            return new HttpResult(tClass == null ? null : this.gson.fromJson(response.body().string(), tClass), response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

