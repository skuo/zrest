package org.zrest.client;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class Client {
    private static String serverUrl = "http://localhost:8080/zrest-web";
    RestTemplate restTemplate = new RestTemplate();

    public String getBidWithHeader(String sourceId, String source) {
        String url = serverUrl + "/bids/{sourceId}/source/{source}";
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Content-Type", "application/json");
        requestHeaders.set("Authorization", "zrest:signature");
        HttpEntity<?> requestEntity = new HttpEntity<>(requestHeaders);

        HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class, sourceId, source);

        String responseHeader = response.getHeaders().getFirst("Approved");
        System.out.println("Approved=" + responseHeader);
        String body = response.getBody();
        System.out.println("body = " + body);
        return body;
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.getBidWithHeader("sourceId", "wp");
    }
}
