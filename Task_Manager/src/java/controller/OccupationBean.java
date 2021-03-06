/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.JPAUtil;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import model.Occupation;
import service.OccupationService;

/**
 *
 * @author andre
 */
@ManagedBean
@SessionScoped
public class OccupationBean {

    private Occupation occupation = new Occupation();
    private List<Occupation> occupations;
    
    @Inject
    private OccupationService service;

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
        service.save(occupation);

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
        try {
            if (theOccupation.getId() != null) {
                em.getTransaction().begin();
                theOccupation = em.merge(theOccupation);
                em.remove(theOccupation);
                em.getTransaction().commit();
            }

        } catch (Exception e) {
            String str = "N??o foi possivel apagar o cargo " + theOccupation.getName() + " porque ele possui relacionamentos com outra tabela no banco de dados. Apague primeiro os propriet??rios vinculados a este cargo.";
            FacesContext.getCurrentInstance().addMessage("erroId:xxx", new FacesMessage(str));
        }

        em.close();

        this.occupations = null;

    }

}
