package taskclass;

import enumclass.TypeTask;
import manager.interfaces.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {

    private List<Integer> subTaskListId;
    private static final LocalDateTime plugTime = LocalDateTime.of(1970,
            Month.JANUARY,1,0,0);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");
    //не могу убрать dateTimeFormatter из-за того, что не использовать его из Task при передачи в super()

    public Epic(String name, String description) {
        super(name, description, plugTime.format(dateTimeFormatter), 0);
        subTaskListId = new ArrayList<>();
    }

    public void calculateEndTime(TaskManager taskManager) {
        LocalDateTime endTime = LocalDateTime.MIN;
        for (Integer integer : subTaskListId) {
            if (!taskManager.getTaskMap().containsKey(integer)) {     //для восстановления с файла когда еще не
                continue;                                           //подгружены остальные подзадачи
            }
            if (taskManager.getTaskMap().get(integer).getEndTime().isAfter(endTime)) {
                endTime = taskManager.getTaskMap().get(integer).getEndTime();
            }
        }
        this.endTime = endTime;
    }

    public void calculateStartTime(TaskManager taskManager) {
        LocalDateTime startTime = LocalDateTime.MAX;
        for (Integer integer : subTaskListId) {
            if (!taskManager.getTaskMap().containsKey(integer)) {     //для восстановления с файла когда еще не
                continue;                                           //подгружены остальные подзадачи
            }
            if (taskManager.getTaskMap().get(integer).getStartTime().isBefore(startTime)) {
                startTime = taskManager.getTaskMap().get(integer).getStartTime();
            }
        }
        this.startTime = startTime;
    }

    public void calculateDuration() {
        if (this.startTime == plugTime || this.endTime == plugTime) {    //для восстановления с файла когда еще не
            return;                                                     //подгружены остальные подзадачи
        }
        this.duration = Duration.between(this.startTime, this.endTime);
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public int getId() {
        return this.id;
    }

    public void setSubTaskListId(List<Integer> subTaskListId) {
        this.subTaskListId = subTaskListId;
    }

    public void addNewSubTaskId(Integer id) {
        subTaskListId.add(id);
    }

    public List<Integer> getSubTaskListId() {
        return subTaskListId;
    }

    public void removeSubtaskId(Integer id) {
        subTaskListId.remove(subTaskListId.indexOf(id));
    }

    @Override
    public TypeTask getTypeTask() {
        return TypeTask.EPIC;
    }

    @Override
    public String toString() {
        String plugToString = "Эпик без подзадач";
        String startWork = (getStartTime() == plugTime) ? plugToString : dateTimeFormatter.format(getStartTime());
        String endWork = (getEndTime() == plugTime) ? plugToString : dateTimeFormatter.format(getEndTime());
        String duration = (getDuration() == Duration.ZERO) ? plugToString : getDuration().toMinutes() + "";
        return "ID: " + getId() + " " + getTypeTask().getName() + ":" + "\t" + getName() + "\n"
                + "Описание:\t" + getDescription() + "\n"
                + "Статус:\t" + getStatus() + "\n"
                + "Начало работы:\t" + startWork + "\n"
                + "Продолжительность(мин):\t" + duration + "\n"
                + "Завершение:\t" + endWork + "\n";
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}