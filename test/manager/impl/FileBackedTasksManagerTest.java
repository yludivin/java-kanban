package manager.impl;

import exeptions.ManagerSaveException;
import manager.interfaces.HistoryManager;
import manager.interfaces.TaskManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {
    private static String pathTestFile = "D:\\yp\\java-kanban\\test\\dataTest\\testData";
    private FileBackedTasksManager taskManager;
    private List<String> stringsFile;       //в тестах с записью из файлов тут хранятся строки с задачами
    private static List<String> expectedLines;
    private static Path testDataToLoadFromFile =
            Path.of("D:\\yp\\java-kanban\\test\\dataTest\\testDataForLoadToFile");


    public FileBackedTasksManagerTest() {
        taskManager = new FileBackedTasksManager(Path.of(pathTestFile));
    }

    @BeforeEach
    void clearTaskmanagerAndFile() {
        taskManager.deleteAll();
    }

    @Test
    void shouldReturn3ForFileBackedTasksManager1Task1Epic1Subtask() {     //проверяю что 3 задачи записались в файл
        int expectedCoincidences = 4;
        int realCoincidences = 0;
        createTaskEpicTwoSubtask();
        initTestExpectedLines();
        loadFromFile(Path.of(pathTestFile));
        for (int i = 0; i < expectedCoincidences; i++) {
            if (stringsFile.get(i).equals(expectedLines.get(i))) {
                realCoincidences++;
            }
        }
        Assertions.assertEquals(expectedCoincidences, realCoincidences);
    }

    @Test
    void shouldReturnTrueForFileBackedTasksManagerLoadFromFileTasks() {
        TaskManager testManager = (TaskManager) createTasksAndHistory()[0];
        FileBackedTasksManager fbtmNew = taskManager.loadFromFile(testDataToLoadFromFile);
        if(!testManager.getTaskMap().keySet().equals(taskManager.getTaskMap().keySet())){
            assertTrue(false);
            return;
        }
        int lastId = 7;
        for(int i = 1; i <= lastId; i++){
            Task realTask = taskManager.getTaskMap().get(i);
            Task expectedTask = testManager.getTaskMap().get(i);
            if(!realTask.toString().equals(expectedTask.toString())){
                assertTrue(false);
            }
        }
        assertTrue(true);
    }

    @Test
    void shouldReturnTrueForFileBackedTasksManagerLoadFromFileHistory() {
        HistoryManager testHistoryManager = (HistoryManager) createTasksAndHistory()[1];
        FileBackedTasksManager fbtmNew = taskManager.loadFromFile(testDataToLoadFromFile);

        List<Task> expectedHistory = testHistoryManager.getHistory();
        List<Task> realHistory = taskManager.inMemoryHistoryManager.getHistory();
        if (expectedHistory.size() != realHistory.size()) {
            Assertions.assertTrue(false);
            return;
        }
        for (int i = 0; i < expectedHistory.size(); i++) {
            if (expectedHistory.get(i).getId() != realHistory.get(i).getId()) {
                Assertions.assertTrue(false);
                return;
            }
        }
        Assertions.assertTrue(true);
    }


    @Test
    void shouldReturnTrueForFileBackedTasksManagerHistoryEmpty() {
        createTaskEpicTwoSubtask();
        initTestExpectedLines();
        loadFromFile(Path.of(pathTestFile));
        String expectedEmptyHistory = "[]";
        String lineWithEmptyHistory = stringsFile.get(stringsFile.size() - 1);
        assertTrue(expectedEmptyHistory.equals(lineWithEmptyHistory));
    }

    private void loadFromFile(Path file) {
        stringsFile = new LinkedList<>();
        try (FileReader fr = new FileReader(file.toFile());
             BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                stringsFile.add(br.readLine());
            }
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл не найден", e);
        } catch (IOException e) {
            throw new ManagerSaveException("Проблема при считывании файла с ПЗУ", e);
        }
    }

    private void createTaskEpicTwoSubtask() {
        task = taskManager.createNewTask(createTestTask());
        epic = (Epic) taskManager.createNewEpic(createTestEpic());
        subTask1 = (SubTask) taskManager.createNewSubtask(createTestSubtask1());
        subTask2 = (SubTask) taskManager.createNewSubtask(createTestSubtask2());

    }

    private void initTestExpectedLines(){
        expectedLines = new ArrayList<>();
        expectedLines.add("1,TASK,anyName,NEW,anyDescription,12:20 30-08-2022,PT1H,13:20 30-08-2022");
        expectedLines.add("2,EPIC,anyName,NEW,anyDescription,12:20 24-08-2022,PT12H5M,00:25 25-08-2022,sub tasks - [3, 4]");
        expectedLines.add("3,SUB_TASK,anyName,NEW,anyDescription,12:20 24-08-2022,PT5H,17:20 24-08-2022,2");
        expectedLines.add("4,SUB_TASK,anyName,NEW,anyDescription,17:45 24-08-2022,PT6H40M,00:25 25-08-2022,2");
    }

/*    public Task createTestTask(){
        String startTime = "12:20 30-08-2022";
        long duration = 60;
        task = new Task(NAME_TASK, DESCRIPTION_TASK, startTime,duration);
        return task;
    }

    public SubTask createTestSubtask1(){
        String startTime = "12:20 24-08-2022";
        long duration = 300;
        subTask1 = new SubTask(NAME_TASK, DESCRIPTION_TASK,startTime,duration, epic);
        return subTask1;
    }
    public SubTask createTestSubtask2(){
        String startTime = "17:45 24-08-2022";
        long duration = 400;
        subTask2 = new SubTask(NAME_TASK, DESCRIPTION_TASK,startTime,duration, epic);
        return subTask2;
    }

    public Epic createTestEpic(){
        epic = new Epic(NAME_TASK, DESCRIPTION_TASK);
        return epic;
    }*/
    private Object[] createTasksAndHistory() {
        TaskManager testManager = new InMemoryTaskManager();

        Task taskf1 = testManager.createNewTask(
                new Task("Вынести мусор к машине", "Машина приезжает с 6 до 7",
                        "10:15 24-08-2022", 60));
        Task taskf2 = testManager.createNewTask(
                new Task("Убраться к приезду родителей в гостинной", "Все моечные средства в кладовке",
                        "11:20 24-08-2022", 55));

        Epic epicf3 = (Epic) testManager.createNewEpic(
                new Epic("Расчет стоимости проекта дома","ЖБИ еще не готово"));
        SubTask subTaskf4 = (SubTask) testManager.createNewSubtask(
                new SubTask("Разработка архитектурного решения", "Рома в отпуске",
                        "12:20 24-08-2022", 300, epicf3 ));
        SubTask subTaskf5 = (SubTask) testManager.createNewSubtask(
                new SubTask("Раcчет фундамента под архитектуру", "металл подорожал",
                        "17:45 24-08-2022", 400, epicf3));
        SubTask subTaskf6 = (SubTask) testManager.createNewSubtask(
                new SubTask("Раcчет вентиляции", "в основе - Арктика", "12:20 25-08-2022",
                        400,epicf3));

        Epic epicf7 = (Epic) testManager.createNewEpic(
                new Epic("Формирование списка мебели", "брать Питерские фирмы"));

        HistoryManager testHistoryManager = new InMemoryHistoryManager();
        //у меня реверсивный вывод. Добавляю в обратном порядке
        testHistoryManager.add(taskf1);
        testHistoryManager.add(taskf2);
        testHistoryManager.add(subTaskf4);
        testHistoryManager.add(epicf7);

        Object[] returnObjects = {testManager, testHistoryManager};
        return returnObjects;
    }
}
