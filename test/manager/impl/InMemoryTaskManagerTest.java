package manager.impl;

import org.junit.jupiter.api.Test;
import taskclass.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryTaskManagerTest extends TaskManagerTest {

    public InMemoryTaskManagerTest() {
        super(new InMemoryTaskManager());
    }

    @Test
    void shouldReturnTrueForGetTask() {
        createNewEpicAndTwoNewSubtasks();
        int idEpic = epic.getId();
        int idSubtask1 = subTask1.getId();
        int idSubtask2 = subTask2.getId();

        taskManager.getTask(idSubtask1);
        taskManager.getTask(idEpic);
        taskManager.getTask(idSubtask2);

        List<Task> idSequence = List.of(subTask1, epic, subTask2);
        List<Task> history = ((InMemoryTaskManager)taskManager).getInMemoryHistoryManager().getHistory();
        boolean condition = false;
        if(idSequence.size() != history.size()){
            assertTrue(condition);
        }else{
            condition = true;
            for(int i = 0; i < idSequence.size(); i++){
                if(!idSequence.get(i).equals(history.get(idSequence.size() - i - 1))){  //у меня обратный вывод при запросе
                    condition = false;
                    break;
                }
            }
            assertTrue(condition);
        }
    }
}
