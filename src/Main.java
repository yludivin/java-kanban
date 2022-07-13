import enumclass.Status;
import managers.Managers;
import taskclass.Epic;
import taskclass.SubTask;
import manager.impl.InMemoryTaskManager;
import manager.interfaces.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

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

        taskManager.getTask(1);
        taskManager.getTask(3);
        taskManager.getTask(5);
        taskManager.getTask(7);
        System.out.println("Первый вывод истории \n _______________________________________");
        getHistory(taskManager);
        System.out.println("Второй вывод истории \n _______________________________________");
        taskManager.getTask(3);
        taskManager.getTask(1);

        getHistory(taskManager);
    }

    private static void getHistory(TaskManager taskManager){
        ((InMemoryTaskManager)taskManager).getInMemoryHistoryManager().getHistory();
    }
}