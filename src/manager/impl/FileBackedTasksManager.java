package manager.impl;

import enumclass.Status;
import enumclass.TypeTask;
import exeptions.ManagerSaveException;
import taskclass.Epic;
import taskclass.SubTask;
import taskclass.Task;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final Path path;
    protected DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy");

    public FileBackedTasksManager(Path path) {
        super();
        this.path = path;
    }

    @Override
    public Task createNewTask(Task task) {
        Task tmpTask = super.createNewTask(task);
        save();
        return tmpTask;
    }

    @Override
    public Task createNewSubtask(SubTask subTask) {         //меняю метод суперкласса тк уже подгрузил сабтаски в эпик
        int id = ++taskId;
        subTask.setId(id);
        tasksMap.put(taskId, subTask);

        if (subTask.getEpicId() != 0) {      //при парсинге из ПЗУ EpicId устанавливается дальше.
            List<Integer> subtasksId = ((Epic) tasksMap.get(subTask.getEpicId())).getSubTaskListId();
            if (!subtasksId.contains(subTask.getId())) {      //если данные уже подгружены, то в список подзадач не добавляем ничег
                ((Epic) tasksMap.get(subTask.getEpicId())).addNewSubTaskId(taskId);
                addTaskToPriortizedSet(subTask);
                refreshEpicLastTime((tasksMap.get(subTask.getEpicId())).getId());
                refreshEpicStartTime((tasksMap.get(subTask.getEpicId())).getId());
                refreshEpicDuration((tasksMap.get(subTask.getEpicId())).getId());
                validateTask();
            }
        }
        save();
        return subTask;
    }

    @Override
    public Task createNewEpic(Epic epic) {
        Task tmpTask = super.createNewEpic(epic);
        save();
        return tmpTask;
    }

    @Override
    public void getTask(Integer id) {
        super.getTask(id);
        save();
    }

    private void save() {
        if (!Files.exists(path)) {
            try {
                Files.createFile(path);
                try (FileWriter fw = new FileWriter(path.toFile())) {
                    fw.write("id,type,name,status,description,epic");
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Проблема создания файла данных на ПЗУ", e);
            }
        } else {
            try (FileWriter fw = new FileWriter(path.toFile())) {
                String newLine;
                for (Map.Entry<Integer, Task> task : tasksMap.entrySet()) {
                    newLine = toString(task.getValue());
                    fw.write(newLine);
                }
                fw.write(toStringHistory());
            } catch (IOException e) {
                throw new ManagerSaveException("Проблема создания файла данных на ПЗУ", e);
            }
        }
    }

    public String toString(Task task) {
        //id,type,name,status,description,!<new>starttime,duration,endtime<new>!,epic
        switch (task.getTypeTask()) {
            case EPIC:
                return task.getId() + "," + task.getTypeTask() + "," + task.getName() + "," +
                        task.getStatus() + "," + task.getDescription() + "," +
                        task.getStartTime().format(dateTimeFormatter) + "," +
                        task.getDuration() + "," + task.getEndTime().format(dateTimeFormatter) + "," + "sub tasks - " +
                        ((Epic) task).getSubTaskListId().toString() + "\n";
            case TASK:
                return task.getId() + "," + task.getTypeTask() + "," + task.getName() + "," +
                        task.getStatus() + "," + task.getDescription() + "," +
                        task.getStartTime().format(dateTimeFormatter) + "," +
                        task.getDuration() + "," + task.getEndTime().format(dateTimeFormatter) + "\n";
            case SUBTASK:
                return task.getId() + "," + task.getTypeTask() + "," + task.getName() + "," +
                        task.getStatus() + "," + task.getDescription() + "," +
                        task.getStartTime().format(dateTimeFormatter) + "," +
                        task.getDuration() + "," + task.getEndTime().format(dateTimeFormatter) +
                        "," + ((SubTask) task).getEpicId() + "\n";

        }
        return null;
    }

    public String toStringHistory() {
        List<Task> taskList = new ArrayList<>(getInMemoryHistoryManager().getHistory());
        List<String> idTasks = new LinkedList<>();
        for (Task task : taskList) {
            idTasks.add(String.valueOf(task.getId()));
        }
        return "\n" + idTasks;
    }

    public Task fromString(String value) {
        //id0,type1,name2,status3,description4,!<new>starttime5,duration6,endtime7<new>!,epic8
        String[] taskMetadate = value.split(",");
        Task task = null;
        switch (TypeTask.valueOf(taskMetadate[1])) {
            case TASK:
                Task newTask = new Task(taskMetadate[2], taskMetadate[4], taskMetadate[5],
                        Duration.parse(taskMetadate[6]).toMinutes());
                task = createNewTask(newTask);
                task.setId(Integer.parseInt(taskMetadate[0]));
                task.setStatus(Status.valueOf(taskMetadate[3]));
                break;
            case SUBTASK:
                Epic tmpEpic = (Epic) tasksMap.get(Integer.valueOf(taskMetadate[8]));
                SubTask newSubtask = new SubTask(taskMetadate[2], taskMetadate[4], taskMetadate[5],
                        Duration.parse(taskMetadate[6]).toMinutes(), tmpEpic);
                task = createNewSubtask(newSubtask);
                task.setId(Integer.parseInt(taskMetadate[0]));
                task.setStatus(Status.valueOf(taskMetadate[3]));
                break;
            case EPIC:
                Epic newEpic = new Epic(taskMetadate[2], taskMetadate[4]);
                task = createNewEpic(newEpic);
                task.setId(Integer.parseInt(taskMetadate[0]));
                task.setStatus(Status.valueOf(taskMetadate[3]));
                setStartEndTimeDuration(task, taskMetadate[5], taskMetadate[7]);
                break;
        }
        return task;
    }

    private List<Integer> valuesInBrackets(String line) {
        int startIndex = line.indexOf('[') + 1;
        int endIndex = line.indexOf(']');
        String valuesInString = line.substring(startIndex, endIndex);
        String[] values = valuesInString.split(",");
        List<Integer> result = new LinkedList<>();
        for (String value : values) {
            if (Objects.equals(value.strip(), "")) {
                return Collections.emptyList();
            }
            result.add(Integer.valueOf(value.strip()));
        }
        return result;
    }

    public FileBackedTasksManager loadFromFile(Path file) {
        List<String> stringsFile = new LinkedList<>();
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

        FileBackedTasksManager newFbtm = new FileBackedTasksManager(file);
        for (String s : stringsFile) {
            if (s.isBlank()) {
                break;
            }
            Task newTask = fromString(s);
            newFbtm.tasksMap.put(newTask.getId(), newTask);
        }

        String historyInString = stringsFile.get(stringsFile.size() - 1);
        List<Integer> historyIdInStrings = new ArrayList<>(valuesInBrackets(historyInString));
        this.setInMemoryHistoryManager(loadHistoryManager(historyIdInStrings));
        newFbtm.setInMemoryHistoryManager(loadHistoryManager(historyIdInStrings));
        return newFbtm;
    }

    private InMemoryHistoryManager loadHistoryManager(List<Integer> historyIdInStrings) {
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        List<Task> taskList = new LinkedList<>();
        for (Integer taskId : historyIdInStrings) {
            taskList.add(tasksMap.get(taskId));
        }
        for (Task task : taskList) {
            inMemoryHistoryManager.add(task);
        }
        return inMemoryHistoryManager;
    }

    void setStartEndTimeDuration(Task task, String startTime, String endTime) {
        task.setStartTime(LocalDateTime.parse(startTime, dateTimeFormatter));
        task.setEndTime(LocalDateTime.parse(endTime, dateTimeFormatter));
        task.setDuration((int) Duration.between(task.getStartTime(), task.getEndTime()).toMinutes());
    }
}