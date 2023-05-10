package net.exotia.bridge.shared.http;

import com.google.gson.Gson;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.http.entities.Dto;
import net.exotia.bridge.shared.http.entities.RequestResult;
import net.exotia.bridge.shared.http.entities.Result;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;

import static net.exotia.bridge.shared.Endpoints.*;

public class HttpService {
    private static final MediaType JSON = MediaType.parse("application/json");
    private OkHttpClient httpClient = new OkHttpClient();
    private Gson gson = new Gson();

    public OkHttpClient getHttpClient() {
        return this.httpClient;
    }

    public WebSocket prepareWebSocketConnection(ApiConfiguration apiConfiguration, WebSocketListener webSocketListener) {
        OkHttpClient wsClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
        Request request = new Request.Builder()
                .url(this.changeProtocols(getUri(WEBSOCKET, apiConfiguration)))
                .header(AUTH_HEADER, new ExotiaPlayer(
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        "0", "0.0.0.0").getCipher(apiConfiguration))
                .build();
        return wsClient.newWebSocket(request, webSocketListener);
    }
    private String changeProtocols(String string) {
        return string.replace("http", "ws").replace("https", "wss");
    }

    public <T> T get(String uri, Class<T> tClass, Map<String, String> headers){
        Request.Builder builder = new Request.Builder()
                .url(uri)
                .get()
                .addHeader("Content-Type", "application/json");

        if (headers != null) headers.forEach(builder::addHeader);
        Request request = builder.build();
        try (Response response = this.httpClient.newCall(request).execute()) {
            assert response.body() != null;
            String responseString = response.body().string();
            return this.gson.fromJson(responseString, tClass);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T> void get(String uri, Class<T> tClass, BiConsumer<T, Result> function, Map<String, String> headers){
        RequestResult result = this.sendRequest(HttpMethod.GET, uri, null, tClass, headers);
        function.accept((T) result.getObject(), new Result(result.getResponse(), result.getResponseString()));
    }

    public <T> void post(String uri, Class<T> tClass, BiConsumer<T, Result> function, Map<String, String> headers) {
        RequestResult result = this.sendRequest(HttpMethod.POST, uri, null, tClass, headers);
        function.accept((T) result.getObject(), new Result(result.getResponse(), result.getResponseString()));
    }
    public <T1, T2 extends Dto> void post(String uri, T2 json, Class<T1> tClass, BiConsumer<T1, Result> function, Map<String, String> headers) {
        RequestResult result = this.sendRequest(HttpMethod.POST, uri, json, tClass, headers);
        function.accept((T1) result.getObject(), new Result(result.getResponse(), result.getResponseString()));
    }

    public <T> void put(String uri, Class<T> tClass, BiConsumer<T, Result> function, Map<String, String> headers) {
        RequestResult result = this.sendRequest(HttpMethod.PUT, uri, null, tClass, headers);
        function.accept((T) result.getObject(), new Result(result.getResponse(), result.getResponseString()));
    }
    public <T1, T2 extends Dto> void put(String uri, T2 json, Class<T1> tClass, BiConsumer<T1, Result> function, Map<String, String> headers) {
        RequestResult result = this.sendRequest(HttpMethod.PUT, uri, json, tClass, headers);
        function.accept((T1) result.getObject(), new Result(result.getResponse(), result.getResponseString()));
    }

    private  <T1, T2 extends Dto> RequestResult sendRequest(HttpMethod method, String uri, T2 json, Class<T1> tClass, Map<String, String> headers) {
        RequestBody body = RequestBody.create(this.gson.toJson(json), JSON);
        RequestBody finalBody = method != HttpMethod.GET ? (json == null ? RequestBody.create(null, new byte[]{}) : body) : null;
        Request.Builder builder = new Request.Builder()
                .url(uri)
                .method(method.name(), finalBody)
                .addHeader("Content-Type", "application/json");

        if (headers != null) headers.forEach(builder::addHeader);
        Request request = builder.build();
        try (Response response = this.httpClient.newCall(request).execute()) {
//            assert response.body() != null;
            String responseString = response.body().string();
            return new RequestResult(tClass == null ? null : this.gson.fromJson(responseString, tClass), response, response.code() + " | " + responseString);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
