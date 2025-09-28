package com.example.taskmanager.ejb;

import java.util.List;

import javax.ejb.Remote;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;

@Remote
public interface TaskManagerSessionRemote {

    // User methods
    User createUser(User user);
    User findUserById(Long userId);
    User findUserByUsername(String username);
    List<User> getAllUsers();
    void updateUser(User user);
    void deleteUser(Long userId);

    // Task methods
    Task createTask(Task task);
    Task findTaskById(Long taskId);
    List<Task> getAllTasks();
    List<Task> getTasksByUser(Long userId);
    void updateTask(Task task);
    void deleteTask(Long taskId);

    // Auth methods
    User authenticate(String username, String password);
}
