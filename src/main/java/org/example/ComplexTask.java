package org.example;

public class ComplexTask {
    private final int taskId;

    public ComplexTask(int taskId) {
        this.taskId = taskId;
    }

    public String execute() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Task" + taskId + "interrupted";
        }
        return "Task" + taskId + "completed";
    }
}

