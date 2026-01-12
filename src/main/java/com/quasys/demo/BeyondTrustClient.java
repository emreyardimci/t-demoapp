package com.quasys.demo;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BeyondTrustClient {

    private static final String BASE_URL =
            "https://pam.quasys.com.tr/BeyondTrust/api/public/v3";

    private static final String API_KEY = "c79488314430a91e477d32bad64278c007f3b2860712785d25082a8a430e3325b99fa2be60f7c0cb4c5bd289d5e591cf211d0bc235d1776390bb286dd7b69933";
    private static final String RUN_AS  = "emre.yardimci";
    private static final String PASSWORD = "[Hey14575079.111%]";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public BeyondTrustClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    /* -------------------------------------------------
     * PUBLIC METHOD
     * ------------------------------------------------- */
    public String getSecretText(String secretId) throws Exception {

        String accessToken = signIn();
        return fetchSecretText(secretId, accessToken);
    }

    /* -------------------------------------------------
     * AUTH
     * ------------------------------------------------- */
    private String signIn() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/Auth/SignAppin"))
                .header("Content-Type", "application/json")
                .header(
                        "Authorization",
                        "PS-Auth key=" + API_KEY +
                        "; runas=" + RUN_AS +
                        "; pwd=" + PASSWORD + ";"
                )
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("SignAppin failed: " + response.body());
        }

        JsonNode json = objectMapper.readTree(response.body());
        return json.get("access_token").asText();
    }

    /* -------------------------------------------------
     * SECRET FETCH
     * ------------------------------------------------- */
    private String fetchSecretText(String secretId, String accessToken)
            throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        BASE_URL +
                        "/Secrets-Safe/secrets/" +
                        secretId +
                        "/text?version=3.1"
                ))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Secret fetch failed: " + response.body());
        }

        JsonNode json = objectMapper.readTree(response.body());
        return json.get("text").asText();
    }
}
