package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private HttpClient client;
    private URL url;
    private String apiToken;


    public KVTaskClient(URL url) throws URISyntaxException, IOException, InterruptedException {
        client = HttpClient.newBuilder().build();
        this.url = url;
        register();
    }

    private void register() throws URISyntaxException, IOException, InterruptedException {
        URI uri = new URI(url.toString() + "/register");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        apiToken = response.body();
    }


    public void put(String key, String json) throws IOException, InterruptedException, URISyntaxException {
        //POST /save/<ключ>?API_TOKEN=
        URI uri = new URI(this.url.toString() + "/save/key?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new RuntimeException("Ошибка при отправке запроса");
        }

    }

    public String load(String key) throws URISyntaxException, IOException, InterruptedException {
        //GET /load/<ключ>?API_TOKEN=
        URI uri = new URI(this.url.toString() + "/load/key?API_TOKEN=" + apiToken);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode() != 200){
            throw new RuntimeException("Ошибка при получении ответа");
        }
        return response.body();
    }

}

/*HttpTaskServer -> httpTaskManager -> KVClient -> KVServer и обратно.
        KVClient создается внутри httpTaskManager
        KVClient данные передает в HTTPtaskManager, где они уже обрабатываются
        KVClient и KVServer оба работают с данными в виде строк байтов. В/из json преобразуешь в менеджере*/
