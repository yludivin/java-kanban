package manager.impl;

import manager.interfaces.HistoryManager;
import taskclass.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Map<Integer, Task> historyIdBuffer;



    public InMemoryHistoryManager() {
        this.historyIdBuffer = new LinkedHashMap<>();
    }

    @Override
    public void add(Task task) {        //можно было просто передать в параметр order true, но я решил хоть что-то написать)
        Integer taskId = task.getId();
        if(historyIdBuffer.containsKey(taskId)){
            historyIdBuffer.remove(taskId);
            historyIdBuffer.put(taskId, task);
        }else{
            historyIdBuffer.put(taskId, task);
        }
    }

    @Override
    public void remove(int id) {        //у меня это не используется, но ТЗ требует
        historyIdBuffer.remove(id);
    }

    @Override
    public void getHistory() {
        Task[] taskArray = new Task[historyIdBuffer.size()];
        int index = historyIdBuffer.size() - 1;
        for (Map.Entry<Integer, Task> entry : historyIdBuffer.entrySet()) {
            taskArray[index--] = entry.getValue();
        }
        System.out.println(Arrays.toString(taskArray));
    }

}

