package five.ind.ems_mvc.service;

import five.ind.ems_mvc.entity.Task;
import five.ind.ems_mvc.entity.enums.TaskStatus;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    void updateEndDate(Long taskId, java.time.LocalDate endDate);
    Task createTask(Long managerId, Long projectId, Long teamId, String name, String description, String priority, Long assignedEmpId, LocalDate start, LocalDate due);
    List<Task> getTasksForProject(Long projectId);
    Task getTaskById(Long taskId);
    void updateTaskStatus(Long taskId, TaskStatus status);
}