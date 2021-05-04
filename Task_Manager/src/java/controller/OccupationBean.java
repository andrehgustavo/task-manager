/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.sun.xml.bind.v2.TODO;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Occupation;

/**
 *
 * @author andre
 */
@ManagedBean
@SessionScoped
public class OccupationBean {

    private Occupation occupation = new Occupation();
    private List<Occupation> occupations;
    
    /**
     * Creates a OccupationBean new instance
     */
    public OccupationBean() {
        occupation.setId(null);
    }

    public Occupation getOccupation() {
        return occupation;
    }

    public void setOccupation(Occupation occupation) {
        this.occupation = occupation;
    }
    
    
    
    //METHODS
    
    public String newOccupation(String info) {
        this.occupation = new Occupation();
        return info + "occupationForm";
    }
    
    public String save() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            // Starts DB transaction
            em.getTransaction().begin();
            // Verify if exists
            if (occupation.getId() == null) {
                //Save occupation
                em.persist(occupation);
            } else {
                //Update occupation.
                occupation = em.merge(occupation);
            }
            //Publish transaction.
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        this.occupations = null;
        return "occupationList";
    }

    public String update(Occupation theOccupation) {
        this.occupation = theOccupation;
        return "occupationForm";
    }


    public List<Occupation> getOccupations() {
        EntityManager em = JPAUtil.getEntityManager();
        if (this.occupations == null) {
            Query query = em.createQuery("select o from Occupation o order by id",
                    Occupation.class);
            this.occupations = query.getResultList();
            em.close();
        }
        return occupations;
    }
    /** 
     * Todo: Tratar delete com o relacionamento
     */
    public void delete(Occupation theOccupation) {
        EntityManager em = JPAUtil.getEntityManager();
        if (theOccupation.getId() != null) {
            em.getTransaction().begin();
            theOccupation = em.merge(theOccupation);
            em.remove(theOccupation);
            em.getTransaction().commit();
            em.close();
        }

        this.occupations = null;

    }
    
    
    
    
}
