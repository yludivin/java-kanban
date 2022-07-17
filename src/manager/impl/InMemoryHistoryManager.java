package manager.impl;

import enumclass.TypeTask;
import manager.interfaces.HistoryManager;
import taskclass.Epic;
import taskclass.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    CustomLinkedList customLinkedList;
    Map<Integer, CustomLinkedList.Node> taskHashMap;

    public InMemoryHistoryManager() {
        this.customLinkedList = new CustomLinkedList();
        this.taskHashMap = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        Integer taskId = task.getId();
        if (taskHashMap.containsKey(taskId)) {
            customLinkedList.removeNode(taskHashMap.get(taskId));
            customLinkedList.add(task);
            taskHashMap.put(taskId, customLinkedList.getFirst());
        } else {
            customLinkedList.add(task);
            taskHashMap.put(taskId, customLinkedList.getFirst());
        }
    }

    @Override
    public void remove(int id) {
        Task task = (Task)taskHashMap.get(id).item;
        if(task.getTypeTask() == TypeTask.EPIC){
            List<Integer> subtaskList = ((Epic)task).getSubTaskListId();
            for (Integer taskId : subtaskList) {
                customLinkedList.removeNode(taskHashMap.get(taskId));
            }
        }
        customLinkedList.removeNode(taskHashMap.get(id));
    }

    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    private class CustomLinkedList {
        Task task;
        Node<Task> first;
        Node<Task> last;
        int size = 0;

        public void add(Task task) {
            Node newNode;
            if(first == null){
                newNode = new Node(null, task, null);
                first = newNode;
                last = newNode;
            }else{
                Node nextNode = first;
                first = new Node(null, task, nextNode);
                last = nextNode;
                nextNode.prev = first;
            }
            this.size++;
        }

        public void removeNode(Node node) {

            if(node.prev == null){
                node.next.prev = null;
                this.first = node.next;
            }else if(node.next == null){
                node.prev.next = null;
                this.last = node.prev;
            }else{
                node.prev.next = node.next;
                node.next.prev = node.prev;
            }
            this.size--;
        }

        public Node<Task> getFirst() {
            return first;
        }

        public List<Task> getTasks() {
            List<Task> allTasks = new ArrayList<>();
            for (CustomLinkedList.Node<Task> x = first; x != null; x = x.next) {
                allTasks.add(x.getItem());
            }
            return allTasks;
        }

        class Node<Task> {
            private Task item;
            private CustomLinkedList.Node<Task> next;
            private CustomLinkedList.Node<Task> prev;

            Node(CustomLinkedList.Node<Task> prev, Task element, CustomLinkedList.Node<Task> next) {
                this.item = element;
                this.next = next;
                this.prev = prev;
            }

            public Node<Task> getNext() {
                return next;
            }

            public Node<Task> getPrev() {
                return prev;
            }

            public void setNext(Node<Task> next) {
                this.next = next;
            }

            public void setPrev(Node<Task> prev) {
                this.prev = prev;
            }

            public Task getItem() {
                return item;
            }
        }
    }
}

