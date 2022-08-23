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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {
    private static String pathTestFile = "D:\\yp\\java-kanban\\test\\dataTest\\testData";
    private FileBackedTasksManager taskManager;
    private List<String> stringsFile;
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
        taskManager.createNewTask(TEXT, TEXT);
        epic = (Epic) taskManager.createNewEpic(TEXT, TEXT);
        taskManager.createNewSubtask(TEXT, TEXT, epic);
        taskManager.createNewSubtask(TEXT, TEXT, epic);
        expectedLines = new ArrayList<>();
        expectedLines.add("1,TASK,SomeText,NEW,SomeText");
        expectedLines.add("2,EPIC,SomeText,NEW,SomeText,sub tasks - [3, 4]");
        expectedLines.add("3,SUB_TASK,SomeText,NEW,SomeText,2");
        expectedLines.add("4,SUB_TASK,SomeText,NEW,SomeText,2");
    }

    private Object[] createTasksAndHistory() {
        TaskManager testManager = new InMemoryTaskManager();
        Task task1 = testManager.createNewTask("Вынести мусор к машине", "Машина приезжает с 6 до 7");
        Task task2 = testManager.createNewTask("Убраться к приезду родителей в гостинной",
                "Все моечные средства в кладовке");
        Epic epic3 = (Epic) testManager.createNewEpic("Расчет стоимости проекта дома",
                "Срок до 15.03.2022");
        SubTask subTask4 = (SubTask) testManager.createNewSubtask("Разработка архитектурного решения",
                "Срок до 03.02.2022", epic3);
        SubTask subTask5 = (SubTask) testManager.createNewSubtask("Раcчет фундамента под архитектуру",
                "Срок до 05.02.2022", epic3);
        SubTask subTask6 = (SubTask) testManager.createNewSubtask("Раcчет вентиляции",
                "Срок до 08.02.2022", epic3);
        Epic epic7 = (Epic) testManager.createNewEpic("Формирование списка мебели",
                "Срок до 17.06.2022");
        HistoryManager testHistoryManager = new InMemoryHistoryManager();
        //у меня реверсивный вывод. Добавляю в обратном порядке
        testHistoryManager.add(task1);
        testHistoryManager.add(task2);
        testHistoryManager.add(subTask4);
        testHistoryManager.add(epic7);

        Object[] returnObjects = {testManager, testHistoryManager};
        return returnObjects;
    }
}
