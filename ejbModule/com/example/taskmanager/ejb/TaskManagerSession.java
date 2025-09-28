package com.example.taskmanager.ejb;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.example.taskmanager.entity.Task;
import com.example.taskmanager.entity.User;

/**
 * Session Bean implementation class TaskManagerSession
 */
@Stateless
@LocalBean
public class TaskManagerSession implements TaskManagerSessionRemote, TaskManagerSessionLocal {

    private static final Logger logger = Logger.getLogger(TaskManagerSession.class.getName());

    @PersistenceContext
    private EntityManager em;

    // --- USER METHODS ---

    @Override
    public User createUser(User user) {
        if (user == null || user.getUsername() == null) {
            throw new IllegalArgumentException("Utilisateur invalide");
        }
        try {
            em.persist(user);
            return user;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de la création de l'utilisateur", e);
            return null;
        }
    }

    @Override
    public User findUserById(Long userId) {
        return em.find(User.class, userId);
    }

    @Override
    public User findUserByUsername(String username) {
        try {
            TypedQuery<User> query = em.createNamedQuery("User.findByUsername", User.class);
            query.setParameter("username", username);
            return query.getSingleResult();
        } catch (Exception e) {
            logger.log(Level.WARNING, "Utilisateur non trouvé pour le nom : " + username, e);
            return null;
        }
    }

    @Override
    public List<User> getAllUsers() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public void updateUser(User user) {
        if (user != null) {
            em.merge(user);
        }
    }

    @Override
    public void deleteUser(Long userId) {
        User user = findUserById(userId);
        if (user != null) {
            try {
                em.remove(user);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erreur lors de la suppression de l'utilisateur", e);
            }
        }
    }

    // --- TASK METHODS ---

    @Override
    public Task createTask(Task task) {
        if (task == null || task.getTitle() == null) {
            throw new IllegalArgumentException("Tâche invalide");
        }
        try {
            em.persist(task);
            return task;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de la création de la tâche", e);
            return null;
        }
    }

    @Override
    public Task findTaskById(Long taskId) {
        return em.find(Task.class, taskId);
    }

    @Override
    public List<Task> getAllTasks() {
        return em.createQuery("SELECT t FROM Task t", Task.class).getResultList();
    }

    @Override
    public List<Task> getTasksByUser(Long userId) {
        try {
            TypedQuery<Task> query = em.createQuery(
                "SELECT t FROM Task t WHERE t.user.userId = :userId", Task.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Erreur lors de la récupération des tâches de l'utilisateur", e);
            return null;
        }
    }

    @Override
    public void updateTask(Task task) {
        if (task != null) {
            em.merge(task);
        }
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = findTaskById(taskId);
        if (task != null) {
            try {
                em.remove(task);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Erreur lors de la suppression de la tâche", e);
            }
        }
    }

    // --- AUTHENTICATION ---

    @Override
    public User authenticate(String username, String password) {
        User user = findUserByUsername(username);
        if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}