/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JPAUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Task;
import model.Owner;
import model.Priority;
import model.Status;

/**
 *
 * @author andre
 */
@ManagedBean
@SessionScoped
public class TaskBean implements Serializable {

    private Task task = new Task();

    private List<Task> tasks;
    private List<Owner> owners;
    private int ownerId;
    private Long filterNumber;
    private String filterTitleDesc;
    private int filterOwnerId;
    private Status filterStatus;

    /**
     * Creates a TaskBean new instance
     */
    public TaskBean() {
        task.setId(null);
    }

    //getters and setters
    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public List<Owner> getOwners() {
        return owners;
    }

    public void setOwners(List<Owner> owners) {
        this.owners = owners;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Long getFilterNumber() {
        return filterNumber;
    }

    public void setFilterNumber(Long filterNumber) {
        this.filterNumber = filterNumber;
    }

    public String getFilterTitleDesc() {
        return filterTitleDesc;
    }

    public void setFilterTitleDesc(String filterTitleDesc) {
        this.filterTitleDesc = filterTitleDesc;
    }

    public int getFilterOwnerId() {
        return filterOwnerId;
    }

    public void setFilterOwnerId(int filterOwnerId) {
        this.filterOwnerId = filterOwnerId;
    }

    public Status getFilterStatus() {
        return filterStatus;
    }

    public void setFilterStatus(Status filterStatus) {
        this.filterStatus = filterStatus;
    }

    //methods
    public Priority[] getPriorities() {
        return Priority.values();
    }

    public Status[] getStatues() {
        return Status.values();
    }

    public String newTask(String info) {
        task = new Task();
        updateOwnerList();
        return info + "taskForm";
    }

    public String save() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            task.setOwner(em.find(Owner.class, Long.valueOf(ownerId)));
            // Starts DB transaction
            em.getTransaction().begin();
            // Verify if exists
            if (task.getId() == null) {
                //Save task
                task.setStatus(Status.IN_PROGRESS);
                em.persist(task);
            } else {
                //Update task.
                task = em.merge(task);
            }
            //Publish transaction.
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        this.tasks = null;
        return "taskList";
    }

    public String update(Task t) {
        this.task = t;
        this.ownerId = Math.toIntExact(t.getOwner().getId());
        return "taskForm";
    }

    public void closeTask(Task task) {
        EntityManager em = JPAUtil.getEntityManager();
        if (task.getId() != null) {
            task.setStatus(Status.FINISHED);
            em.getTransaction().begin();
            task = em.merge(task);
            em.getTransaction().commit();
            em.close();
        }

        this.tasks = null;

    }

    public List<Task> getTasks() {
        EntityManager em = JPAUtil.getEntityManager();
        if (this.tasks == null) {
            Query t = em.createQuery("select t from Task t order by id",
                    Task.class);
            this.tasks = t.getResultList();
            em.close();
        }
        return tasks;
    }

    public void delete(Task task) {
        EntityManager em = JPAUtil.getEntityManager();
        if (task.getId() != null) {
            em.getTransaction().begin();
            task = em.merge(task);
            em.remove(task);
            em.getTransaction().commit();
            em.close();
        }

        this.tasks = null;

    }

    public void doFilter() {
        EntityManager em = JPAUtil.getEntityManager();
        Query theTask = em.createQuery("select t from Task t order by id", Task.class);
        this.tasks = theTask.getResultList();
        List<Predicate<Task>> allPredicates = new ArrayList<>();
        if (this.getFilterNumber() != null) {
            allPredicates.add(t -> (t.getId()) == (this.getFilterNumber()).longValue());
        }
        if (this.getFilterTitleDesc() != null) {
            allPredicates.add(t -> t.getTitle().toLowerCase().contains(this.getFilterTitleDesc().toLowerCase()) || t.getDescription().toLowerCase().contains(this.getFilterTitleDesc().toLowerCase()));
        }
        if (this.getFilterOwnerId() != 0L) {
            allPredicates.add(t -> t.getOwner().getId() == this.getFilterOwnerId());
        }
        if (this.getFilterStatus() != null) {
            allPredicates.add(t -> t.getStatus() == this.getFilterStatus());
        }
        //FILTER
        if (allPredicates.size() > 0) {
            this.tasks = this.tasks.stream().filter(allPredicates.stream().reduce(x -> true, Predicate::and))
                    .collect(Collectors.toList());
        }
        em.close();
    }

    public void clearFilter() {
        EntityManager em = JPAUtil.getEntityManager();
        Query theTask = em.createQuery("select t from Task t order by id", Task.class);
        this.tasks = theTask.getResultList();
        filterNumber = null;
        filterTitleDesc = null;
        filterOwnerId = 0;
        filterStatus = null;
        em.close();
    }

    public void updateOwnerList() {
        EntityManager em = JPAUtil.getEntityManager();
        Query o = em.createQuery("select o from Owner o", Owner.class);
        this.owners = o.getResultList();
        em.close();
    }

}
