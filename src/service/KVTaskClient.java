package service;

import utils.Managers;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class KVTaskClient {
    private final String url;
    private String token;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    public KVTaskClient(String url) {
        this.url = url;
        this.token = registerAPIToken(url);
        Managers.getGson();
    }

    private String registerAPIToken(String url) {

        HttpClient client = HttpClient.newHttpClient();
        URI uri = URI.create(url + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        try {
            HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString() ;
            HttpResponse<String> response = client.send(request, handler);
            token = response.body();
        } catch (InterruptedException | IOException exc) {
            System.out.println("Не удалось зарегистрировать токен: " + exc.getMessage());
        }
        return token;

    }
    protected void put(String key, String json) {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8078/put/" + key + "?API_TOKEN=" + token);
        HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json, DEFAULT_CHARSET);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (InterruptedException | IOException exc){
            System.out.println("Не удалось сохранить состояние менеджера: " + exc.getMessage());
        }
    }

    protected String load(String key){
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8078/put/" + key + "?API_TOKEN=" + token);
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = null;
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (InterruptedException | IOException exc){
            System.out.println("Не удалось восстановить состояние менеджера: " + exc.getMessage());
        }
        return response != null ? response.body() : "KVTaskClient.load() is greeting you";
    }
}
