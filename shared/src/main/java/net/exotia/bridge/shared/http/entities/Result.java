package net.exotia.bridge.shared.http.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import okhttp3.Response;

@Getter
@AllArgsConstructor
public class Result {
    private Response response;
    private String responseString;
}
