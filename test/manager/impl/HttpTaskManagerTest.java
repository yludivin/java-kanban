package manager.impl;

import api.KVServer;
import manager.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URL;


import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest {
    HTTPTaskManager taskManager;
    KVServer kvServer;

    @BeforeEach
    void createManager() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = Managers.getHTTPTaskManagerByPath(new URL("http://localhost:8078/"));

    }

    @AfterEach
    void stopKV() {
        kvServer.stop();
    }

    @Test
    void saveAndLoad() {

    }
}