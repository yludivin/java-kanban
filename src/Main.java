import enumclass.Status;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;
import taskmanager.InMemoryTaskManager;

public class Main {

    public static void main(String[] args) {
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager();

        System.out.println("Создаю 2 задачи, 1 эпик, 2 подзадачи\n _______________________________________");

        Task task1 = inMemoryTaskManager.createNewTask("Вынести мусор к машине", "Машина приезжает с 6 до 7");
        Task task2 = inMemoryTaskManager.createNewTask("Убраться к приезду родителей в гостинной",
                "Все моечные средства в кладовке");

        Epic epic1 = (Epic) inMemoryTaskManager.createNewEpic("Расчет стоимости проекта дома", "Срок до 15.03.2022");

        SubTask subTusk1 = (SubTask) inMemoryTaskManager.createNewSubtask("Разработка архитектурного решения",
                "Срок до 03.02.2022", epic1);
        inMemoryTaskManager.getAllTasksName();

        SubTask subTusk2 = (SubTask) inMemoryTaskManager.createNewSubtask("Раcчет фундамента под архитектуру",
                "Срок до 05.02.2022", epic1);

        System.out.println("Вношу изменения во все задачи, меняю статусы\n _______________________________________");
        inMemoryTaskManager.updateTask(task1, "Вынести мусор к машине", "Машина приедет завтра",
                Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(task2, "Убраться к приезду родителей в гостинной",
                "Стоп, пока не купят химию", Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(epic1, "Расчет стоимости проекта дома",
                "Срок до 20.03.2022", null);
        inMemoryTaskManager.updateTask(subTusk1, "Разработка архитектурного решения",
                "Срок до 03.02.2022", Status.IN_PROGRESS);
        inMemoryTaskManager.updateTask(subTusk2, "Раcчет фундамента под архитектуру",
                "Срок до 05.02.2022", Status.IN_PROGRESS);
        inMemoryTaskManager.getAllTasksName();

        System.out.println("Удаляю задачи, меняю статусы\n _______________________________________");
        inMemoryTaskManager.deleteWithId(task1.getId());
        inMemoryTaskManager.deleteWithId(subTusk1.getId());
        inMemoryTaskManager.updateTask(subTusk2, "Раcчет фундамента под архитектуру",
                "Срок до 05.02.2022", Status.DONE);
        inMemoryTaskManager.getAllTasksName();

        System.out.println("Удаляю эпик, проверяю самоудаление subTask\n _______________________________________");
        inMemoryTaskManager.deleteWithId(epic1.getId());
        inMemoryTaskManager.getAllTasksName();
    }
}