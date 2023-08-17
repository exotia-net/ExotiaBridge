package net.exotia.bridge.shared.http;

import com.google.gson.Gson;
import net.exotia.bridge.shared.ApiConfiguration;
import net.exotia.bridge.shared.http.entities.RequestDto;
import net.exotia.bridge.shared.services.entities.ExotiaPlayer;
import okhttp3.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

import static net.exotia.bridge.shared.Endpoints.*;

public class HttpService {
    private static final MediaType JSON = MediaType.parse("application/json");
    private final OkHttpClient httpClient = new OkHttpClient();
    private final Gson gson = new Gson();

    public OkHttpClient getHttpClient() {
        return this.httpClient;
    }

    public WebSocket prepareWebSocketConnection(ApiConfiguration apiConfiguration, WebSocketListener webSocketListener) {
        OkHttpClient wsClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .build();
        Request request = new Request.Builder()
                .url(this.switchProtocols(getUri(WEBSOCKET, apiConfiguration)))
                .header(AUTH_HEADER, new ExotiaPlayer(
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        "0", "0.0.0.0").getCipher(apiConfiguration))
                .build();
        return wsClient.newWebSocket(request, webSocketListener);
    }
    public String getApiKey(ApiConfiguration configuration) {
        return new ExotiaPlayer(UUID.fromString("00000000-0000-0000-0000-000000000000"), "0", "0.0.0.0").getCipher(configuration);
    }
    private String switchProtocols(String string) {
        return string.replace("http", "ws").replace("https", "wss");
    }

    public <T> HttpResponse<T> get(String uri, Class<T> tClass, Map<String, String> headers){
        Request.Builder builder = new Request.Builder().url(uri).get()
                .addHeader("Content-Type", "application/json");

        if (headers != null) headers.forEach(builder::addHeader);
        Request request = builder.build();
        try (Response response = this.httpClient.newCall(request).execute()) {
            assert response.body() != null;
            String responseString = response.body().string();
            if (tClass == null) return new HttpResponse<T>(response, (T) "ee");
            return new HttpResponse<>(response, this.gson.fromJson(responseString, tClass));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response get(String uri, Map<String, String> headers){
        Request.Builder builder = new Request.Builder().url(uri).get().addHeader("Content-Type", "application/json");
        if (headers != null) headers.forEach(builder::addHeader);
        Request request = builder.build();

        try {
            return this.httpClient.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <T1, T2 extends RequestDto> HttpResponse<T1> post(String uri, Class<T1> responseType, T2 body, Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(this.gson.toJson(body), JSON);
        Request.Builder builder = new Request.Builder().url(uri).post(requestBody)
                .addHeader("Content-Type", "application/json");

        if (headers != null) headers.forEach(builder::addHeader);
        Request request = builder.build();
        try (Response response = this.httpClient.newCall(request).execute()) {
            assert response.body() != null;
            String responseString = response.body().string();
            if (responseType == null) return new HttpResponse<T1>(response, (T1) responseString);
            return new HttpResponse<>(response, this.gson.fromJson(responseString, responseType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public <T> HttpResponse<T> post(String uri, Class<T> responseType, Map<String, String> headers) {
        RequestBody requestBody = RequestBody.create(this.gson.toJson(RequestBody.create(null, new byte[]{})), JSON);
        Request.Builder builder = new Request.Builder().url(uri).post(requestBody)
                .addHeader("Content-Type", "application/json");

        if (headers != null) headers.forEach(builder::addHeader);
        Request request = builder.build();
        try (Response response = this.httpClient.newCall(request).execute()) {
            assert response.body() != null;
            String responseString = response.body().string();
            if (responseType == null) return new HttpResponse<T>(response, (T) responseString);
            return new HttpResponse<>(response, this.gson.fromJson(responseString, responseType));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
