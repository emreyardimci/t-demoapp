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

    // Bunları env'den okumak daha güvenli. Şimdilik demo amaçlı sabit.
    private static final String API_KEY = "c79488314430a91e477d32bad64278c007f3b2860712785d25082a8a430e3325b99fa2be60f7c0cb4c5bd289d5e591cf211d0bc235d1776390bb286dd7b69933";
    private static final String RUN_AS  = "emre.yardimci";
    private static final String PASSWORD = "[Hey14575079.111%]";

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public BeyondTrustClient() {
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1) // HTTP_1_1_REQUIRED fix
                .build();
        this.objectMapper = new ObjectMapper();
    }

    public String getSecretText(String secretId) throws Exception {
        String sessionCookie = signInAndGetSessionCookie(); // "ASP.NET_SessionId=...."
        return fetchSecretTextWithCookie(secretId, sessionCookie);
    }

    private String signInAndGetSessionCookie() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/Auth/SignAppin"))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .header("Authorization",
                        "PS-Auth key=" + API_KEY +
                        "; runas=" + RUN_AS +
                        "; pwd=[" + PASSWORD + "];")
                // curl --data '' ile aynı: boş body
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("SignAppin failed (" + response.statusCode() + "): " + response.body());
        }

        // Set-Cookie header'ından ASP.NET_SessionId'yi al
        String setCookie = response.headers()
                .firstValue("set-cookie")
                .orElseThrow(() -> new RuntimeException("SignAppin succeeded but Set-Cookie header is missing"));

        // Örnek: "ASP.NET_SessionId=oemyvfit...; path=/; secure; HttpOnly; SameSite=Lax"
        String sessionPair = extractCookiePair(setCookie, "ASP.NET_SessionId");
        if (sessionPair == null) {
            throw new RuntimeException("Set-Cookie içinde ASP.NET_SessionId bulunamadı: " + setCookie);
        }

        // Opsiyonel: response body parse ederek log amaçlı kullanıcı adı vs. okunabilir
        // JsonNode whoAmI = objectMapper.readTree(response.body());

        return sessionPair; // "ASP.NET_SessionId=...."
    }

    private String fetchSecretTextWithCookie(String secretId, String sessionCookiePair) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        BASE_URL + "/Secrets-Safe/secrets/" + secretId + "/text?version=3.1"
                ))
                .header("Accept", "application/json")
                .header("Cookie", sessionCookiePair)
                .GET()
                .build();

        HttpResponse<String> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Secret fetch failed (" + response.statusCode() + "): " + response.body());
        }

        JsonNode json = objectMapper.readTree(response.body());
        JsonNode textNode = json.get("text");
        if (textNode == null || textNode.isNull()) {
            throw new RuntimeException("Response içinde 'text' alanı yok: " + response.body());
        }
        return textNode.asText();
    }

    // "Set-Cookie" değerinden "cookieName=value" pair'ini çıkarır
    private static String extractCookiePair(String setCookieHeader, String cookieName) {
        // birden fazla cookie tek header'da gelirse diye kaba ayrım:
        // "a=b; ... , c=d; ..." senaryolarına karşı split(",") riskli olabilir,
        // ancak burada genelde tek cookie geliyor. Yine de güvenli tarafta kalıp doğrudan arayalım.
        int idx = setCookieHeader.indexOf(cookieName + "=");
        if (idx < 0) return null;

        int start = idx;
        int end = setCookieHeader.indexOf(';', start);
        if (end < 0) end = setCookieHeader.length();

        return setCookieHeader.substring(start, end).trim(); // "ASP.NET_SessionId=...."
    }
}
