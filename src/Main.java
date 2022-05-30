import enumclass.Status;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;
import taskmanager.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        System.out.println("Создаю 2 задачи, 1 эпик, 2 подзадачи\n _______________________________________");

        Task task1 = taskManager.createNewTask("Вынести мусор к машине", "Машина приезжает с 6 до 7");
        Task task2 = taskManager.createNewTask("Убраться к приезду родителей в гостинной",
                "Все моечные средства в кладовке");

        Epic epic1 = taskManager.createNewEpic("Расчет стоимости проекта дома", "Срок до 15.03.2022");

        SubTask subTusk1 = taskManager.createNewSubtask("Разработка архитектурного решения",
                "Срок до 03.02.2022", epic1);
        taskManager.getAllTasksName();

        SubTask subTusk2 = taskManager.createNewSubtask("Раcчет фундамента под архитектуру",
                "Срок до 05.02.2022", epic1);

        System.out.println("Вношу изменения во все задачи, меняю статусы\n _______________________________________");
        taskManager.updateTask(task1, "Вынести мусор к машине", "Машина приедет завтра",
                Status.IN_PROGRESS);
        taskManager.updateTask(task2, "Убраться к приезду родителей в гостинной",
                "Стоп, пока не купят химию", Status.IN_PROGRESS);
        taskManager.updateTask(epic1, "Расчет стоимости проекта дома",
                "Срок до 20.03.2022", null);
        taskManager.updateTask(subTusk1, "Разработка архитектурного решения",
                "Срок до 03.02.2022", Status.IN_PROGRESS);
        taskManager.updateTask(subTusk2, "Раcчет фундамента под архитектуру",
                "Срок до 05.02.2022", Status.IN_PROGRESS);
        taskManager.getAllTasksName();

        System.out.println("Удаляю задачи, меняю статусы\n _______________________________________");
        taskManager.deleteWithId(task1.getId());
        taskManager.deleteWithId(subTusk1.getId());
        taskManager.updateTask(subTusk2, "Раcчет фундамента под архитектуру",
                "Срок до 05.02.2022", Status.DONE);
        taskManager.getAllTasksName();

        System.out.println("Удаляю эпик, проверяю самоудаление subTask\n _______________________________________");
        taskManager.deleteWithId(epic1.getId());
        taskManager.getAllTasksName();
    }
}
