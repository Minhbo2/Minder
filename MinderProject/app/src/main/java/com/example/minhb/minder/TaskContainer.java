package com.example.minhb.minder;

public class TaskContainer
{
    private String taskName;
    private String taskDescription;
    private String taskId;

    public TaskContainer(String taskId, String taskName, String taskDescription)
    {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public String getTaskId() { return taskId; }
}
