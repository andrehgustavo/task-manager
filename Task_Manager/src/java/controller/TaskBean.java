/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.util.FilterTask;
import java.io.Serializable;
import java.util.List;
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
public class TaskBean implements Serializable{

    private Task task = new Task();
    
    private List<Task> tasks;
    private List<Owner> owners;
    private int ownerId;
    private FilterTask filterTask;
    
    @PostConstruct
    public void init(){
        EntityManager em = JPAUtil.getEntityManager();
        if (this.owners == null) {
            Query o = em.createQuery("select o from Owner o", Owner.class);
            this.owners = o.getResultList();
            em.close();
        }
    }


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

    public FilterTask getFilterTask() {
        return filterTask;
    }

    public void setFilter(FilterTask filterTask) {
        this.filterTask = filterTask;
    }
    
    
    

    //methods
    public Priority[] getPriorities(){
        return Priority.values();
    }
    
    public Status[] getStatues(){
        return Status.values();
    }
    
    public String newTask(String info) {
        task = new Task();
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
            Query t = em.createQuery("select t from Task t",
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
    
    public void doFilter(){
        //TO DO
//        EntityManager em = JPAUtil.getEntityManager();
//        Query t = em.createQuery("select t from Task t",
//                    Task.class);
//            this.tasks = t.getResultList();
//            
//            this.tasks = this.tasks.stream().filter(t -> t.getId() == filter.getNumber() &&
//                            t.getTitle()==);
//            em.close();
    }

}
