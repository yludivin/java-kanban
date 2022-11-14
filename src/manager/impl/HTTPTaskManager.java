package manager.impl;

import api.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import manager.impl.FileBackedTasksManager;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

public class HTTPTaskManager extends FileBackedTasksManager {
    private static URL url;
    private static URI uri;
    private static KVTaskClient kvTaskClient;
    private static Gson gson = new GsonBuilder().serializeNulls().create();


    public HTTPTaskManager(URI uri) throws IOException, URISyntaxException, InterruptedException {
        super(uri);
        this.uri = uri;
        kvTaskClient = new KVTaskClient(url);
    }

    @Override
    protected void save()  {
        try {
            kvTaskClient.put("Task", gson.toJson(getTaskMap()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public FileBackedTasksManager loadFromFile(Path file) {
        return super.loadFromFile(file);
    }
}

/*HttpTaskServer -> httpTaskManager -> KVClient -> KVServer и обратно.
        KVClient создается внутри httpTaskManager
        KVClient данные передает в HTTPtaskManager, где они уже обрабатываются
        KVClient и KVServer оба работают с данными в виде строк байтов. В/из json преобразуешь в менеджере*/