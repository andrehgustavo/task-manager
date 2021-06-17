/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JPAUtil;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Occupation;
import model.Owner;

/**
 *
 * @author andre
 */
@ManagedBean
@SessionScoped
public class OwnerBean {

    private Owner owner = new Owner();
    private List<Owner> owners;
    private List<Occupation> occupations;
    private int occupationId;


    /**
     * Creates a OwnerBean new instance
     */
    public OwnerBean() {
        owner.setId(null);
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Occupation> getOccupations() {
        return occupations;
    }

    public void setOccupations(List<Occupation> occupations) {
        this.occupations = occupations;
    }

    public int getOccupationId() {
        return occupationId;
    }

    public void setOccupationId(int occupationId) {
        this.occupationId = occupationId;
    }

    //Methods
    public String newOwner(String info) {
        owner = new Owner();
        updateOwnerList();
        this.occupationId = 0;
        return info + "ownerForm";
    }

    public String save() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            owner.setOccupation(em.find(Occupation.class, Long.valueOf(occupationId)));
            // Starts DB transaction
            em.getTransaction().begin();
            // Verify if exists
            if (owner.getId() == null) {
                //Save owner
                em.persist(owner);
            } else {
                //Update owner.
                owner = em.merge(owner);
            }
            //Publish transaction.
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        this.owners = null;
        return "ownerList";
    }

    public String update(Owner theOwner) {
        this.owner = theOwner;
        this.occupationId = Math.toIntExact(theOwner.getOccupation().getId());
        return "ownerForm";
    }

    public List<Owner> getOwners() {
        EntityManager em = JPAUtil.getEntityManager();
        if (this.owners == null) {
            Query query = em.createQuery("select o from Owner o order by id",
                    Owner.class);
            this.owners = query.getResultList();
            em.close();
        }
        return owners;
    }

    public void delete(Owner theOwner) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            if (theOwner.getId() != null) {
            em.getTransaction().begin();
            theOwner = em.merge(theOwner);
            em.remove(theOwner);
            em.getTransaction().commit();
        }   
        } catch (Exception e) {
            String str = "Não foi possivel apagar o proprietário " + theOwner.getName() + " porque ele possui relacionamentos. Apague primeiro a tarefa vinculada a ele.";
            FacesContext.getCurrentInstance().addMessage("erroId:xxx", new FacesMessage(str));
        }
        
        em.close();        
        this.owners = null;

    }

    public void updateOwnerList() {
        EntityManager em = JPAUtil.getEntityManager();
        Query o = em.createQuery("select o from Occupation o", Occupation.class);
        this.occupations = o.getResultList();
        em.close();
    }

}
