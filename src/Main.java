import manager.Managers;
import manager.impl.FileBackedTasksManager;
import taskclass.Epic;
import taskclass.SubTask;
import manager.impl.InMemoryTaskManager;
import manager.interfaces.TaskManager;
import taskclass.Task;

import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
/*        TaskManager taskManager = Managers.getDefault();

        System.out.println("Создаю 2 задачи, 1 эпик, 3 подзадачи, 1 эпик пустой\n _______________________________________");

        taskclass.Task task1 = taskManager.createNewTask("Вынести мусор к машине", "Машина приезжает с 6 до 7");
        taskclass.Task task2 = taskManager.createNewTask("Убраться к приезду родителей в гостинной",
                "Все моечные средства в кладовке");

        Epic epic1 = (Epic) taskManager.createNewEpic("Расчет стоимости проекта дома", "Срок до 15.03.2022");

        SubTask subTusk1 = (SubTask) taskManager.createNewSubtask("Разработка архитектурного решения",
                "Срок до 03.02.2022", epic1);


        SubTask subTusk2 = (SubTask) taskManager.createNewSubtask("Раcчет фундамента под архитектуру",
                "Срок до 05.02.2022", epic1);

        SubTask subTusk3 = (SubTask) taskManager.createNewSubtask("Раcчет вентиляции",
                "Срок до 08.02.2022", epic1);

        Epic epic2 = (Epic) taskManager.createNewEpic("Формирование списка мебели", "Срок до 17.06.2022");
        taskManager.getAllTasksName();
        System.out.println("Просматриваю 5 задач \n _______________________________________");
        taskManager.getTask(1);
        taskManager.getTask(3);
        taskManager.getTask(5);
        taskManager.getTask(7);
        System.out.println("Первый вывод истории \n _______________________________________");
        getHistory(taskManager);
        System.out.println("Просматриваю еще 2 задачи \n _______________________________________");
        taskManager.getTask(3);
        System.out.println("Второй вывод истории \n _______________________________________");
        getHistory(taskManager);
        taskManager.getTask(1);
        System.out.println("Третий вывод истории \n _______________________________________");
        getHistory(taskManager);
        System.out.println("Просматриваю еще 5 задач \n _______________________________________");
        taskManager.getTask(2);
        taskManager.getTask(4);
        taskManager.getTask(6);
        taskManager.getTask(3);
        taskManager.getTask(3);
        System.out.println("Четвертый вывод истории \n _______________________________________");
        getHistory(taskManager);
        System.out.println("Удаляю Эпик Id 3 с подзадачами Id 4,Id 5, Id 6 \n _______________________________________");
        deleteTask(taskManager,3);
        System.out.println("Пятый вывод истории \n _______________________________________");
        getHistory(taskManager);
        System.out.println("Удаляю Задачу Id 2 \n _______________________________________");
        deleteTask(taskManager,2);
        System.out.println("Шестой вывод истории \n _______________________________________");
        getHistory(taskManager);*/

        TaskManager taskManager = new InMemoryTaskManager();

        Task task1 = taskManager.createNewTask(
                new Task("Вынести мусор к машине", "Машина приезжает с 6 до 7",
                        "10:15 24-08-2022", 60));
        Task task2 = taskManager.createNewTask(
                new Task("Убраться к приезду родителей в гостинной", "Все моечные средства в кладовке",
                        "11:20 24-08-2022", 55));

        Epic epic3 = (Epic) taskManager.createNewEpic(
                new Epic("Расчет стоимости проекта дома","ЖБИ еще не готово"));
        SubTask subTask4 = (SubTask) taskManager.createNewSubtask(
                new SubTask("Разработка архитектурного решения", "Рома в отпуске",
                        "12:20 24-08-2022", 300, epic3 ));
        SubTask subTask5 = (SubTask) taskManager.createNewSubtask(
                new SubTask("Раcчет фундамента под архитектуру", "металл подорожал",
                        "17:45 24-08-2022", 400, epic3));
        SubTask subTask6 = (SubTask) taskManager.createNewSubtask(
                new SubTask("Раcчет вентиляции", "в основе - Арктика", "12:20 25-08-2022",
                 400,epic3));

        Epic epic7 = (Epic) taskManager.createNewEpic(
                new Epic("Формирование списка мебели", "брать Питерские фирмы"));

        System.out.println(task2);

        System.out.println("\n\n");
        System.out.println(subTask4);
        System.out.println("\n\n");
        System.out.println(epic3);
        System.out.println("\n\n");
        System.out.println("Вывод задач в рамках приоритета");
        System.out.println(taskManager.getPrioritizedTasks());

        System.out.println("\n\n");

        System.out.println("Обновляю задачи 2 шт - 1 , 5");

        taskManager.updateTask(task1,
                new Task("Вынести мусор к машине", "Перенос на 5 дней",
                        "10:15 29-08-2022", 60));

        taskManager.updateTask(subTask5,
                new SubTask("Раcчет фундамента под архитектуру", "перенос",
                        "17:45 27-08-2022", 400, epic3));
        System.out.println("Вывод задач в рамках приоритета");
        System.out.println(taskManager.getPrioritizedTasks());



        FileBackedTasksManager fbtm1 = new FileBackedTasksManager(
                Path.of("D:\\yp\\java-kanban\\src\\data\\saveData1.txt"));
        Task taskf1 = fbtm1.createNewTask(
                new Task("Вынести мусор к машине", "Машина приезжает с 6 до 7",
                        "10:15 24-08-2022", 60));
        Task taskf2 = fbtm1.createNewTask(
                new Task("Убраться к приезду родителей в гостинной", "Все моечные средства в кладовке",
                        "11:20 24-08-2022", 55));

        Epic epicf3 = (Epic) fbtm1.createNewEpic(
                new Epic("Расчет стоимости проекта дома","ЖБИ еще не готово"));
        SubTask subTaskf4 = (SubTask) fbtm1.createNewSubtask(
                new SubTask("Разработка архитектурного решения", "Рома в отпуске",
                        "12:20 24-08-2022", 300, epic3 ));
        SubTask subTaskf5 = (SubTask) fbtm1.createNewSubtask(
                new SubTask("Раcчет фундамента под архитектуру", "металл подорожал",
                        "17:45 24-08-2022", 400, epic3));
        SubTask subTaskf6 = (SubTask) fbtm1.createNewSubtask(
                new SubTask("Раcчет вентиляции", "в основе - Арктика", "12:20 25-08-2022",
                        400,epic3));

        Epic epicf7 = (Epic) fbtm1.createNewEpic(
                new Epic("Формирование списка мебели", "брать Питерские фирмы"));

        fbtm1.getTask(2);
        fbtm1.getTask(4);
        fbtm1.getTask(7);
        fbtm1.getTask(4);
        fbtm1.getTask(2);
        fbtm1.getTask(1);
        fbtm1.getTask(2);

        FileBackedTasksManager fbtm2 = new FileBackedTasksManager(
                Path.of("D:\\yp\\java-kanban\\src\\data\\saveData2.txt"));


        fbtm2.loadFromFile(Path.of("D:\\yp\\java-kanban\\src\\data\\saveData1.txt"));
        fbtm2.getTask(6);
        fbtm2.getTask(1);

    }

    private static void getHistory(TaskManager taskManager){
        List<Task> taskList = new ArrayList<>(((InMemoryTaskManager)taskManager).getInMemoryHistoryManager().getHistory());
        for(Task task : taskList){
            System.out.println(task);
        }
    }

    private static void deleteTask(TaskManager taskManager, int id){
        ((InMemoryTaskManager)taskManager).getInMemoryHistoryManager().remove(id);
    }
}