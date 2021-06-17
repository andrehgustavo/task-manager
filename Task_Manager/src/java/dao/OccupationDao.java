/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import model.Occupation;

/**
 *
 * @author andre
 */
public class OccupationDao {
    @Inject
    private EntityManager em;

    public OccupationDao() {
        this.em = JPAUtil.getEntityManager();
    }
    
    public void save(Occupation occupation) {
        
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
    }
    
}
